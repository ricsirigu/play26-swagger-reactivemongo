# Play Framework 2.6.x with Swagger and ReactiveMongo
A simple TODO app built with [Play Framework 2.6](https://www.playframework.com/) [Swagger](https://github.com/swagger-api/swagger-play/tree/master/play-2.6/swagger-play2) and [ReactiveMongo](http://reactivemongo.org/)

### How to run
You need
* MongoDB Installed and running on your machine with a database ```example```. Tutorial [here](https://docs.mongodb.com/v3.2/tutorial/install-mongodb-on-ubuntu/)
* SBT (mandatory if you don't want to use an IDE)
* IntelliJ to import the project (optional)
* SBT plugin (optional)

Tho ways to run the project:
* Import the project, run SBT and type ```run```  to launch the server.
* cd into the project directory, run SBT and type ```run```  to launch the server.

Then open your favourite browser and go to

```localhost:9000/api-docs```

From the beautiful Swagger-UI interface you can perform all the CRUD operations you want

Enjoy.

Author: [Riccardo Sirigu](https://www.riccardosirigu.com/)
