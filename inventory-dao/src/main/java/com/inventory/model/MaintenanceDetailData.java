package com.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.stereotype.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.ZonedDateTime;

@Entity
@Table(name = "maintenance_detail_data")
@Indexed
@Data
public class MaintenanceDetailData {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @Column(name = "battery_vendor")
  private String batteryVendor;

  @Column(name = "battery_type")
  private String batteryType;

  @Column(name = "serial_number_code")
  private String serialNumberCode;

  @Column(name = "service_serial_number_code")
  private String serviceSerialNumberCode;

  /**
   * Service battery charges.
   */
  @Column(name = "service_charges")
  private Double serviceCharges;

  @Column(name = "charging_charges")
  private Double chargingCharges;

  @JsonIgnore
  @Column(name = "battery_in_date")
  private ZonedDateTime batteryInDate;

  @JsonIgnore
  @Column(name = "battery_out_date")
  private ZonedDateTime batteryOutDate;

  @Column(name = "battery_status")
  @Enumerated(EnumType.ORDINAL)
  private BatteryStatus batteryStatus = BatteryStatus.UNKNOWN;

  @Transient
  private String checkInDate;

  @Transient
  private String CheckOutDate;

  @Transient
  private String totalServiceCharges;
}
