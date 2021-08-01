package com.mypros.corona.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationStats {
    private  String state;
    private String country;
    private int latestTotal;
}
