package com.tass.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Plane {
    private String flightCode;
    private String planeType;
    private String from;
    private String to;

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Plane)){
            return false;
        }

        Plane plane = (Plane) o;
        return this.flightCode.equals(plane.getFlightCode());
    }
}
