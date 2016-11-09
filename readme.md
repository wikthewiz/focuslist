Modules used

Eclipse Mars
============
jetty-all-9.3.8-uber.jar
google cloud messaging 
Jetty
=====
See: http://www.eclipse.org/jetty/documentation/current/advanced-embedding.html

To see all jetty versions:  http://central.maven.org/maven2/org/eclipse/jetty/aggregate/jetty-all/

To download:
>curl -o d:\src\server_code\lib\jetty-all-9.3.13.jar http://central.maven.org/maven2/org/eclipse/jetty/aggregate/jetty-all/9.3.13.v20161014/jetty-all-9.3.13.v20161014-uber.ja

Dependencies
============
To make rest client work you need all there dependencies:
hk2
javassist
javax
jersey
validation

Jersey
======
current jersey version: 2.24

Jersey have ton of different dependencies which also intersect with jetty. I have downloaded jersey 2.22.2 from here: https://jersey.java.net/download.html

from the external dependecies I have removed the following jars which is present in jetty:
javax-servlet-api
javax-annotation-api

Howto create dist
=================
When updating the build path or adding libraries a new build.xml will have to be created. This is done by:

1. Right-click on an Eclipse project
2. Click "Export" then 
3. Under "General" choose  "Ant build files".

Now you can run:
> ant clean build

Now you have everything in bin folder.
    rmdir dist\lib\ /q /s
    xcopy lib\* dist\lib\ /S /Y

Now copy the resources:
    rmdir dist\resources\ /q /s
    xcopy bin\resources dist\resources\ /S /Y

Create the jar file:
    jar cmf Manifest.txt server_code.jar -C bin\ wikthewiz
    xcopy server_code.jar dist\ /Y
    rm server_code.jar

