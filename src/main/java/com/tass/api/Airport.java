package com.tass.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Airport {
    private String name;
    private String code;
    private Long views;
}
