package com.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Indexed;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "maintenance_data")
@Indexed
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MaintenanceData extends BaseModel{
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "vehicle_number")
  private String vehicleNumber;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "maintenance_Detail_Id")
  private Set<MaintenanceDetailData> maintenanceDetailData;

  @JsonIgnore
  @Transient
  private ZonedDateTime batteryInDate;

  @Column(name = "serial_number")
  private Long serialNumber;

  @Column(name = "deposit_amount")
  private double depositAmount;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "transaction_type")
  private TransactionType transactionType;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "account_status")
  private AccountStatus accountStatus = AccountStatus.OPEN;

  @Transient
  private String checkInDate;

  @Transient
  private String CheckOutDate;
}

