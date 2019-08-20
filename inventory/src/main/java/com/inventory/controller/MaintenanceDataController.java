package com.inventory.controller;

import com.inventory.dto.MaintenanceDataDTO;
import com.inventory.dto.UpdateBatteryStatusDTO;
import com.inventory.model.MaintenanceData;
import com.inventory.service.MaintenanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api/maintenance") @Slf4j public class MaintenanceDataController {

  private final MaintenanceDataService maintenanceDataService;

  @Autowired public MaintenanceDataController(MaintenanceDataService maintenanceDataService) {
    this.maintenanceDataService = maintenanceDataService;
  }

  @GetMapping public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(transformMaintenanceData(maintenanceDataService.findAll()));
  }

  @GetMapping("/{id}") public Callable<ResponseEntity<?>> findOne(@PathVariable("id") Long id) {
    return () -> ResponseEntity
        .ok(maintenanceDataService.transformMaintenanceData(maintenanceDataService.findOne(id)));
  }

  @PostMapping public ResponseEntity<?> add(@RequestBody MaintenanceData maintenanceData) {
    return ResponseEntity.ok(maintenanceDataService.saveOrUpdate(maintenanceData));
  }

  @GetMapping("/search") public ResponseEntity<?> searchUser(@RequestParam(value = "text",required = false) String text,
      @RequestParam(value = "startDate",required = false) String startDate,@RequestParam(value = "endDate",required = false) String endDate ) {
    return ResponseEntity.ok(transformMaintenanceData(maintenanceDataService.searchElement(text,startDate,endDate)));
  }

  @GetMapping("/test") public Callable<ResponseEntity<MaintenanceData>> test() {
    return () -> ResponseEntity.ok(new MaintenanceData());
  }

  @GetMapping("/serialNumber") public ResponseEntity<Long> getSerialNumber() {
    return ResponseEntity.ok(maintenanceDataService.getSerialNumber());
  }

  @PostMapping("/updateBatteryStatus")
  public Callable<ResponseEntity<?>> updateBatteryStatus(@RequestBody List<UpdateBatteryStatusDTO> updateBatteryStatus){
    return () -> {
      maintenanceDataService.updateBatteryStatus(updateBatteryStatus);
      return ResponseEntity.ok().build();
    };
  }

  @GetMapping("/print/{id}")
  public Callable<ResponseEntity<?>> getPrintAbleData(@PathVariable Long id){
    return () -> ResponseEntity.ok(maintenanceDataService
        .getPrintAbleData(id));
  }

  private List<MaintenanceDataDTO> transformMaintenanceData(
      List<MaintenanceData> maintenanceDataList) {
    return maintenanceDataList.stream().map(maintenanceDataService::transformMaintenanceData)
        .collect(Collectors.toList());
  }

}
