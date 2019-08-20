package com.inventory.service;


import com.inventory.dto.MaintenanceDataDTO;
import com.inventory.dto.PrintAbleMaintenanceDTO;
import com.inventory.dto.UpdateBatteryStatusDTO;
import com.inventory.model.MaintenanceData;

import java.util.List;

public interface MaintenanceDataService {
  MaintenanceData saveOrUpdate(MaintenanceData maintenanceData);
  List<MaintenanceData> findAll();
  List<MaintenanceData> searchElement(String text, String startDate, String endDate);
  Long getSerialNumber();
  MaintenanceDataDTO transformMaintenanceData(MaintenanceData maintenanceData);
  MaintenanceData findOne(Long id);
  void updateBatteryStatus(List<UpdateBatteryStatusDTO> batteryStatusDTOS);
  PrintAbleMaintenanceDTO getPrintAbleData(Long id);
}
