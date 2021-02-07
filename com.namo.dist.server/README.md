# Simple http static file server

Source code for Multi-threaded web server for serving static files using thread-pools in Java.
Provide standard HTTP functionality for:
1. Cache-headers
2. E-tags
3. Last-modified
4. Mime-type
5. Http-range

Uses chain of responsibility pattern and one can enhance the pipeline to include keep-alive response header functionality
too as needed

### Build and execution steps:
	
Project is built using jdk15 and maven3.6.3, though one should be able to use jdk version>=11 and maven version 3.x

Once project is cloned from here: https://github.com/Pkasliwal/staticfile-httpserver, cd into com.namo.dist.server and run the following steps:

1] Build jar (jar will be generated in target directory with this command): 

	mvn package
2] Execute tests: 

	mvn test
3] Execute a specific test:
 
	mvn test -Dtest=<TEST_CLASS_NAME>
4] Clean, Build, Deploy, Test, for all: 

	mvn clean install
5] Run/Execute the server on port 8500: 

	mvn exec:java
6] Run/Execute the server using jar file available in target directory (Xmx configuration can be changed as needed)

	java -XshowSettings:vm -Xmx1024m -jar target\com.namo.dist.server-2.0.0.jar
7] 	One can build docker image as well as follows (given docker is installed in the environment)

	docker build -t IMAGE_NAME:VERSION .
IMAGE_NAME and VERSION to be used for further commands: pkasliwal/com.namo.dist.server.staticfileserver:2.0.0

8]	Docker image execution:
I have dockerized the server and is available on docker hub with image name: pkasliwal/com.namo.dist.server.staticfileserver:2.0.0
Command to run the server (-m parameter is optional to configure memory allocation for the server container):

	docker run -m3GB -p HOST_PORT:8500 pkasliwal/com.namo.dist.server.staticfileserver:2.0.0

Above server has some static example files in-built and can be accessed using following urls:

http://localhost:{{HOST_PORT}}/staticfiles/Serverless_comparison.pdf

http://localhost:{{HOST_PORT}}/staticfiles/light_green.jpg

http://localhost:{{HOST_PORT}}/staticfiles/solah01.mp3

http://localhost:{{HOST_PORT}}/staticfiles/simple-test.txt


9] Command to mount other files:

	docker run -p HOST_PORT:8500 -v ABSOLUTE_FILES_DIR:/usr/src/myserver/files pkasliwal/com.namo.dist.server.staticfileserver:2.0.0

### Postman collection for testing
Post collection available in tests/postman folder.

Test collection and env files will need to be imported into Postman desktop app and change the port env variable to HOST_PORT used in the server deployment.
Default port value defined is 8500 which will work out of the box.


### Project structure
	1] files directory contains testfiles of different types
	2] src directory has java code
	3] tests directory has postman test files
	4] Rest of the files: 
		Dockerfile for dockerization
		README