package com.github.simplemocks.hub.api.dto;

import com.github.simplemocks.webapp.api.dto.HealthStatus;
import lombok.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebApplicationPLDto {
    private String code;
    private String frontendUrl;
    private String icon;
    private String title;
    private String description;
    private HealthStatus healthStatus;
}
