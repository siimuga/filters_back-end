# Filters back-end application

A Spring Boot application based on Java 17 that provides REST interfaces for filters monitoring and processing. The project uses Gradle for build and dependency management.

## Technologies
* Java 17
* Spring Boot
* Gradle
* REST API
* JUnit (for testing)

## Running the Application
**Start the development environment:**

On Unix/macOS:
*./gradlew bootRun*

On Windows:
*gradlew bootRun*

The API will be available at:
http://localhost:8080

## Testing
The tests are located in the directory:
*src/test/java/com/example/filters_back_end*

**To run the tests:**

On Unix/macOS:
*./gradlew test*

On Windows:
*gradlew test*

## Build and Package
**To create a build:**

On Unix/macOS:
*./gradlew build*

On Windows:
*gradlew build*

The resulting .jar file will be created in the *build/libs* directory.