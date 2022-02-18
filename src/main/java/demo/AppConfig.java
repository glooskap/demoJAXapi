package demo;

import demo.provider.AuthFilter;
import demo.provider.CORSFilter;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import jakarta.ws.rs.ApplicationPath;
import java.util.logging.Level;
import java.util.logging.Logger;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "JAX-RS demo",
                        description = "A JAX-RS running on a Jersey server.\n"
                        + "Perform CRUD operations on a quotes database."
                        , version = "1.0"
                )
)
@ApplicationPath("/demo")
public class AppConfig extends ResourceConfig {

    public AppConfig() {

        packages("demo.service");

        register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));

        register(new CORSFilter());

        register(new AuthFilter());

        register(OpenApiResource.class);

    }

}
