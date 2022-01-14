package demo;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

import org.glassfish.jersey.logging.LoggingFeature;
import java.util.logging.Level;
import java.util.logging.Logger;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "JAX-RS Jersey demo"
                )
)
@ApplicationPath("/demo")
public class AppConfig extends ResourceConfig {

    public AppConfig() {

        packages("demo.service");

        register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));

        register(OpenApiResource.class);

        System.out.println("app configured");

    }

}
