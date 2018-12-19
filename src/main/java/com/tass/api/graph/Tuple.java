package com.tass.api.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tuple {
    private String from;
    private String to;
    private Value value;
}
