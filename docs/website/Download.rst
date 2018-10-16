Download 
========


Stable Release
--------------

The latest stable release is available on `Source Forge <https://sourceforge.net/projects/georegression/>`_ with direct links provided below for your convenience:

* :sourceforge:`Source Code <src>`
* :sourceforge:`Precompiled Jar <libs>`

Bleeding Edge
-------------

The latest source code is available from GitHub in the SNAPSHOT branch.  The latest stable code can be found in the 'master' branch.  Most of the time SNAPSHOT should compile without any problems.  Not familiar with GIT?  Learn about it here http://git-scm.com/

Git repository: https://github.com/lessthanoptimal/GeoRegression

::
   
  git clone git://github.com/lessthanoptimal/GeoRegression.git georegression


Maven Central
------------------------

To include the latest stable release in your Maven projects add the following dependency.

**Maven Central:**

.. parsed-literal::
     <dependency>
      <groupId>org.georegression</groupId>
      <artifactId>georegression</artifactId>
      <version>\ |geo_version|\ </version>
    </dependency>


Dependencies
------------

GeoRegression depends on `DDogleg <http://ddogleg.org>`_ and `EJML <http://ejml.org>`_.  It uses `JUnit <http://junit.org>`_ for unit testing.
