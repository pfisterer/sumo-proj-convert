Convert SUMO UTM coordinates to WGS84 (latitude/longitude)
=============
This is an attempt, to convert SUME coordinates in the output file (emmission output) to lat/lon coordinates and add them to the output XML file.   

Requirements
======
To build, you need

* Java 8 or higher (<http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>) 
* Maven 3 or higher (<http://maven.apache.org/>)

Building and Setup
======
Before cloning this repository, be sure to enable automatic conversion of CRLF/LF on your machine using `git config --global core.autocrlf input`. For more information, please  refer to <http://help.github.com/dealing-with-lineendings/>

To build the project, run `mvn package`, this will build the program and place the generated jar file in the directory `target/`.

Running
======
To Run, use `mvn exec:java`, e.g., as follows: `mvn exec:java -Dexec.args="--in src/main/resources/sumo-demo-out.xml --out target/sumo-demo-out-lon-lat.xml --zone 17 --offsetx -664189.34 --offsety -4857879.23 --verbose` 

You can obtain the parameters from Sumo's netconvert file. Example: 
```XML
 <location netOffset="-469060.73,-5466718.90" convBoundary="0.00,0.00,15919.26,11855.03" origBoundary="8.573426,49.352580,8.793576,49.459560" projParameter="+proj=utm +zone=32 +ellps=WGS84 +datum=WGS84 +units=m +no_defs"/>
```
The parameters would be `mvn exec:java -Dexec.args="--in src/main/resources/sumo-demo-out.xml --out target/sumo-demo-out-lon-lat.xml --zone 32 --offsetx -469060.73 --offsety -5466718.90`
