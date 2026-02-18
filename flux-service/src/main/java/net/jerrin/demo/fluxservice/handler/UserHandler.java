package net.jerrin.demo.fluxservice.handler;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.fluxservice.dao.UserDao;
import net.jerrin.demo.fluxservice.model.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserHandler {

    private final UserDao userDao;

    public Mono<ServerResponse> getUsers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userDao.getUsers(), User.class);
    }
}
