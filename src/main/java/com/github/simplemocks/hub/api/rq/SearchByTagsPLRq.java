package com.github.simplemocks.hub.api.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchByTagsPLRq {
    private List<String> tags;
    private int page;
    private int pageSize;
}
