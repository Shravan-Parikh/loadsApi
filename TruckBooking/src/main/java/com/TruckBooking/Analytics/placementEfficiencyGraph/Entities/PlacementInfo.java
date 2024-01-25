package com.TruckBooking.Analytics.placementEfficiencyGraph.Entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
public class PlacementInfo {
    @Id
    private String transporterId;

    private String transporterName;
    private String shipperId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "placement_map", joinColumns = @JoinColumn(name = "transporter_id"))
    @MapKeyColumn(name = "placement_map_key")
    @Column(name = "placement_map_value")
    private Map<String, String> placementMap = new HashMap<>();// "year: 2023" : "[10, 20, 30, 40, 34]", "month: Jan": "[10, 25, 39, 06]", "week: 10": "[10, 20, 30, 40]"
}
