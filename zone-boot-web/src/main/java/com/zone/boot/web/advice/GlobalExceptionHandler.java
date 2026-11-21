package com.zone.boot.web.advice;

import com.zone.boot.common.enums.BusinessCodeEnum;
import com.zone.boot.common.exception.BusinessException;
import com.zone.boot.domain.api.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler<T> {

    @ExceptionHandler(BusinessException.class)
    public ApiResult<T> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return ApiResult.error(e);
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<T> handleGeneralException(Exception e, HttpServletRequest request) {
        return ApiResult.error(BusinessCodeEnum.SYSTEM_ERROR);
    }
}