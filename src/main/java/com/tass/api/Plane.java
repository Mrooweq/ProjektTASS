package com.tass.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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
        return this.flightCode.equals(plane.getFlightCode())
                && this.from.equals(plane.getFrom())
                && this.to.equals(plane.getTo());
    }

    @Override
    public int hashCode(){
        return Arrays.asList(flightCode, from, to).hashCode();
    }
}
