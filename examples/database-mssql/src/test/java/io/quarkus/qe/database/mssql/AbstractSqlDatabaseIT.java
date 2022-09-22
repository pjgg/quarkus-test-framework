package io.quarkus.qe.database.mssql;

import static org.hamcrest.Matchers.hasSize;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.bootstrap.SqlServerService;
import io.quarkus.test.services.MssqlContainer;
import io.quarkus.test.services.QuarkusApplication;

public abstract class AbstractSqlDatabaseIT {

    @MssqlContainer
    //@Container(image = "${mssql.image}", port = 1433, expectedLog = "Service Broker manager has started")
    public static SqlServerService database = new SqlServerService();

    @QuarkusApplication
    static RestService app = new RestService()
            .withProperty("quarkus.datasource.username", database.getUser())
            .withProperty("quarkus.datasource.password", database.getPassword())
            .withProperty("quarkus.datasource.jdbc.url", database::getJdbcUrl);

    @Test
    @Order(1)
    public void getAll() {
        app.given()
                .get("/book")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("", hasSize(7));
    }
}
