package com.TruckBooking.LoadsApi.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

//import com.LoadsApi.LoadsApi.Entities.Load.Publish;
import com.TruckBooking.LoadsApi.Entities.Load.Status;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoadRequest {

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

	private String companyName;

	private String postLoadId;       //optional
	private String productType;      //optional
	private String truckType;        //optional
	private String weight;           //optional

	private String loadingPoint2;		//optional
	private String loadingPointCity2;	//optional
	private String loadingPointState2;	//optional
	private String unloadingPoint2;		//optional
	private String unloadingPointCity2;  //optional
	private String unloadingPointState2; //optional
	private ArrayList<ArrayList<String>> transporterList;

	private String noOfTrucks;
	private String noOfTyres;
	private String LR; // optional
	private String biddingEndDate;  //optional
	private String biddingEndTime; //optional
	private String comment; // this should be an optional
	private String loadingDate;
	private String publishMethod;
	private String loadingTime;
	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name="loadingPointGeoId")
	private List<String> loadingPointGeoId=new ArrayList<>(); //optional




	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name="unloadingPointGeoId")
	private List<String> unloadingPointGeoId=new ArrayList<>(); //optional


	private Long rate;
	@Enumerated(EnumType.STRING)
	private UnitValue unitValue;
	public enum UnitValue {
		PER_TON, PER_TRUCK
	}
	public Status status;
}
