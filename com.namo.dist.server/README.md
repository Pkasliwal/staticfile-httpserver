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

I have dockerized the server and is available on docker hub with image name: pkasliwal/com.namo.dist.server.staticfileserver:1.0

	Command to run the server:
docker run -p HOST_PORT:8500 pkasliwal/com.namo.dist.server.staticfileserver:1.0

Above server has some static example files in-built and can be accessed using following urls:

http://localhost:{{HOST_PORT}}/staticfiles/Serverless_comparison.pdf

http://localhost:{{HOST_PORT}}/staticfiles/light_green.jpg

http://localhost:{{HOST_PORT}}/staticfiles/solah01.mp3

http://localhost:{{HOST_PORT}}/staticfiles/simple-test.txt

	Command to mount other files:
docker run -p HOST_PORT:8500 -v ABSOLUTE_FILES_DIR:/usr/src/myserver/files pkasliwal/com.namo.dist.server.staticfileserver:1.0

	Postman collection for testing
Post collection available in tests/postman folder.
Test collection and env files will need to be imported into Postman desktop app and change the port env variable to HOST_PORT used in the server deployment.
Default port value defined is 8500 which will work out of the box.