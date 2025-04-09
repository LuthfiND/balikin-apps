package org.balikin;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.balikin.model.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        int status;
        String errorCode;

        if (e instanceof BadRequestException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
            errorCode = "ERR_BAD_REQUEST";
        } else if (e instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
            errorCode = "ERR_NOT_FOUND";
        } else if (e instanceof NotAcceptableException) {
            status = Response.Status.NOT_ACCEPTABLE.getStatusCode();
            errorCode = "ERR_NOT_ACCEPTABLE";
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            errorCode = "ERR_INTERNAL";
        }

        ErrorResponse error =  new ErrorResponse(
                status,
                e.getMessage(),
                errorCode,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}