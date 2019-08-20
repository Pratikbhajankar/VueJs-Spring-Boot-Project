package com.inventory.dto;

import com.inventory.model.AccountStatus;
import com.inventory.model.MaintenanceDetailData;
import com.inventory.model.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data public class MaintenanceDataDTO {

  private Long id;

  private String name;

  private String vehicleNumber;

  private String checkInDate;

  private String CheckOutDate;

  private String batteryVendor;

  private String batteryType;

  private String serialNumberCode;

  private String serviceCharges;

  private Set<MaintenanceDetailData> maintenanceDetailData;

  private double depositAmount;
  private TransactionType transactionType;

  private AccountStatus accountStatus;

  private double totalAmount;

  private double balanceAmount;
  private double totalChargingCharges;
  private double totalServiceCharges;

}
