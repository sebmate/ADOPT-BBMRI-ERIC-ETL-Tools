#!/usr/bin/perl -w

# Original Version by Petr Holub

use v5.14;
use strict;
use experimental 'smartmatch';
use feature "switch";
use Data::Dumper;

#use XML::XPath;
#use XML::XPath::XMLParser;

use XML::Twig;

my $global_event_no = 1;

my $inXMLfname = shift @ARGV;

#my $inXML = XML::XPath->new(filename => $inXMLfname);

my $twig=XML::Twig->new();    # create the twig
$twig->parsefile($inXMLfname); # build it

my $root = $twig->root;
$root->set_tag('BHImport');
$root->set_att("xmlns"=>"http://registry.samply.de/schemata/import_v1");

my @mdr_url = $root->get_xpath('Mdr/URL');
$mdr_url[0]->set_text('https://mdr.osse-register.de/v3/api/mdr');

my @patients = $root->children('OSSEPatient');

foreach my $p (@patients) {
	$p->set_tag('BHPatient');

	my @dataelements = $p->descendants('Dataelement');
	foreach my $d (@dataelements) {
		my $mdrkey = $d->att('mdrkey');
		$mdrkey =~ s/^urn:ccdg:d/D/;
		$mdrkey =~ s/:/_/g;
		$d->set_tag($mdrkey);
		$d->del_att('mdrkey');
	}

	my @bd = $p->descendants('BasicData');
	$bd[0]->wrap_in('BasicData');
	$bd[0]->set_tag('Form');
	$bd[0]->set_att('name' => "form_28_ver-27");

	my @events = $p->descendants('Event');
	foreach my $e (@events) {
		my $new_e = $e->wrap_in('LogitudinalData')->wrap_in('Event');
		$new_e->set_atts($e->atts);
		if (!defined $new_e->att('name') || $new_e->att('name') eq "") {
			$new_e->set_att('name' => ++$global_event_no);
		}
		elsif ($new_e->att('name') =~ /^\d+$/ && $new_e->att('name') <= $global_event_no) {
			die "Collision of even name with my own artificial event number generator";
		}

		my $e_type = $e->att('eventtype');
		$e->del_atts;
	
		for ($e_type) {
			when ("Surgery") {$e->set_tag('Form'); $e->set_att('name' => 'form_32_ver-8'); }
			when ("Sample") {$e->set_tag('Form1'); $e->set_att('name' => 'form_35_ver-6'); }
			when ("Histopathology") {$e->set_tag('Form2'); $e->set_att('name' => 'form_34_ver-22'); }
			when ("Pharmacotherapy") {$e->set_tag('Form3'); $e->set_att('name' => 'form_33_ver-10'); }
			when ("Response to therapy") {$e->set_tag('Form4'); $e->set_att('name' => 'form_31_ver-2'); }
			when ("Radiation therapy") {$e->set_tag('Form5'); $e->set_att('name' => 'form_29_ver-5'); }
			when ("Targeted therapy") {$e->set_tag('Form6'); $e->set_att('name' => 'form_30_ver-3'); }
			when ("Targeted Therapy") {$e->set_tag('Form6'); $e->set_att('name' => 'form_30_ver-3'); }
			default {die "Unknown type of event: " . $e_type . "\n"};
		}
	}
}

$twig->set_pretty_print('indented');
$twig->print;                 # output the twig
