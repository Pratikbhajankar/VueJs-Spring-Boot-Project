package com.inventory.dto;

import com.inventory.model.BatteryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PrintAbleMaintenanceDetailDataDTO {


  private String serialNumberCode;
  private String batteryVendor;
  private String batteryType;
  private Double serviceCharges;
  private double currentServiceCharges;
  private BatteryStatus batteryStatus = BatteryStatus.UNKNOWN;
  private String checkInDate;
  private String CheckOutDate;
}
