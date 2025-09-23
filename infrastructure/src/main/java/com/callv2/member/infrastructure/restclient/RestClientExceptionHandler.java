package com.callv2.member.infrastructure.restclient;

import java.util.function.Function;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import com.callv2.member.infrastructure.configuration.mapper.Mapper;

public final class RestClientExceptionHandler {

    public static <B> void throwsException(
            final Class<B> bodyClazz,
            final HttpRequest request,
            final ClientHttpResponse response,
            final Function<B, ? extends Throwable> exceptionFunction) {
        try {
            final B body = Mapper.mapper().readValue(response.getBody(), bodyClazz);
            throw exceptionFunction.apply(body);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}