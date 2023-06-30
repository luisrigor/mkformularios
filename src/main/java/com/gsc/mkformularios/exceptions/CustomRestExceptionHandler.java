package com.gsc.mkformularios.exceptions;


import com.gsc.mkformularios.constants.ApiErrorConstants;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Log4j
@ControllerAdvice
public class CustomRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> getPvmException(GetPVMException ex, WebRequest request) {
        log.error(ex.getMessage());
        ApiError apiError = new ApiError(ApiErrorConstants.ERROR_PROCESSING_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> getPvmException(CreatePVMException ex, WebRequest request) {
        log.error(ex.getMessage());
        ApiError apiError = new ApiError(ApiErrorConstants.ERROR_PROCESSING_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
