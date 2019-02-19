# REST API to support the tests

## Using the uploaded image

You can use the image at normandesjr/rest-api-for-test

## Changing the image

If you make any updates in script, you should build the image with the command below:

```
docker build -t normandesjr/rest-api-for-test .
````

To run the container, use the command below:

```
docker run -p 5000:5000 -it --rm --name running-python-rest-app normandesjr/rest-api-for-test
```

Pushing the image to the repository:

```
docker push normandesjr/rest-api-for-test
```

The server will be available at 5000 port.

* http://localhost:5000/users/bruno -> It takes 10 seconds to respond a 200 OK
* http://localhost:5000/users/pedro -> 200 OK with a JSON
* http://localhost:5000/users/ricardo -> 500 Internal Server Error

