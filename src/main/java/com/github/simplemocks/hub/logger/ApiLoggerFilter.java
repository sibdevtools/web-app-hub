package com.github.simplemocks.hub.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * @author sibmaks
 * @since 0.0.6
 */
@Slf4j
public class ApiLoggerFilter extends HttpFilter {
    private static final Set<MediaType> FULL_PRINT_CONTENT_TYPE = Set.of(
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_HTML,
            MediaType.TEXT_XML,
            MediaType.TEXT_MARKDOWN,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML
    );

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var rqUID = UUID.randomUUID().toString();
        var method = request.getMethod();
        var requestURI = request.getRequestURI();

        var requestWrapper = new ContentCachingRequestWrapper(request);
        var responseWrapper = new ContentCachingResponseWrapper(response);

        var startTime = System.currentTimeMillis();
        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            var timing = System.currentTimeMillis() - startTime;
            var status = responseWrapper.getStatus();
            var rqHeaders = getHeaders(requestWrapper);
            var rqType = requestWrapper.getContentType();
            var rqBody = getRqBody(requestWrapper);
            var rsType = responseWrapper.getContentType();
            var rsBody = getRsBody(responseWrapper);
            responseWrapper.copyBodyToResponse();
            log.info("""
                            [INCOMING]
                            RqUID: '{}'
                            Method: '{}'
                            Uri: '{}'
                            Timing: '{} ms'
                            Status: '{}'
                            Rq-Headers: '{}'
                            Rq-Content-Type: '{}'
                            Rq: '{}'
                            Rs-Content-Type: '{}'
                            Rs: '{}'""",
                    rqUID,
                    method,
                    requestURI,
                    timing,
                    status,
                    rqHeaders,
                    rqType,
                    rqBody,
                    rsType,
                    rsBody
            );
        }
    }

    private String getHeaders(ContentCachingRequestWrapper requestWrapper) {
        var headers = new StringBuilder();
        var headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var header = headerNames.nextElement();
            headers.append(header)
                    .append("=\"")
                    .append(getHeaderValue(requestWrapper, header))
                    .append('"');
            if (headerNames.hasMoreElements()) {
                headers.append('\n');
            }
        }
        return headers.toString();
    }

    private String getHeaderValue(ContentCachingRequestWrapper request, String headerName) {
        var headerValues = request.getHeaders(headerName);
        var headerValue = new StringBuilder();
        while (headerValues.hasMoreElements()) {
            var value = headerValues.nextElement();
            headerValue.append(value);
            if (headerValues.hasMoreElements()) {
                headerValue.append(',');
            }
        }
        return headerValue.toString();
    }

    private String getRqBody(ContentCachingRequestWrapper request) {
        var contentType = request.getContentType();
        if (contentType == null || isNotPrintableContentType(contentType)) {
            return "%d bytes".formatted(request.getContentLength());
        }
        try {
            var inputStream = request.getInputStream();
            var content = inputStream.readAllBytes();
            if (content.length == 0) {
                content = request.getContentAsByteArray();
            }
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Can't read", e);
            return "can't read: " + e.getMessage();
        }
    }

    private String getRsBody(ContentCachingResponseWrapper response) {
        var contentType = response.getContentType();
        if (contentType == null || isNotPrintableContentType(contentType)) {
            return "%d bytes".formatted(response.getContentSize());
        }
        try {
            var inputStream = response.getContentInputStream();
            var content = inputStream.readAllBytes();
            if (content.length == 0) {
                content = response.getContentAsByteArray();
            }
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Can't read", e);
            return "can't read: " + e.getMessage();
        }
    }

    private boolean isNotPrintableContentType(String contentType) {
        var mediaType = MediaType.parseMediaType(contentType);
        for (var printableContentType : FULL_PRINT_CONTENT_TYPE) {
            if (printableContentType.isCompatibleWith(mediaType)) {
                return false;
            }
        }
        return true;
    }
}
