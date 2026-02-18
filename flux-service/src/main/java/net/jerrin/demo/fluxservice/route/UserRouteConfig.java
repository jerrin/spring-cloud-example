package net.jerrin.demo.fluxservice.route;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.fluxservice.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class UserRouteConfig {

    private final UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions.route()
                .GET("/users", userHandler::getUsers)
                .build();
    }
}
