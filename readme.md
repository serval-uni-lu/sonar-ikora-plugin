# Code quality for Robot Framework

[![License](https://img.shields.io/badge/License-LGPL%203.0-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0.en.html)

## Description

sonar-ikora-plugin is a code analyzer for Robot Framework.

## Features

* 28 Rules
* Metrics (number of lines, code duplication)

## Build requirements

* Java Development Kit (JDK) 11 or higher
* Maven 3.6.0 or higher

## Build from source

1. Clone the project on your machine using ```git clone https://github.com/kabinja/sonar-ikora-plugin.git``` .
2. Move to the directory.
3. run the Maven command ```mvn clean install```.

## Limitations

* The code present in Python files is not covered by this plugin, which may lead to misses when resolving keyword definitions.
* Currently, the libraries are defined in a fixed [JSON file](https://raw.githubusercontent.com/UL-SnT-Serval/ikora-core/master/src/main/resources/libraries.json).

## License

Code is under the [GNU LESSER GENERAL PUBLIC LICENSE Version 3](https://www.gnu.org/licenses/lgpl-3.0.en.html).