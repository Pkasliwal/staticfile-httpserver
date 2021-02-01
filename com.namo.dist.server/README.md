Source code for Multi-threaded web server for serving static files using thread-pools in Java.
Provide standard HTTP functionality for:
1. Cache-headers
2. E-tags
3. Last-modified
4. Mime-type
5. Http-range

Uses chain of responsibility pattern and one can enhance the pipeline to include keep-alive response header functionality
too as needed

Some of the available static files that can be accessed (default port is 8500):

http://localhost:{{port}}/staticfiles/Serverless_comparison.pdf

http://localhost:{{port}}/staticfiles/light_green.jpg

http://localhost:{{port}}/staticfiles/solah01.mp3

http://localhost:{{port}}/staticfiles/simple-test.txt

Server will be dockerized and one could mount additional static files as needed on the docker run.