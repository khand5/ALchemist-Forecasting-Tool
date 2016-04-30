# Alchemist
----

Developed by D. Khan, H. Khan, M. Bitzos, J. Wang. This Readme document was compiled on April 13, 2016.

#### Technology Stack
Built on Server-Client Architecture.

###### Server

Built with Java, using the following libraries:

* GSON (used to serialize Java objects to JSON)
* Spark (used to expose a REST API which can be consumed using HTTP requests, and all replies are returned in JSON)
* OpenForecast (used to predict data projections given previous data)
* JUnit Framework for testing purposes


###### Client

Built on the Ionic Framework, using the following libraries:

* Highcharts.js (to draw charts)
* Highmaps.js (to draw maps)
* jQuery (dependency of Highcharts/highmaps)


#### Requirements
Please make sure you have the following requirements.

1. Java 1.8 (https://java.com/en/download/)
2. Node.js (https://nodejs.org/en/)
3. Eclipse *with Gradle support* (Can be obtained by searching for "Buildship Gradle" in the eclipse marketplace)

Download and install them as per the instructions on their website.

#### Getting Started

###### The Client
1. *Install ionic via npm*. In order to test the app locally, you need to install the ionic framework. This can be done easily using Node's package manager `npm`.

    ```
    npm install -g ionic
    ```

2. *Serve the app locally via ionic*. Simply go to the client's directory and:

    ```
    cd ./alchemist/client/
    ionic serve
    ```

3. *Open the client in your browser*. If your default browser does not open up automatically, try opening it manually and visiting to http://localhost:8100/ (by default, ionic uses port 8100, but your installation may differ).

###### The Server
1. *Import the server as a Gradle Project.* Open up eclipse, and click `File > Import...`. When the dialog box appears, search for `Gradle` and click Next after selecting "Gradle Project". Follow the instructions on the dialogue, (select `./alchemist/server/` as the project root directory).

2. *Run AlchemistController*. After the project is done importing and the dependencies are downloaded, open up AlchemistController.java and click `Run > Run As > Java Application`.

###### Example Searches
In case you're getting no results, try these **case-sensitive** searches:

| Column           | Search                 |
| :--------------- | :--------------------- |
| Field of Research| Mechanical engineering |
| Organization     | McMaster University    |
| Province         | Quebec                 |
| Professor*       | "Zucker Jeffery"       |
| Subject          | Communications systems |
| Year             | 2003                   |
| Amount           | 10000                  |

*The quotations matter when searching for professor names.
