package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetApiSimulation extends Simulation {

    private static final String BASE_URL =
            System.getProperty(
                    "baseUrl",
                    "http://localhost:8080"
            );

    private static final String API_PATH =
            System.getProperty(
                    "apiPath",
                    "/api/employees"
            );

    private final HttpProtocolBuilder httpProtocol =
            http
                    .baseUrl(BASE_URL)
                    .acceptHeader("application/json");

    private final ScenarioBuilder getEmployeeScenario =
            scenario("Get Employee API")
                    .exec(
                            http("GET Employee")
                                    .get(API_PATH)
                                    .check(
                                            status().is(200),
                                            jsonPath("$[0].id").exists()
                                    )
                    );

    {
        setUp(
                getEmployeeScenario.injectOpen(
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