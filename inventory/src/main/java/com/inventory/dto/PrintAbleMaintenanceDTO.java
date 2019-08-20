package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PrintAbleMaintenanceDTO {
  private String name;
  private String vehicleNumber;
  private String todayDate;
  private String receiptNumber;
  private List<PrintAbleMaintenanceDetailDataDTO> maintenanceDetailDataDTOS;
}
