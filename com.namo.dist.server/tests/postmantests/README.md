Execution of postman test

One can import the 2 files available as part of this folder on postman desktop application or web browser
Then import collection file as Postman collection and env as Postman environment.

Run the imported collection using provided env, port defined on the environment by default is 8500.
If server is made to run on a different port then default, one can change postman env variable "port" from
postman app to the defined port.

Running following Happy path tests as part of the collection, one can run them on the browser too:
Default port used by server: 8500

http://localhost:{{port}}/staticfiles/Serverless_comparison.pdf

http://localhost:{{port}}/staticfiles/light_green.jpg

http://localhost:{{port}}/staticfiles/solah01.mp3

http://localhost:{{port}}/staticfiles/simple-test.txt


TODO:

Need to add Negative test scenarios

Need to test content range and other http header scenarios

Need to perform load testing with simultaneous requests