package com.inventory.repository;

import com.inventory.model.MaintenanceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface MaintenanceDataRepository extends JpaRepository<MaintenanceData,Long> {

  @Query("FROM MaintenanceData m join m.maintenanceDetailData md WHERE (lower(m.name)  LIKE %:text% "
      + "OR lower(md.batteryVendor) LIKE %:text% "
      + "OR lower(md.batteryType) LIKE %:text% OR lower(md.serialNumberCode) LIKE %:text%)")
  List<MaintenanceData> searchElement(@Param("text") String text);


  @Query("FROM MaintenanceData m join m.maintenanceDetailData md WHERE (md.batteryInDate "
      + "BETWEEN :startDate AND :endDate OR md.batteryOutDate BETWEEN :startDate AND :endDate )"
      + "AND (lower(m.name)  LIKE %:text% "
      + " OR lower(md.batteryVendor) LIKE %:text% "
      + " OR lower(md.batteryType) LIKE %:text% OR lower(md.serialNumberCode) LIKE %:text%)")
  List<MaintenanceData> searchElement(@Param("text") String text,
      @Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

  @Query("FROM MaintenanceData m join m.maintenanceDetailData md WHERE md.batteryInDate "
      + "BETWEEN :startDate AND :endDate OR md.batteryOutDate BETWEEN :startDate AND :endDate group by m.id")
  List<MaintenanceData> searchElement(@Param("startDate") ZonedDateTime startDate,
      @Param("endDate") ZonedDateTime endDate);

  Optional<MaintenanceData> findTopByOrderByIdDesc();
}
