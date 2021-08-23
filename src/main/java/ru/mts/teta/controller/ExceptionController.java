package ru.mts.teta.controller;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.mts.teta.exception.ImageFoundException;
import ru.mts.teta.exception.InternalServerError;
import ru.mts.teta.exception.NotFoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(InternalServerError.class)
    public ModelAndView internalServerErrorHandler(InternalServerError ex) {
        ModelAndView modelAndView = new ModelAndView("server_error");
        modelAndView.setStatus(ex.getStatusCode());
        return modelAndView;
    }

    @ExceptionHandler(ImageFoundException.class)
    public ResponseEntity<Void> avatarFoundExceptionHandler(ImageFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ModelAndView fileSizeLimitExceededException(FileSizeLimitExceededException ex) {
        ModelAndView modelAndView = new ModelAndView("server_error");
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }
}
