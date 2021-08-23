package ru.mts.teta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class InternalServerError extends HttpServerErrorException {
    public InternalServerError() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
