# Gatling GET API Maven Project

## Requirements
- JDK 17+
- IntelliJ IDEA
- Maven 3.9+ recommended

## Import into IntelliJ
1. Extract this ZIP.
2. IntelliJ IDEA -> File -> Open.
3. Select the extracted `gatling-get-api-maven` folder.
4. Open `pom.xml` as a Maven project.
5. Wait for Maven dependencies to download.

## Configure your API
The simulation defaults to:
- Base URL: http://localhost:8080
- API path: /api/employees

You can override them without editing Java:

Windows CMD:
mvn gatling:test -DbaseUrl=http://localhost:8080 -DapiPath=/api/employees

PowerShell:
mvn gatling:test "-DbaseUrl=http://localhost:8080" "-DapiPath=/api/employees"

## Run
From IntelliJ Terminal:
mvn clean test
mvn gatling:test

The test ramps up 100 users over 30 seconds.

## Report
After execution, open:
target/gatling/<latest-run>/index.html

## Notes
- Change `baseUrl` and `apiPath` for your API.
- If your API requires authentication, add the Authorization header in GetApiSimulation.java.
- The project uses Java DSL and the Gatling Maven plugin.
