Geometric Regression Library (GeoRegression) is a free Java based geometry library for scientific computing in fields such as robotics and computer vision with a focus on 2D/3D space. The goal of GeoRegression is to provide all the core functions for estimating the closest point/distance between geometric primitives, estimating best-fit shapes, and estimating and applying geometric transforms. It is designed for high performance and ease of use. GeoRegression has been release under an Apache v2.0 license for both commercial and non-commercial use.

Build Instructions: See install.txt
Webpage: http://georegression.org
Source Code: https://github.com/lessthanoptimal/GeoRegression
Bugs/Feature Requests: https://github.com/lessthanoptimal/GeoRegression/issues

Capabilities:
    Geometric primitives
        Points, lines, vectors, ... etc 
    Dimensionality
        Full support for 2-D/3-D space.
        Minimal support for N-D space. 
    Metric measures
        Distance, closest point, and points of intersection. 
    Transforms
        Rigid body 2D/3D, homography, and affine.
    Best Fit
        Shapes (e.g. best fit line from points).
        Motion/Transform (e.g. motion for a set of point pairs). 
    Support for both 32-bit and 64-bit floating point numbers.
        Types: float and double.
        Limited support for integers types. 

--------------------------------------------------------------------------
Directory Structure:

src/                     Project source code.
test/                    Source code for unit tests
libs/                    Contains jars of external dependencies
examples/                Directory containing code examples showing how to use this library.
experimental/            Code not yet ready for the main distribution but still might be useful

---------------------------------------------------------------------------

GeoRegression has been developed by Peter Abeles.