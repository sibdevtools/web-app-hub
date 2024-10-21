package com.github.sibdevtools.hub.controller;

import com.github.sibdevtools.common.api.rs.StandardBodyRs;
import com.github.sibdevtools.hub.api.dto.WebApplicationPLDto;
import com.github.sibdevtools.hub.api.rq.SearchByTagsPLRq;
import com.github.sibdevtools.hub.api.rs.GetConfigurationsPLRs;
import com.github.sibdevtools.hub.api.rs.SearchByTagsPLRs;
import com.github.sibdevtools.hub.service.WebApplicationServiceImpl;
import com.github.sibdevtools.localization.api.dto.LocalizationId;
import com.github.sibdevtools.localization.api.dto.LocalizedText;
import com.github.sibdevtools.localization.api.rq.LocalizeRq;
import com.github.sibdevtools.localization.api.service.LocalizationService;
import com.github.sibdevtools.webapp.api.rq.SearchByTagsRq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
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
                .map(it -> WebApplicationPLDto.builder()
                        .code(it.getCode())
                        .frontendUrl(it.getFrontendUrl())
                        .icon(getLocalization(it.getIconCode(), userLocale))
                        .title(getLocalization(it.getTitleCode(), userLocale))
                        .description(getLocalization(it.getDescriptionCode(), userLocale))
                        .healthStatus(it.getHealthStatus())
                        .version(it.getVersion())
                        .build())
                .collect(Collectors.toMap(
                                WebApplicationPLDto::getCode,
                                Function.identity()
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
                .getBody()
                .stream()
                .map(it -> WebApplicationPLDto.builder()
                        .code(it.getCode())
                        .frontendUrl(it.getFrontendUrl())
                        .icon(getLocalization(it.getIconCode(), userLocale))
                        .title(getLocalization(it.getTitleCode(), userLocale))
                        .description(getLocalization(it.getDescriptionCode(), userLocale))
                        .healthStatus(it.getHealthStatus())
                        .version(it.getVersion())
                        .build())
                .toList();

        return SearchByTagsPLRs.builder()
                .webApplications(webApplicationPLDtos)
                .build();
    }

    private String getLocalization(LocalizationId localizationId,
                                   Locale locale) {
        var rq = new LocalizeRq(localizationId, locale);
        var rs = localizationService.localize(rq);
        return Optional.of(rs)
                .map(StandardBodyRs::getBody)
                .map(LocalizedText::getMessage)
                .orElse(null);
    }
}
