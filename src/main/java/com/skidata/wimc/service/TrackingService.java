package com.skidata.wimc.service;

import com.skidata.wimc.tracking.CarPark;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackingService {

    @GetMapping("/carpark")
    public CarPark getCarPark() {
        return new CarPark();
    }
}
