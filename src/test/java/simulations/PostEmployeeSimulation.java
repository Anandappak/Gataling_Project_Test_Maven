package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PostEmployeeSimulation extends Simulation {

    private static final String BASE_URL =
            System.getProperty(
                    "baseUrl",
                    "http://localhost:8080"
            );

    private static final String API_PATH =
            System.getProperty(
                    "apiPath",
                    "/api/v1/employees"
            );

    private final HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl(BASE_URL)
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json");

    private final ScenarioBuilder createEmployeeScenario =
            scenario("Create Employee API")

                    .exec(
                            http("POST Create Employee")
                                    .post(API_PATH)

                                    .body(
                                            StringBody(session -> {

                                                String uniqueId =
                                                        UUID.randomUUID().toString();

                                                return """
                                                        {
                                                          "name": "Employee %s",
                                                          "email": "employee.%s@gmail.com",
                                                          "department": "IT",
                                                          "salary": 85000
                                                        }
                                                        """.formatted(
                                                        uniqueId,
                                                        uniqueId
                                                );
                                            })
                                    )

                                    .check(
                                            status().is(201),

                                            jsonPath("$.id")
                                                    .exists(),

                                            jsonPath("$.name")
                                                    .exists(),

                                            jsonPath("$.email")
                                                    .exists(),

                                            jsonPath("$.department")
                                                    .is("IT")
                                    )
                    );

    {
        setUp(
                createEmployeeScenario.injectOpen(
                        rampUsers(100).during(60),
                        constantUsersPerSec(20).during(120)
                )
        )
        .protocols(httpProtocol)

        .assertions(

                global()
                        .failedRequests()
                        .percent()
                        .lt(5.0),

                global()
                        .responseTime()
                        .percentile3()
                        .lt(1000)
        );
    }
}