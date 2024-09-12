package com.github.simplemocks.hub.controller;

import com.github.simple_mocks.localization_service.api.dto.LocalizationId;
import com.github.simple_mocks.localization_service.api.dto.LocalizedText;
import com.github.simple_mocks.localization_service.api.rq.LocalizeRq;
import com.github.simple_mocks.localization_service.api.service.LocalizationService;
import com.github.simplemocks.hub.api.dto.WebApplicationPLDto;
import com.github.simplemocks.hub.api.rq.SearchByTagsPLRq;
import com.github.simplemocks.hub.api.rs.GetConfigurationsPLRs;
import com.github.simplemocks.hub.api.rs.SearchByTagsPLRs;
import com.github.simplemocks.hub.service.WebApplicationServiceImpl;
import com.github.simplemocks.webapp.api.dto.WebApplication;
import com.github.simplemocks.webapp.api.rq.SearchByTagsRq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/app/hub/")
public class WebApplicationController {
    private final WebApplicationServiceImpl webApplicationService;
    private final LocalizationService localizationService;

    @GetMapping("v1/configuration/")
    public GetConfigurationsPLRs getConfigurations() {
        var userLocale = Locale.ENGLISH;

        var configMap = webApplicationService.getAll()
                .stream()
                .collect(Collectors.toMap(
                                WebApplication::getCode,
                                it -> WebApplicationPLDto.builder()
                                        .code(it.getCode())
                                        .frontendUrl(it.getFrontendUrl())
                                        .icon(getLocalization(it.getIconCode(), userLocale))
                                        .title(getLocalization(it.getTitleCode(), userLocale))
                                        .description(getLocalization(it.getDescriptionCode(), userLocale))
                                        .healthStatus(it.getHealthStatus())
                                        .build()
                        )
                );

        return GetConfigurationsPLRs.builder()
                .configs(configMap)
                .build();
    }

    @GetMapping("v1/applications/")
    public SearchByTagsPLRs searchByTags(@RequestBody SearchByTagsPLRq rq) {
        var userLocale = Locale.ENGLISH;

        var webApplicationPLDtos = webApplicationService.searchByTags(
                        SearchByTagsRq.builder()
                                .tags(rq.getTags())
                                .page(rq.getPage())
                                .pageSize(rq.getPageSize())
                                .build()
                )
                .stream()
                .map(it -> WebApplicationPLDto.builder()
                        .code(it.getCode())
                        .frontendUrl(it.getFrontendUrl())
                        .icon(getLocalization(it.getIconCode(), userLocale))
                        .title(getLocalization(it.getTitleCode(), userLocale))
                        .description(getLocalization(it.getDescriptionCode(), userLocale))
                        .healthStatus(it.getHealthStatus())
                        .build())
                .toList();

        return SearchByTagsPLRs.builder()
                .webApplications(webApplicationPLDtos)
                .build();
    }

    private String getLocalization(LocalizationId localizationId,
                                   Locale locale) {
        return Optional.ofNullable(localizationService.localize(
                                new LocalizeRq(localizationId, locale)
                        )
                )
                .map(LocalizedText::getMessage)
                .orElse(null);
    }
}
