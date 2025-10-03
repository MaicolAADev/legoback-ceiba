package sura.pruebalegoback.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.common.ex.BusinessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    public static <T> Mono<T> handle(Throwable e) {
        log.error(e.getMessage());
        if (e instanceof BusinessException be) {
            if (be.getCode().equals("PATIENT_NOT_FOUND")) {
                return Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        be.getMessage(),
                        be
                ));
            }
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    be.getMessage(),
                    be
            ));
        }
        return Mono.error(new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                e
        ));
    }
}