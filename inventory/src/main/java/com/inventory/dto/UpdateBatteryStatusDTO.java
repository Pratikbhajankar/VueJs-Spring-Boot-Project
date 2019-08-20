package com.inventory.dto;

import com.inventory.model.BatteryStatus;
import lombok.Data;

@Data
public class UpdateBatteryStatusDTO {
  private Long id;
  private BatteryStatus batteryStatus;
}
