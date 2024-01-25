package com.TruckBooking.ContractRateUpload.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="Contract_Rates")
public class Rates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Loading Point Cannot Be Empty")
    private String loadingPointCity;

    @NotBlank(message = "UnLoading Point Cannot Be Empty")
    private String unloadingPointCity;

    @NotNull(message = "Weight Cannot Be Empty")
    private String weight;
    private Integer rate;               //optional
    private String transporterId;       //optional
    private String shipperId;
    private String transporterName;     //optional

    @NotBlank(message = "Transporter Email Cannot Be Empty")
    private String transporterEmail;
}