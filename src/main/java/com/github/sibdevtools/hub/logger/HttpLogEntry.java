package com.github.sibdevtools.hub.logger;

import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.10
 */
@Builder
public record HttpLogEntry(
        RequestDirection direction,
        String rqUID,
        String method,
        String uri,
        long timing,
        int status,
        Map<String, List<String>> rqHeaders,
        String rq,
        Map<String, List<String>> rsHeaders,
        String rs) {
}
