package com.TruckBooking.LoadsApi.Service;

import java.sql.Timestamp;
import java.util.List;

import com.TruckBooking.LoadsApi.Entities.Load;
import com.TruckBooking.LoadsApi.Model.LoadRequest;
import com.TruckBooking.LoadsApi.Response.CreateLoadResponse;
import com.TruckBooking.LoadsApi.Response.UpdateLoadResponse;

public interface LoadService {
	public CreateLoadResponse addLoad(LoadRequest load);

	public List<Load> getLoads(Integer pageNo, String loadingPointCity, String unloadingPointCity, String shipperId,
			String truckType, boolean suggestedLoads, String transporterId, Timestamp startTimestamp, Timestamp endTimestamp);
	
	
	public UpdateLoadResponse updateLoad(String loadId, LoadRequest loadrequest);

	public CreateLoadResponse getLoad(String loadId);

	public void deleteLoad(String loadId);

}
