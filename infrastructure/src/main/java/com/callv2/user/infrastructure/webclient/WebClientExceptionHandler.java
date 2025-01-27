package com.callv2.user.infrastructure.webclient;

import java.util.function.Function;

import org.springframework.web.reactive.function.client.ClientResponse;

import reactor.core.publisher.Mono;

public class WebClientExceptionHandler {

    public static <B> Function<ClientResponse, Mono<? extends Throwable>> throwsException(
            final Class<B> bodyClazz, final Function<B, ? extends Throwable> exceptionFunction) {
        return res -> res
                .bodyToMono(bodyClazz)
                .flatMap(error -> Mono.error(exceptionFunction.apply(error)));
    }

}