Documentation
=====================

Usage documentation is primarily provided in the form of examples and JavaDoc. To use GeoRegression simply add its jars to your class path like you would with any other library.  Jars can be obtained from source code or from the :doc:`/Download` page. Gradle scripts are provided for compiling the source code.


JavaDoc
-----------

Full documentation of the API is contained in the `JavaDoc <http://georegression.org/javadoc>`_

Example Code
------------

Several examples are included with the source code and can be found in the :gitexample:`examples <>` directory. For your convenience several examples have been added to this website, see links below. To view the latest examples, browse the GIT repository.
List of Example Code:

* :doc:`/RotationParameterizations`
* :doc:`/MetricLines`
* :doc:`/TransformFitting`
* :doc:`/TransformSequence`

Building the Library
--------------------

GeoRegression is easy to compile using the provided Gradle script.  In Linux type the following commands:

Gradle::

  $ cd georegression/
  $ ./gradlew createLibraryDirectory
  :compileJava
  :processResources UP-TO-DATE
  :classes
  :jar
  :sourcesJar UP-TO-DATE
  :createLibraryDirectory

  BUILD SUCCESSFUL

  Total time: 7.238 secs

Then look in the "georegression/libraries" directory for compiled jar and source
