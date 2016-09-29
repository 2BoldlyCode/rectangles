Rectangles
==========

##Quick Start

Rectangles is a Java application.  Follow the following directions to get started
quickly.

###Prerequisites

The following prerequisites are required for building and running this application.

*   git client
*   Java JRE 8+
*   Docker Toolbox

###Docker

Both a command line client and REST service has been pushed to quay.io as docker images.

If you have Docker setup on your machine, you can run the example data by executing
the following command:

```
./rectangles -d < example/testdata
```

Dropping the `-d` will compile the source locally and run it (make sure you have the requisites).

This will download the CLI image and run it interactively.

Similarly, to launch the REST web service, make sure you have docker-compose installed and
on your path.  From the project base directory:

```
cd docker-compose
docker-compose up
```

To find out where the server started up, you can use the following snippet to discover
the docker host IP.

```
echo $DOCKER_HOST | n="[0-9]\{1,3\}" sed "s/.*\/\/\($n\.$n\.$n\.$n\).*/\1/"
```

Using that IP, we can query the server using cURL

```
curl http://$HOST_IP:3000/analyze/0,0,5,5/1,1,4,4
```

In this example, I passed two rectangles to be analyzed `(0 0, 5 5)` and `(1 1, 4 4)`

Read on to learn more about how we represent rectangle geometry and such.

###Build

Clone the repository to your local machine.

```
git clone https://github.com/2BoldlyCode/rectangles.git
```

The run script automatically downloads maven and builds the source, however, should
something go wrong and you need to build it manually, execute the following from the
project root directory:

On OSX / Linux

```
./mvnw install
```

On Windows

```
./mvnw.cmd install
```

This will download all dependencies and build.  Follow the directions in the wrapper
error messages if you are missing a Java JDK or do not have your path setup correctly.

To start up the script locally, use the spring-boot maven plugin as follows (substitute)
the appropriate maven command (mvnw.cmd) for windows.

```
cd rectangles-cli
../mvnw spring-boot:run < ../example/testdata
```

Finally, to build your own docker image, you can use the `build-docker.sh` script.
I have only tested this on OSX.  From the project root, execute:

```
./build-docker.sh
```

By default this builds both the CLI and REST server.  Just pass an argument such as
`cli` or `rest` to only build that component.

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

I have a couple of BASH scripts demonstrating build and launch tools.

###Build Automation and Dependency Management

I have chosen Maven to model my project's build and dependencies.  Gradle would have been a
good option too; however, I have considerably more experience with Maven and it is
adequate to accomplish my goals.

The Maven model is responsible for building and packaging my solution.  I will also use
it to configure optional packaging solutions such as Docker.

###REST Service

Using SpringBoot and SpringMVC I have created a very simple REST wrapper around the
core analysis code that can provide answers in JSON.  It accepts a very simple formula
for requesting analysis.

```
http://localhost:3000/analyze/x1,y1,x2,y2/x1,y1,x2,y2
```

###Use Git and Release Management

I used Git to version my source and GitHub to publish it.  Further more, released
versions have been pushed as ready-to-run docker images on quay.io.  The source
incorporates the version of the software for informational purposes by scraping
the local git repository properties.

###OSX / Linux Focus

The java app will run on Windows; however, the vast majority of my experience is on
Linux/OSX.   Please try to run the app manually using the compiled jar (a manifest exists
for the entry point) if you have any problems on the Windows box.

```
java -jar rectangles/rectangles-cli/target/rectangles-cli-0.0.1-SNAPSHOT.jar < example/testdata
```

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
