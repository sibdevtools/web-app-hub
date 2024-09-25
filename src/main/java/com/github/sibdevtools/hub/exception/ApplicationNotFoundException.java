package com.github.sibdevtools.hub.exception;

import com.github.sibdevtools.error.exception.ServiceException;
import com.github.sibdevtools.hub.constant.Constant;
import jakarta.annotation.Nonnull;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class ApplicationNotFoundException extends ServiceException {
    public ApplicationNotFoundException(@Nonnull String systemMessage) {
        super(Constant.ERROR_SOURCE, "APPLICATION_NOT_FOUND", systemMessage);
    }
}
