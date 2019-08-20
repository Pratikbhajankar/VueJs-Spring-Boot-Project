package com.inventory.service.impl;

import com.inventory.config.ApplicationConfigurationProperties;
import com.inventory.dto.MaintenanceDataDTO;
import com.inventory.dto.UpdateBatteryStatusDTO;
import com.inventory.model.BatteryStatus;
import com.inventory.model.MaintenanceData;
import com.inventory.model.MaintenanceDetailData;
import com.inventory.repository.MaintenanceDataRepository;
import com.inventory.repository.MaintenanceDetailDataRepository;
import com.inventory.service.MaintenanceDataService;
import com.inventory.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @Slf4j public class MaintenanceDataServiceImpl implements MaintenanceDataService {

  private final MaintenanceDataRepository maintenanceDataRepository;
  private final ApplicationConfigurationProperties configurationProperties;
  private final MaintenanceDetailDataRepository maintenanceDetailDataRepository;

  @Autowired public MaintenanceDataServiceImpl(MaintenanceDataRepository maintenanceDataRepository,
      ApplicationConfigurationProperties configurationProperties,
      MaintenanceDetailDataRepository maintenanceDetailDataRepository) {
    this.maintenanceDataRepository = maintenanceDataRepository;
    this.configurationProperties = configurationProperties;
    this.maintenanceDetailDataRepository = maintenanceDetailDataRepository;
  }

  @Override public MaintenanceData saveOrUpdate(MaintenanceData maintenanceData) {
    try {
      maintenanceData.setBatteryInDate(CommonUtils
          .stringToDate(maintenanceData.getCheckInDate(), configurationProperties.getDateFormat()));
      maintenanceData.getMaintenanceDetailData().forEach(maintenanceDetailData -> {
        maintenanceDetailData.setBatteryStatus(BatteryStatus.CHECKIN);
        maintenanceDetailData.setBatteryInDate(ZonedDateTime.now());
      });
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return maintenanceDataRepository.save(maintenanceData);
  }

  @Override public List<MaintenanceData> findAll() {
    return maintenanceDataRepository.findAll();
  }

  @Override public List<MaintenanceData> searchElement(String text, String startDate,
      String endDate) {

    if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
      try {
        final ZonedDateTime startDateZoned = CommonUtils
            .stringToDate(startDate, configurationProperties.getDateFormat());
        final ZonedDateTime endDateZoned = CommonUtils
            .stringToDate(endDate, configurationProperties.getDateFormat()).plusDays(1);
        if (StringUtils.isEmpty(text)) {
          return maintenanceDataRepository.searchElement(startDateZoned, endDateZoned);
        }
        return maintenanceDataRepository.searchElement(text, startDateZoned, endDateZoned);
      } catch (ParseException e) {
        log.error(e.getMessage(), e);

      }
    }
    if (StringUtils.isEmpty(text)) {
      return findAll();
    }
    return maintenanceDataRepository.searchElement(text);
  }

  public MaintenanceDataDTO transformMaintenanceData(MaintenanceData maintenanceData) {
    MaintenanceDataDTO.MaintenanceDataDTOBuilder builder = MaintenanceDataDTO.builder();
    builder.name(maintenanceData.getName());
    builder.id(maintenanceData.getId());
    builder.vehicleNumber(maintenanceData.getVehicleNumber());
    builder.depositAmount(maintenanceData.getDepositAmount());
    builder.transactionType(maintenanceData.getTransactionType());
    builder.accountStatus(maintenanceData.getAccountStatus());

    if (!Objects.isNull(maintenanceData.getCreatedAt())) {
      builder.checkInDate(CommonUtils
          .dateToString(maintenanceData.getCreatedAt(), configurationProperties.getDateFormat()));
    }

    if (!CollectionUtils.isEmpty(maintenanceData.getMaintenanceDetailData())) {

      builder.batteryType(maintenanceData.getMaintenanceDetailData().stream()
          .map(MaintenanceDetailData::getBatteryType).filter(StringUtils::isNotEmpty)
          .collect(Collectors.joining(",")));
      builder.batteryVendor(maintenanceData.getMaintenanceDetailData().stream()
          .map(MaintenanceDetailData::getBatteryVendor).filter(StringUtils::isNotEmpty)
          .collect(Collectors.joining(",")));
      builder.serialNumberCode(maintenanceData.getMaintenanceDetailData().stream()
          .map(MaintenanceDetailData::getSerialNumberCode).filter(StringUtils::isNotEmpty)
          .collect(Collectors.joining(",")));
      builder.serviceCharges(maintenanceData.getMaintenanceDetailData().stream()
          .map(MaintenanceDetailData::getServiceCharges).map(String::valueOf)
          .filter(Objects::nonNull).collect(Collectors.joining(",")));

      //Comment:: accounting start from here.
      final double totalServiceCharges = maintenanceData.getMaintenanceDetailData().stream()
          .map(data->{
            final ZonedDateTime endDate = data.getBatteryOutDate() != null ?
                data.getBatteryOutDate() :
                ZonedDateTime.now();
            final Long days = CommonUtils.dayDiff(data.getBatteryInDate(), endDate);
            return days ==  0 ?
                data.getServiceCharges() :
                data.getServiceCharges() * (days + 1 * 1.0);
          }).mapToDouble(Double::doubleValue).sum();

      final double totalChargingCharges = maintenanceData.getMaintenanceDetailData().stream().
          mapToDouble(MaintenanceDetailData::getChargingCharges).sum();
      builder.totalServiceCharges(totalServiceCharges);
      builder.totalAmount(totalServiceCharges+totalChargingCharges);
      builder.totalChargingCharges(totalChargingCharges);
      builder.balanceAmount((totalServiceCharges+totalChargingCharges)
          -maintenanceData.getDepositAmount());

      maintenanceData.getMaintenanceDetailData().forEach(maintenanceDetailData -> {
        if (!Objects.isNull(maintenanceDetailData.getBatteryInDate())) {
          maintenanceDetailData.setCheckInDate(CommonUtils
              .dateToString(maintenanceDetailData.getBatteryInDate(),
                  configurationProperties.getDateFormat()));
        }
        if (!Objects.isNull(maintenanceDetailData.getBatteryOutDate())) {
          maintenanceDetailData.setCheckOutDate(CommonUtils
              .dateToString(maintenanceDetailData.getBatteryOutDate(),
                  configurationProperties.getDateFormat()));
        }
      });
      builder.maintenanceDetailData(maintenanceData.getMaintenanceDetailData());
    }
    return builder.build();
  }

  @Override public Long getSerialNumber() {
    final Optional<MaintenanceData> maintenanceDataOptional = maintenanceDataRepository
        .findTopByOrderByIdDesc();
    if (!maintenanceDataOptional.isPresent() || Objects
        .isNull(maintenanceDataOptional.get().getSerialNumber())
        || maintenanceDataOptional.get().getSerialNumber() == 0) {
      return 23454L;
    }
    return maintenanceDataOptional.get().getSerialNumber() + 1;

  }

  @Override public MaintenanceData findOne(Long id) {
    return maintenanceDataRepository.findById(id).orElse(new MaintenanceData());
  }

  @Override public void updateBatteryStatus(List<UpdateBatteryStatusDTO> batteryStatusDTOS) {
    final List<Long> ids = batteryStatusDTOS.stream().map(UpdateBatteryStatusDTO::getId)
        .collect(Collectors.toList());
    final List<MaintenanceDetailData> allDetailData = maintenanceDetailDataRepository
        .findAllById(ids);
    allDetailData.forEach(maintenanceDetailData -> {
      final Optional<UpdateBatteryStatusDTO> optionalUpdateBatteryStatus = batteryStatusDTOS
          .stream().filter(updateBatteryStatus -> updateBatteryStatus.getId()
              .equals(maintenanceDetailData.getId())).findFirst();
      if (optionalUpdateBatteryStatus.isPresent()) {
        final UpdateBatteryStatusDTO updateBatteryStatus = optionalUpdateBatteryStatus.get();

        switch (updateBatteryStatus.getBatteryStatus()) {
        case CHECKIN:
          maintenanceDetailData.setBatteryInDate(ZonedDateTime.now());
          break;
        case CHECKOUT:
          maintenanceDetailData.setBatteryOutDate(ZonedDateTime.now());
          break;
        case UNKNOWN:
        default:
          break;
        }
        maintenanceDetailData.setBatteryStatus(updateBatteryStatus.getBatteryStatus());
      }
    });
    maintenanceDetailDataRepository.saveAll(allDetailData);
  }
}
