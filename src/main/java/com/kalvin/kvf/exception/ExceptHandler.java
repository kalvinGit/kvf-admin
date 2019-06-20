package com.kalvin.kvf.exception;

import com.kalvin.kvf.common.constant.Constants;
import com.kalvin.kvf.dto.R;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class ExceptHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptHandler.class);

    @ExceptionHandler(KvfException.class)
    public R handleKvfException(KvfException e) {
        LOGGER.error("kvf异常：", e);
        if (e.getErrorCode() == null) {
            return R.fail(e.getMsg());
        }
        return R.fail(e.getErrorCode(), e.getMsg());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public R handleUnauthorizedExceptionException(UnauthorizedException e) {
        LOGGER.error("kvf异常：{}", e.getMessage());
        return R.fail(Constants.OTHER_FAIL_CODE, "权限不足，请联系管理员。");
    }
}
