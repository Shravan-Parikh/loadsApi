package com.TruckBooking.LoadsApi.Response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.TruckBooking.LoadsApi.Entities.Load.Status;

import lombok.Data;

@Data
public class CreateLoadResponse {

	private String loadId;
	private String loadingPoint;
	private String loadingPointCity;
	private String loadingPointState;
	private String unloadingPoint;
	private String unloadingPointCity;
	private String unloadingPointState;

	private String companyName;

	private String postLoadId;           //optional
	private String productType;          //optional
	private String truckType;            //optional
	private String weight;               //optional
	private String postLoadDate;         //optional

	private String loadingPoint2;		//optional
	private String loadingPointCity2;	//optional
	private String loadingPointState2;	//optional
	private String unloadingPoint2;		//optional
	private String unloadingPointCity2;  //optional
	private String unloadingPointState2; //optional

	private ArrayList<ArrayList<String>> transporterList;
	private String noOfTrucks;
	private String noOfTyres;
	private String loadingDate;
	private String publishMethod;
	private String loadingTime;
	public Status status;
	private String LR; //optional
	private String biddingEndDate;  //optional
	private String biddingEndTime; //optional
	private String comment; // this should be an optional
	private Long rate;
	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name="loadingPointGeoId")
	private List<String> loadingPointGeoId=new ArrayList<>(); //optional




	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name="unloadingPointGeoId")
	private List<String> unloadingPointGeoId=new ArrayList<>(); //optional





	@Enumerated(EnumType.STRING)
	private UnitValue unitValue;

	public enum UnitValue {
		PER_TON, PER_TRUCK
	}

	public Timestamp timestamp;

}
