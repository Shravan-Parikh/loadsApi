package com.TruckBooking.LoadsApi.Entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table(name = "load")
@Data
public class Load {
	@Id
	private String loadId;
	@NotBlank(message = "Loading Point Cannot Be Empty")
	private String loadingPoint;
	@NotBlank(message = "Loading Point City Cannot Be Empty")
	private String loadingPointCity;
	@NotBlank(message = "Loading Point State Cannot Be Empty")
	private String loadingPointState;

	@NotBlank(message = "Unloading Point Cannot Be Empty")
	private String unloadingPoint;
	@NotBlank(message = "Unloading Point City Cannot Be Empty")
	private String unloadingPointCity;
	@NotBlank(message = "Unloading Point State Cannot Be Empty")
	private String unloadingPointState;

	private String postLoadId;       // optional
	private String productType;      // optional
	private String truckType;        // optional
	private String weight;           // optional
	private String postLoadDate;     // optional
	private String companyName;

	@CreationTimestamp
	public Timestamp timestamp;

	private String loadingPoint2;		//optional
	private String loadingPointCity2;	//optional
	private String loadingPointState2;	//optional
	private String unloadingPoint2;		//optional
	private String unloadingPointCity2;  //optional
	private String unloadingPointState2; //optional

	
	private String noOfTrucks;
	private String noOfTyres;
	private String LR; // optional
	private String comment; // this should be an optional

	
	private String loadingDate;
	private String publishMethod;
	private String loadingTime;

	
	private Long rate; // optional
	
	private String biddingEndDate;  //optional
	private String biddingEndTime; //optional
	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name="unloadingPointGeoId")
	private List<String> unloadingPointGeoId=new ArrayList<>(); //optional

	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name="loadingPointGeoId")
	private List<String> loadingPointGeoId=new ArrayList<>(); //optional

	@Enumerated(EnumType.STRING)
	private UnitValue unitValue; // optional

	@Enumerated(EnumType.STRING)
	public Status status;

	public enum UnitValue {
		PER_TON, PER_TRUCK
	}

	public enum Status {
		PENDING, EXPIRED, NOT_ASSIGNED, INDENT_ASSIGNED, TRANSPORTER_REJECTED, ON_GOING, COMPLETED
	}
}
