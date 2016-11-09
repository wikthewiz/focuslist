This is a server to store a list. This was done just as project to test out jetty. I actually use it and it can be found here: wikthewiz.com

## Modules

## Jetty
See: http://www.eclipse.org/jetty/documentation/current/advanced-embedding.html

To see all jetty versions:  http://central.maven.org/maven2/org/eclipse/jetty/aggregate/jetty-all/

To download:
`>curl -o d:\src\server_code\lib\jetty-all-9.3.13.jar http://central.maven.org/maven2/org/eclipse/jetty/aggregate/jetty-all/9.3.13.v20161014/jetty-all-9.3.13.v20161014-uber.jar`

## Dependencies
To make rest client work you need all there dependencies:
 * hk2
 * javassist
 * javax
 * jersey
 * validation

## Jersey
current jersey version: 2.24

Jersey have ton of different dependencies which also intersect with jetty. I have downloaded jersey 2.22.2 from here: https://jersey.java.net/download.html

from the external dependecies I have removed the following jars which is present in jetty:
 * javax-servlet-api
 * javax-annotation-api

## Howto create dist
**Mote** When updating the build path or adding libraries you will have to update the Manifest.txt file.

1. In eclipse start by running clean
2. Now you have everything in bin folder:
    1. `> rmdir dist\lib\ /q /s`
    2. `> xcopy lib\* dist\lib\ /S /Y`

3. Now copy the resources:
    1. `> rmdir dist\resources\ /q /s`
    2. `> xcopy bin\resources dist\resources\ /S /Y`

4. Create the jar file:
    1. `> jar cmf Manifest.txt server_code.jar -C bin\ wikthewiz`
    2. `> xcopy server_code.jar dist\ /Y`
    3. `> rm server_code.jar`

