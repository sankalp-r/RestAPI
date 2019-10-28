package com.api.e2e.http.jwt;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.api.e2e.http.OauthToken;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        boolean status = false;
        try {
            String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
            token = token.substring("Bearer".length()).trim();
            status = OauthToken.Verify(token);
        } catch (Exception e) {
            request.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        if (!status) {
            request.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }

}
