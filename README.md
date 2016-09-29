Rectangles
==========

##Quick Start

Rectangles is a Java application.  Follow the following directions to get started
quickly.

###Prerequisites

The following prerequisites are required for building and running this application.

*   git client
*   Java JRE 8+
*   Docker / Docker Machine (if you wish to build and invoke in Docker)

###Build

Clone the repository to your local machine.

```
git clone https://github.com/2BoldlyCode/rectangles.git
```



##Overview

Rectangles analyzes a set of rectangles and determines the following properties:

*   intersection - rectangles share points
*   containment - is a special case of intersection in which the points of the
    contained rectangle are a subset of the points of the containing rectangle
*   adjacency - is a special case of intersection in which two rectangles share
    edge points but do not have overlapping areas

The preceding definitions are a quick summary of the analysis capabilities but
will not be sufficient to determine valid inputs or results.  A broader definition
along with examples of each rule will be described in subsequent sections.

##Skills Demonstration

The purpose of this exercise is to demonstrate software architecture and
development skills needed to build, test, and deploy a software system.

To that extent, this solution is intended to demonstrate the following concepts:

###Problem Definition

The originally stated problem did not adequately specify the geometrical definitions
of intersection, containment, and adjacency.  I have elaborated upon the original
definitions and examples to provide a more complete specification and test set.

The examples for each property are also modeled as unit tests to validate the library.

###Language Proficiency

I've chosen Java for the solution implementation.  I recognize other choices such as
C/C++ would provide better performance in carrying out lots of geometrical calculations.
However, I chose to demonstrate the object-oriented and modular features of Java as well
as demonstrate the use of Spring and other libraries.

I also have built my own classes rather than use the prebuilt Java2D or Canvas
objects (although in real-world scenarios, I prefer not re-inventing the wheel
if not necessary while striking a balance with a reasonable number of dependencies).

Finally, I demonstrate understanding of Java datatypes and check for data overflow
on calculations of width, height, and area.  Technically, area is not required and
if we didn't calculate it, we could analyze larger rectangles; however, for the
purposes of this exercise, we will constrain the area of a rectangle to be less
than or equal to Long.MAX_VALUE.

Using non- register-constrained data types such as BigInteger is not efficient and
we are not creating rectangles on the scale of star systems.

I have a couple of BASH scripts demonstrating build or launch tools.

###Build Automation and Dependency Management

I have chosen Maven to model my project's build and dependencies.  Gradle would have been a
good option too; however, I have considerably more experience with Maven and it is
adequate to accomplish my goals.

The Maven model is responsible for building and packaging my solution.  I will also use
it to configure optional packaging solutions such as Docker.

###

##Definitions

The algorithms for determining intersection, containment, and adjacency
are based upon the following definitions.  For all calculations, the following
is assumed:

*   all values are given as integers (no floating point)
*   all rectangles are on located on the same cartesian plane
*   all rectangles are axis-aligned (line segment slopes are either 0 or undefined)
*   the plane's extent and rectangle properties are constrained by the minimum and
    maximum values of the Java Long datatype.  That is,
    *   x is in the inclusive range \[`Long.MIN_VALUE .. Long.MAX_VALUE` \]
    *   y is in the inclusive range \[ `Long.MIN_VALUE .. Long.MAX_VALUE` \]
    *   the width, height, and area of a rectangle must be a positive integer
        less than or equal to Long.MAX_VALUE

Examples will be given by considering a cartesian plane with coplanar points having
coordinates `(x y)`.  Rectangles will be represented using a modified Well Known
Text (WKT) format (polygon of two points).  Rectangles are defined in terms of
two non-collinear points, `(x1 y1, x2 y2)`.  A rectangle with this definition
will consist of the line segments:

`(x1 y1, x1 y2)`, `(x1 y2, x2 y2)`, `(x2 y2, x2 y1)`, `(x2 y1, x1 y1)`

###Intersection

A rectangle intersects another rectangle if any of its points are shared with
the other rectangle.

Intersection examples:

*   `(0 0, 5 5)` and `(0 0, 5 5)` -- identity
*   `(0 0, 5 5)` and `(1 1, 4 4)` -- containment, no shared edges
*   `(0 0, 5 5)` and `(1 1, 6 6)` -- overlapping corner areas
*   `(0 0, 5 5)` and `(1 1, 6 4)` -- overlapping side area
*   `(0 0, 5 5)` and `(5 0, 10 5)` -- adjacent, shared edge
*   `(0 0, 1 1)` and `(1 1, 2 2)` -- adjacent, shared corner

Non-intersection examples:

*   `(0 0, 2 2)` and `(3 3, 5 5)` -- separated

###Containment

A rectangle contains another rectangle if and only if the points of the contained rectangle
are a subset of the points of the containing rectangle.  Two rectangles with the exact
same dimensions also contain each other.

Containment examples:
*   `(0 0, 1 1)` and `(0 0, 1 1)` -- identity
*   `(0 0, 5 5)` and `(1 1, 4 4)` -- containment, no shared edges

Non-containment examples:
*   `(0 0, 5 5)` and `(1 1, 6 4)` -- overlapping side area
*   `(0 0, 5 5)` and `(5 0, 10 5)` -- adjacent, non-overlapping
*   `(0 0, 2 2)` and `(3 3, 5 5)` -- separated

###Adjacency

A rectangle is adjacent to another rectangle if and only if the two rectangles
share edge points but do not have overlapping areas.

Adjacency examples:
*   `(0 0, 1 1)` and `(1 1, 2 2)` -- shared corner point
*   `(0 0, 5 5)` and `(5 0, 10 5)` -- adjacent, full side
*   `(0 1, 5 4)` and `(5 0, 10 5)` -- adjacent, partial side

Non-adjacency examples:
*   `(0 0, 5 5)` and `(0 1, 5 4)` -- overlapping side area
*   `(0 0, 5 5)` and `(1 1, 5 4)` -- containment, shared edge
*   `(0 0, 5 5)` and `(6 0, 11 5)` -- separated
