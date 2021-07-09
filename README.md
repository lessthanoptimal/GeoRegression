**Geometric Regression Library (GeoRegression)** is a free Java based geometry library for scientific computing in fields such as robotics and computer vision with a focus on 2D/3D space. The goal of GeoRegression is to provide all the core functions for estimating the closest point/distance between geometric primitives, estimating best-fit shapes, and estimating and applying geometric transforms. It is designed for high performance and ease of use. GeoRegression has been release under an Apache v2.0 license for both commercial and non-commercial use.

* **Webpage:** http://georegression.org
* **Source Code:** https://github.com/lessthanoptimal/GeoRegression
* **Precompiled Jars:** https://sourceforge.net/projects/georegression/
* **Bugs/Feature Requests:** https://github.com/lessthanoptimal/GeoRegression/issues

## Capabilities:

* Geometric primitives
  * Points, lines, vectors, ... etc 
* Dimensionality
  * Full support for 2-D/3-D space.
  * Minimal support for N-D space. 
* Metric measures
  * Distance, closest point, and intersections. 
* Transforms
  * Rigid body 2D/3D, homography, and affine.
* 3D Rotation
  * Euler Angles (all possible)
  * Rodrigues (axis-angle)
  * Quaternions
  * Convert between each aother and into 3x3 rotation matices
* Homogenous Coordinates
  * Implicit 2D z=1 and 3D w=1
* Best Fit
  * Shapes (e.g. lines, curves, ellipses, planes, spheres, cylinders, ... etc ).
  * Motion/Transform (e.g. affine, homography, special euclidean). 
* Support for both 32-bit and 64-bit floating point numbers.
  * Types: float and double.
  * Limited support for integers types. 

## Maven Central

GeoRegression is on [Maven Central](https://mvnrepository.com/artifact/org.georegression/georegression) and can
be added to your Gradle project as follows:

```Groovy
compile group: 'org.georegression', name: 'georegression', version: '0.24'
```

## Build Instructions

```bash
cd georegression
./gradlew autogenerate
./gradlew install
```

## Directory Structure:

```
src/              Project source code.
test/             Source code for unit tests
libs/             Contains jars of external dependencies
examples/         Directory containing code examples showing how to use this library.
experimental/     Code not yet ready for the main distribution but still might be useful
```

## Author

GeoRegression has been developed by Peter Abeles.