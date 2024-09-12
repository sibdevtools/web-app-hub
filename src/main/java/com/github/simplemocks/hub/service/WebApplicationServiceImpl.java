package com.github.simplemocks.hub.service;

import com.github.simplemocks.hub.exception.ApplicationNotFoundException;
import com.github.simplemocks.webapp.api.dto.WebApplication;
import com.github.simplemocks.webapp.api.rq.GetApplicationRq;
import com.github.simplemocks.webapp.api.rq.SearchByTagsRq;
import com.github.simplemocks.webapp.api.service.WebApplicationService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class WebApplicationServiceImpl implements WebApplicationService {
    private final Map<String, WebApplication> webApplications;

    public WebApplicationServiceImpl(List<WebApplication> webApplications) {
        this.webApplications = webApplications.stream()
                .collect(Collectors.toMap(WebApplication::getCode, Function.identity()));
    }

    @Nonnull
    @Override
    public WebApplication getByCode(@Nonnull GetApplicationRq rq) {
        var code = rq.code();
        var webApplication = webApplications.get(code);
        if (webApplication == null) {
            throw new ApplicationNotFoundException("Application not found");
        }
        return webApplication;
    }

    @Nonnull
    @Override
    public List<WebApplication> searchByTags(@Nonnull SearchByTagsRq rq) {
        var tags = rq.tags();
        var pageSize = rq.pageSize();
        return webApplications.values().stream()
                .filter(it -> isFitTags(it, tags))
                .limit(pageSize)
                .skip((rq.page() - 1L) * pageSize)
                .toList();
    }

    private boolean isFitTags(WebApplication webApplication, List<String> tags) {
        for (var tag : tags) {
            var applicationTags = webApplication.getTags();
            if (!applicationTags.contains(tag)) {
                return false;
            }
        }
        return true;
    }

    public List<WebApplication> getAll() {
        return new ArrayList<>(webApplications.values());
    }
}
