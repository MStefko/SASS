# Super-resolution Acquisition Simulation Software

- [![Build Status](https://travis-ci.org/LEB-EPFL/SASS.svg?branch=master)](https://travis-ci.org/LEB-EPFL/SASS)
- [![Coverage Status](https://coveralls.io/repos/github/LEB-EPFL/SASS/badge.svg?branch=master)](https://coveralls.io/github/LEB-EPFL/SASS?branch=master)
- [![Join the chat at https://gitter.im/leb_epfl/SASS](https://badges.gitter.im/leb_epfl/SASS.svg)](https://gitter.im/leb_epfl/SASS?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Toolkit for simulating non-stationary fluorescence photodynamics and
real-time control systems for super-resolution microscopy.

## Documentation

http://sass.readthedocs.io/en/latest/

## Usage

## API notice ##

The API for SASS is still under development and may change until major
version 1.0.0. Any methods or classes that are marked as deprecated
will be removed by this release.

## Installation

1. Download the latest .jar from [releases].
2. Download the latest .jar from
   [ALICA_ACPack](https://github.com/LEB-EPFL/ALICA_ACPack/releases).
3. If using SASS as a ImageJ/Fiji plugin, place the SASS jar in
   `<IMAGEJ_ROOT>/plugins` and the ALICA_ACPack jar in
   `<IMAGEJ_ROOT>/jars`. Otherwise, place both jars in the same
   folder of your choosing.

**Note:** The SASS .jar file that is found in the releases is an
uber-jar, i.e. it contains all the dependencies that are necessary to
run.

### As a standalone application
 - Executing the .jar file by double-clicking on it launches a
   BeanShell console. Example scripts can be found in `/scripts/`
   folder.
 - Running SASS from command line: `java -jar <SASS-jar-name> --help`
   brings up available options, such as executing a script, or an
   interactive session within the terminal.
 
### As Fiji plugin
 - If launching Fiji from the command line, launch it from within the
   Fiji directory so that the ALICA_ACPack.jar is properly found.
 - `Plugins -> SASS -> Simulation` launches an interactive simulator
   (not all options are available through the GUI).
 - `Plugins -> SASS -> Command Prompt` launches BeanShell
   console. Generated images can be analyzed immediately with Fiji.

## Where to find help

- How to use SASS: https://gitter.im/leb_epfl/SASS
- Bug reports: https://github.com/LEB-EPFL/SASS/issues
- Feature requests: https://github.com/LEB-EPFL/SASS/issues
- Developer questions: https://gitter.im/leb_epfl/SASS

## Acknowledgements

SASS uses adapted code and algorithms from the following projects:
- [SOFItool](https://github.com/lob-epfl/sofitool) by Arik Girsault
  and Tomas Lukes (GPL)
- [ALICA](https://github.com/MStefko/ALICA) by Marcel Stefko (GPL)
- [MicroscPSF-ImageJ](https://github.com/hijizhou/MicroscPSF-ImageJ)
  by Jizhou Li (MIT)
- [OpenSimplexNoise](https://gist.github.com/KdotJPG/b1270127455a94ac5d19)
  by Kurt Spencer (Unlicense/Public Domain)
 
In addition, SASS relies on these projects to provide core
functionality.
- [Apache Thrift](https://thrift.apache.org/)
- [ImageJ](https://imagej.net/Welcome)
- [Fiji](https://fiji.sc/)

...and the many, many people behind *all* the software that we rely
on.

[Releases]: https://github.com/LEB-EPFL/SASS/releases
