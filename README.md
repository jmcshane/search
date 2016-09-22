# James McShane Search Implementation

[![Build Status](https://travis-ci.org/jmcshane/search.svg?branch=master)](https://travis-ci.org/jmcshane/search)

This project carries out three search mechanisms:

* Full text document search
* Regular expression document search
* Search an index containing preprocessed content

## Building the Code

In order to build the project, run

	./gradlew build

## Running the Code

The spring boot jar that is built is within the `boot` submodule.  From the root directory, this can be run using the following:

	java -jar boot/build/libs/boot.jar

The default port is 9999.

Each search type is available at the url `/search/<type>` with possible values `index`,`text`, and `regex`.  Pass a query parameter named `input` for the search term and send an HTTP GET request to get the search results.

The default home directory for the search is the folder `boot/src/main/resoures/searchDir`.  This can be changed to a directory local to the application by sending an HTTP POST request to the URL `/setHomeDirectory` with form parameter `path`.

## Code Organization

The high-level organization of the code is done using gradle subprojects.  There are three modules under search, each implementing the `SearchStrategy` class present in the `api` module.  The `boot` module starts up the app and makes HTTP GET endpoints available for each search mechanism.  The web endpoints choose the appropriate `SearchStrategy` implementation and forward requests to the submodule.

Though each module individually is small, the module separation allows very focused configuration of the `build.gradle` files.  The mongo libraries are targeted specifically in the `index` library, for example.

## Libraries

There are a few main library selections in this project:

* Spring Boot
* MongoDB/Morphia
* Spock Framework

The Spring Boot set of libraries allow for very simple configuration of Spring based applications.  Though the Spring Boot libraries are default quite opinionated, I find their opinions to often be close to my desires and modifying the configuration is quite simple.

MongoDB and Morphia is used to implement a pre-processed index search.  Morphia was selected because of how well it aligned with the data structure that the search algorithm used to organize the processed text.  Morphia's speed has improved dramatically over the last few release cycles while providing enough abstraction to write Java objects directly to MongoDB.  The `UpdateOperations` proved to be invaluable when indexing new files.  The Mongo collection was written in a way to make querying for word matches as simple as possible, while paying the price in update/delete performance.  This was chosen to be an adequate tradeoff given the interaction patterns that a search application will usually have.

The Spock Framework is used for testing given its declarative structure and alignment with overall project structure.  Spock creates very simple data driven testing mechanisms with the `where:` instruction and an intuitive flow to creating the unit tests.  This framework was used for all unit tests, including the `@SpringBootTest` class which tests the Spring applicationContext startup.

## Performance

Performance evaluation was done using the JunitBenchmark library and initializing the Spring Boot jar that is repackaged by the gradle Spring Boot plugin.  Given three basic files, a performance evaluation was done against the three search types that was not conclusive.  With such a small amount of input, the difference between doing File IO on the three small text files and a query against MongoDB was minimal.

![First Performance Test](initialRun.PNG)

Then, a plain text version of the bible was added and a second performance test was run using just 20 queries.  This test was able to distinguish between the two search strategies very easily, as seen by the graph of results.

![Second Performance Test](largeFile20runs.png)

Finally, the number of random inputs was increased to 20000 and run with 100 concurrent users.  The distinction between the types of searches is clearer now with the concurrent requests.

![Third Performance Test](20kconcurrent-itest-2.PNG)