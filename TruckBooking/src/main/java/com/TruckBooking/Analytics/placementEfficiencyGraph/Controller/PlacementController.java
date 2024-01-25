package com.TruckBooking.Analytics.placementEfficiencyGraph.Controller;

import com.TruckBooking.Analytics.placementEfficiencyGraph.Service.PlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlacementController {

    @Autowired
    private PlacementService placementService;

    @GetMapping("/placementEfficiencyGraph")
    public ResponseEntity<?> placementGraph(@RequestParam String id){   // id - common for both transporterId and shipperId
        Object response = placementService.getPlacementData(id);
        if (response.getClass().equals("".getClass())){    // checking if the response returned is String or Not.
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
