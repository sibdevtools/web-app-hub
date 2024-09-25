package com.github.sibdevtools.hub.api.rs;

import com.github.sibdevtools.hub.api.dto.WebApplicationPLDto;
import lombok.*;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetConfigurationsPLRs {
    private Map<String, WebApplicationPLDto> configs;
}
