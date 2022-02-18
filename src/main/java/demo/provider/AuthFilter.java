package demo.provider;
import demo.Config;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Base64;

public class AuthFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws WebApplicationException {

        if (requestContext.getUriInfo().getPath().endsWith("openapi.json")) return;

        Method method = resourceInfo.getResourceMethod();

        if( !method.isAnnotationPresent(PermitAll.class)) {

            if(method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Access blocked for all users !!").build());
                return;
            }

            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            final List<String> authorization = headers.get("Authorization");

            if(authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("You cannot access this resource").build());
                return;
            }

            String base64Credentials = authorization.get(0).substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            // credentials = username:password
            final String[] values = credentials.split(":", 2);

            if(method.isAnnotationPresent(RolesAllowed.class)) {
                //RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);

                if( !isUserAllowed(values[0], values[1])) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("You cannot access this resource").build());
                }
            }
        }
    }

    private boolean isUserAllowed(final String username, final String password) {

        return username.equals(Config.USER) && password.equals(Config.PASSWORD);
    }
}
