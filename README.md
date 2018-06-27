# ADOPT-BBMRI-ERIC-ETL-Tools
The Samply.MDR-based ETL tools from ADOPT BBMRI-ERIC.

This repository contains the ETL tools being developed as part of (ADOPT) BBMRI-ERIC. An overview of the workflow is shown in the figure below.

![Overview](https://github.com/sebmate/ADOPT-BBMRI-ERIC-ETL-Tools/blob/master/Pipeline%20Overview.png)

- The TablePreprocessor tool parses an Excel file (or any other tabular data) and rotates the data into the CSV EAV format. It also generates a Local Metadata Definition File.
- If the data is provided in the CSV format, the biobank has to enter its metadata into the Biobank Namespace of the MDR. Then the MDRExtractor tool can be used to download a Local Metadata Definition File from the MDR. Similarly, a Central Metadata Definition File has to be downloaded from the MDR, which contains the CCDC terminology.
- Both Metadata Definition Files can be matched with the MDRMatcher tool. This tool generates a Mapping File, which has to be verified by a human expert.
- The MappingGUI program can be used to edit the mapping files.
- The ETLHelper tool can process a CSV file in combination with the Mapping File and transform the data into the XML representation.

Note that this pipeline is demonstrated in the "Demo ETL Pipeline" folder, which also includes the Java binaries of the tools. You can walk through the process by running the BAT files one after each other (01-, 04-, 05-, 06-...bat).

![BBMRI-ERIC Mapping GUI](https://github.com/sebmate/ADOPT-BBMRI-ERIC-ETL-Tools/blob/master/MappingGUI.png)
