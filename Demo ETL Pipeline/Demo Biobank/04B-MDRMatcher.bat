@echo off

@echo Started: %date% %time%

java -XX:-UseGCOverheadLimit -jar ../MDRMatcher.jar -t metadata\ccdg.tsv -s metadata\local.tsv -o mappings\local-ccdg.tsv

@echo Finished: %date% %time%

pause