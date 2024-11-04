package com.github.sibdevtools.hub.logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
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
import java.util.*;

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
    private static final long MAX_BODY_SIZE = 1024;

    private final ObjectMapper objectMapper;

    public ApiLoggerFilter() {
        this.objectMapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var rqUID = UUID.randomUUID().toString();

        var requestWrapper = getRqWrapper(request);
        var responseWrapper = getRsWrapper(request, response);

        var startTime = System.currentTimeMillis();
        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            var timing = System.currentTimeMillis() - startTime;
            var method = request.getMethod();
            var requestURI = request.getRequestURI();
            var httpLogEntry = HttpLogEntry.builder()
                    .direction(RequestDirection.IN)
                    .rqUID(rqUID)
                    .method(method)
                    .uri(requestURI)
                    .timing(timing)
                    .status(responseWrapper.getStatus())
                    .rqHeaders(getHeaders(requestWrapper))
                    .rq(getRqBody(requestWrapper))
                    .rsHeaders(getHeaders(responseWrapper))
                    .rs(getRsBody(responseWrapper))
                    .build();
            responseWrapper.copyBodyToResponse();
            log.info("{}", objectMapper.writeValueAsString(httpLogEntry));
        }
    }

    private static ContentCachingResponseWrapper getRsWrapper(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ContentCachingResponseWrapper responseWrapper;
        if (request instanceof ContentCachingResponseWrapper contentCachingResponseWrapper) {
            responseWrapper = contentCachingResponseWrapper;
        } else {
            responseWrapper = new ContentCachingResponseWrapper(response);
        }
        return responseWrapper;
    }

    private static ContentCachingRequestWrapper getRqWrapper(HttpServletRequest request) {
        ContentCachingRequestWrapper requestWrapper;
        if (request instanceof ContentCachingRequestWrapper contentCachingRequestWrapper) {
            requestWrapper = contentCachingRequestWrapper;
        } else {
            requestWrapper = new ContentCachingRequestWrapper(request);
        }
        return requestWrapper;
    }

    private Map<String, List<String>> getHeaders(HttpServletResponse rs) {
        var headers = new HashMap<String, List<String>>();
        var headerNames = rs.getHeaderNames();
        for (var header : headerNames) {
            headers.put(header, getHeaderValues(rs, header));
        }
        return headers;
    }

    private List<String> getHeaderValues(HttpServletResponse rs, String headerName) {
        return Optional.of(rs.getHeaders(headerName))
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    private Map<String, List<String>> getHeaders(HttpServletRequest rq) {
        var headers = new HashMap<String, List<String>>();
        var headerNames = rq.getHeaderNames();
        if (headerNames == null) {
            return Collections.emptyMap();
        }
        while (headerNames.hasMoreElements()) {
            var header = headerNames.nextElement();
            headers.put(header, getHeaderValues(rq, header));
        }
        return headers;
    }

    private List<String> getHeaderValues(HttpServletRequest rq, String headerName) {
        var enumeration = rq.getHeaders(headerName);
        var values = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            var value = enumeration.nextElement();
            values.add(value);
        }
        return values;
    }

    private String getRqBody(ContentCachingRequestWrapper request) {
        var contentType = request.getContentType();
        var contentLength = request.getContentLength();
        if (contentType == null || isNotPrintableContentType(contentType) || contentLength > MAX_BODY_SIZE) {
            if (contentLength < 0) {
                return null;
            }
            return "%d bytes".formatted(contentLength);
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
        var contentLength = response.getContentSize();
        if (contentType == null || isNotPrintableContentType(contentType) || contentLength > MAX_BODY_SIZE) {
            if (contentLength < 0) {
                return null;
            }
            return "%d bytes".formatted(contentLength);
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
