package com.TruckBooking.Analytics.placementEfficiencyGraph.Model;

import lombok.Data;

import java.util.HashMap;

@Data
public class PlacementEfficiencyResponse {

    private String transporterId;
    private String transporterName;
    private String shipperId;
    private HashMap<String, float[]> placementEfficiencyMap;

}
