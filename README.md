ECF Lab
==============

ECF Lab is a platform independent app developed to simplify usage of the Evolutionary Computation Framework (ECF). Before ECF Lab, user had to manually write configuration files and run experiments from the console. Results were in the form of plain text with no graphical representation. A lot of mistakes were made during execution of experiments so the need for such an application was clear. ECF Lab supports parallel execution of numerous experiments and provides nice visualisation of computed results. It is also supposed to be version independent - when new algorithms, genotypes and parameters are added, app should work as well as before.

-------------------------------------------------------

Video demo

See how to run a simple experiment using ECF Lab. (CLICK TO SEE VIDEO)

[![Run experiment](https://img.youtube.com/vi/MPi82YXWcTU/0.jpg)](https://www.youtube.com/watch?v=MPi82YXWcTU&feature=youtu.be)


-------------------------------------------------------

Installation

1. Download and install ECF http://gp.zemris.fer.hr/ecf/
2. Download/clone this repo
3. Run 'gradlew.bat build' on Windows or './gradlew build' on UNIX
4. Open ECF_Lab/View/build/libs/ECFLab-1.0.jar

-------------------------------------------------------

About

This project was started at the Faculty of Electrical Engineering and Computing in Zagreb for the purpose of Software Design Project course and later for the BSc and Master's Thesis. Main task was to make portable desktop application which should run ECF written in C++. For that purpose, we decided to use Java. Java Swing is used for building UI and Gradle is used as a build tool.

-------------------------------------------------------

More info about ECF project: http://gp.zemris.fer.hr/ecf/
