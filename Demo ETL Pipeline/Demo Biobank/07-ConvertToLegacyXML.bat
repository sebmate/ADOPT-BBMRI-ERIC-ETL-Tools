@ECHO OFF
perl ..\convert-to-legacy-XML.pl xmlFiles\local-ccdg.xml > xmlFiles\Legacy.xml
echo ==== Syntactic Correctnes ====
echo(
..\xml.exe val -s ..\relaxed_import.xsd xmlFiles\Legacy.xml 
echo(
REM echo ==== Completeness ====
REM echo(
REM ..\xml.exe val -s ..\strict_import.xsd xmlFiles\Legacy.xml 
REM echo(
pause