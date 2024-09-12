package com.github.simplemocks.hub.api.rs;

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
    private Map<String, String> configs;
}
