package com.TruckBooking.LoadsApi.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.TruckBooking.LoadsApi.Dao.TransporterEmailDao;
import com.TruckBooking.LoadsApi.Entities.TransporterEmail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TruckBooking.LoadsApi.Constants.CommonConstants;
import com.TruckBooking.LoadsApi.Dao.LoadDao;
import com.TruckBooking.LoadsApi.Entities.Load;
//import com.LoadsApi.LoadsApi.Entities.Load.Publish;
import com.TruckBooking.LoadsApi.Exception.BusinessException;
import com.TruckBooking.LoadsApi.Exception.EntityNotFoundException;
import com.TruckBooking.LoadsApi.Model.LoadRequest;
import com.TruckBooking.LoadsApi.Response.CreateLoadResponse;
import com.TruckBooking.LoadsApi.Response.UpdateLoadResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoadServiceImpl implements LoadService {

	@Autowired
	LoadDao loadDao;

	@Autowired
	TransporterEmailDao transporterEmailDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public CreateLoadResponse addLoad(LoadRequest loadrequest) {
		log.info("addLoad service is started");

		String temp = "";
		Load load = new Load();
		CreateLoadResponse response = new CreateLoadResponse();

		temp = "load:" + UUID.randomUUID();
		load.setLoadId(temp);
		response.setLoadId(temp);

		temp = loadrequest.getLoadingPoint().trim();
		load.setLoadingPoint(temp);
		response.setLoadingPoint(temp);

		temp = loadrequest.getLoadingPointCity().trim();
		load.setLoadingPointCity(temp);
		response.setLoadingPointCity(temp);

		temp = loadrequest.getLoadingPointState().trim();
		load.setLoadingPointState(temp);
		response.setLoadingPointState(temp);

		temp = loadrequest.getUnloadingPoint().trim();
		load.setUnloadingPoint(temp);
		response.setUnloadingPoint(temp);

		temp = loadrequest.getUnloadingPointCity().trim();
		load.setUnloadingPointCity(temp);
		response.setUnloadingPointCity(temp);

		temp = loadrequest.getUnloadingPointState().trim();
		load.setUnloadingPointState(temp);
		response.setUnloadingPointState(temp);

		temp = loadrequest.getCompanyName();
		if (StringUtils.isNotBlank(temp)){
			load.setCompanyName(temp);
			response.setCompanyName(temp);
		}

		temp = loadrequest.getLoadingPoint2();
		if(StringUtils.isNotBlank(temp)){
			load.setLoadingPoint2(temp.trim());
			response.setLoadingPoint2(temp.trim());
		}

		temp = loadrequest.getLoadingPointCity2();
		if(StringUtils.isNotBlank(temp)) {
			load.setLoadingPointCity2(temp.trim());
			response.setLoadingPointCity2(temp.trim());
		}

		temp = loadrequest.getLoadingPointState2();
		if(StringUtils.isNotBlank(temp)) {
			load.setLoadingPointState2(temp.trim());
			response.setLoadingPointState2(temp.trim());
		}

		temp = loadrequest.getUnloadingPoint2();
		if(StringUtils.isNotBlank(temp)) {
			load.setUnloadingPoint2(temp.trim());
			response.setUnloadingPoint2(temp.trim());
		}

		temp = loadrequest.getUnloadingPointCity2();
		if(StringUtils.isNotBlank(temp)) {
			load.setUnloadingPointCity2(temp.trim());
			response.setUnloadingPointCity2(temp.trim());
		}

		temp = loadrequest.getUnloadingPointState2();
		if(StringUtils.isNotBlank(temp)) {
			load.setUnloadingPointState2(temp.trim());
			response.setUnloadingPointState2(temp.trim());
		}

		temp = loadrequest.getPostLoadId();
		if (StringUtils.isNotBlank(temp)){
			load.setPostLoadId(temp.trim());
			response.setPostLoadId(temp.trim());
		}

		temp = loadrequest.getProductType();
		if (StringUtils.isNotBlank(temp)){
			load.setProductType(temp.trim());
			response.setProductType(temp.trim());
		}

		temp = loadrequest.getTruckType();
		if (StringUtils.isNotBlank(temp)){
			load.setTruckType(temp.trim());
			response.setTruckType(temp.trim());
		}

		temp = loadrequest.getWeight();
		if (StringUtils.isNotBlank(temp)){
			load.setWeight(temp.trim());
			response.setWeight(temp.trim());
		}

		temp=ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofPattern("E, MMM dd yyyy"));
		if (StringUtils.isNotBlank(temp)){
			load.setPostLoadDate(temp);
			response.setPostLoadDate(temp);
		}

		temp=null;
		temp = loadrequest.getNoOfTrucks();
		if(temp==null) {
			temp="1";
		}
		load.setNoOfTrucks(temp);
		response.setNoOfTrucks(temp);
		
		temp = loadrequest.getNoOfTyres();
		if (StringUtils.isNotBlank(temp)) {
			load.setNoOfTyres(temp.trim());
			response.setNoOfTyres(temp.trim());
		}

		temp = loadrequest.getLR();
		if(StringUtils.isNotBlank(temp)) {
			load.setLR(temp.trim());
			response.setLR(temp.trim());
		}
		
		temp = loadrequest.getBiddingEndDate();
		if(StringUtils.isNotBlank(temp)) {
			load.setBiddingEndDate(temp.trim());
			response.setBiddingEndDate(temp.trim());
		}
		
		temp = loadrequest.getBiddingEndTime();
		if(StringUtils.isNotBlank(temp)) {
			load.setBiddingEndTime(temp.trim());
			response.setBiddingEndTime(temp.trim());
		}

		temp = loadrequest.getLoadingDate();
		if(StringUtils.isNotBlank(temp)) {
			load.setLoadingDate(temp.trim());
			response.setLoadingDate(temp.trim());
		}
		
		temp = loadrequest.getPublishMethod();
		if(StringUtils.isNotBlank(temp)) {
			load.setPublishMethod(temp.trim());
			response.setPublishMethod(temp.trim());
		}
		List<String>ansList=loadrequest.getLoadingPointGeoId();
		if(ansList!=null){
			load.setLoadingPointGeoId(ansList);
			response.setLoadingPointGeoId(ansList);
		}
		List<String>ansList2=loadrequest.getUnloadingPointGeoId();
		if(ansList2!=null){
			load.setUnloadingPointGeoId(ansList2);
			response.setUnloadingPointGeoId(ansList2);
		}
		
		temp = loadrequest.getLoadingTime();
		if(StringUtils.isNotBlank(temp)) {
			load.setLoadingTime(temp.trim());
			response.setLoadingTime(temp.trim());
		}

		
		temp=ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofPattern("E, MMM dd yyyy"));
		if (StringUtils.isNotBlank(temp)){
			load.setPostLoadDate(temp);
			response.setPostLoadDate(temp);
		}
		
		load.setStatus(Load.Status.PENDING);
		response.setStatus(Load.Status.PENDING);

		temp = loadrequest.getComment();
		if (StringUtils.isNotBlank(temp)) {
			load.setComment(temp.trim());
			response.setComment(temp.trim());
		}

		if (loadrequest.getRate() != null) {
			load.setRate(loadrequest.getRate());
			response.setRate(loadrequest.getRate());
			if (loadrequest.getUnitValue() == null)
				throw new BusinessException("UnitValue can't be null when the rate is provided");

			temp = String.valueOf(loadrequest.getUnitValue());
			if ("PER_TON".equals(temp)) {
				load.setUnitValue(Load.UnitValue.PER_TON);
				response.setUnitValue(CreateLoadResponse.UnitValue.PER_TON);
			} else if ("PER_TRUCK".equals(temp)) {
				load.setUnitValue(Load.UnitValue.PER_TRUCK);
				response.setUnitValue(CreateLoadResponse.UnitValue.PER_TRUCK);
			}
		} else if (loadrequest.getUnitValue() != null) {
			throw new BusinessException("UnitValue can't be set when the rate is not provided");
		}

		loadDao.save(load);
		if(loadrequest.getTransporterList()!=null) {
			for (ArrayList<String> detail : loadrequest.getTransporterList()) {
				TransporterEmail transporterEmail = new TransporterEmail();
				transporterEmail.setEmail(detail.get(0));
				transporterEmail.setName(detail.get(1));
				transporterEmail.setPhoneNo(detail.get(2));
				transporterEmail.setTransporterId(detail.get(3));
				transporterEmail.setLoad(load);
				transporterEmailDao.save(transporterEmail);
			}
			response.setTransporterList(loadrequest.getTransporterList());
		}
		log.info("load is saved to the database");
		log.info("addLoad service response is returned");
		response.setTimestamp(load.getTimestamp());

		return response;
	}

	@SuppressWarnings("unused")
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public List<Load> getLoads(Integer pageNo, String loadingPointCity, String unloadingPointCity, String postLoadId,
			String truckType, boolean suggestedLoads, String transporterId, Timestamp startTimestamp, Timestamp endTimestamp) {
		log.info("getLoads service with params started");

		if (pageNo == null)
			pageNo = 0;

		Pageable currentPage = PageRequest.of(pageNo, CommonConstants.pagesize, Sort.Direction.DESC, "timestamp");

		if (suggestedLoads) {
			//List<Load> load = new ArrayList<>(loadDao.findAll(currentPage).getContent());
			// Collections.reverse(load);
			return loadDao.findByStatus(Load.Status.PENDING,currentPage);
		}

		if (loadingPointCity != null) {
			if (unloadingPointCity != null) {
				List<Load> load = loadDao.findByLoadingPointCityAndUnloadingPointCityAndStatus(loadingPointCity,
						unloadingPointCity, Load.Status.PENDING,currentPage);
				// Collections.reverse(load);
				return load;
			}
			List<Load> load = loadDao.findByLoadingPointCityAndStatus(loadingPointCity,Load.Status.PENDING, currentPage);
			// Collections.reverse(load);
			return load;
		}

		if (unloadingPointCity != null) {
			List<Load> load = loadDao.findByUnloadingPointCityAndStatus(unloadingPointCity,Load.Status.PENDING, currentPage);
			// Collections.reverse(load);
			return load;
		}

		if (postLoadId != null) {
			if(startTimestamp!=null && endTimestamp!=null)
			{
				List<Load> load=loadDao.findByPostLoadIdAndTimestampBetween(postLoadId, startTimestamp, endTimestamp);
				return load;
			} //refering to method to sort data by filters of postloadid and date range
			
			List<Load> load = loadDao.findByPostLoadIdAndStatus(postLoadId, Arrays.asList(Load.Status.PENDING, Load.Status.EXPIRED),currentPage);
			// Collections.reverse(load);
			return load;
		}

		if (truckType != null) {
			List<Load> load = loadDao.findByTruckTypeAndStatus(truckType, Load.Status.PENDING,currentPage);
			// Collections.reverse(load);
			return load;
		}


		if(transporterId!=null){
			List<Load> load=transporterEmailDao.findLoadsByTransporterId(transporterId);
			return load;
		}
		
		if(startTimestamp!=null && endTimestamp!=null){
			List<Load> load=loadDao.findByTimestampBetween(startTimestamp, endTimestamp);
			return load;
		}
		
		

		log.info("getLoads service response is returned");
		return loadDao.findByStatus(Load.Status.PENDING, currentPage);
	}

	
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public CreateLoadResponse getLoad(String loadId) {
		log.info("getLoad service by Id is started");
		Optional<Load> load = loadDao.findByLoadId(loadId);
		if (load.isEmpty())
			throw new EntityNotFoundException(Load.class, "id", loadId);
		log.info("getLoad service response is returned");
		CreateLoadResponse response=new CreateLoadResponse();

//		Getting List of all Transporter Associated with load
		List<TransporterEmail> list=transporterEmailDao.findByLoadLoadId(loadId);

//		Setting up the transporter List for response
		ArrayList<ArrayList<String>> emailList=new ArrayList<>();
		for(TransporterEmail transporterEmail:list){
			ArrayList<String> temp=new ArrayList<>();
			temp.add(transporterEmail.getEmail());
			temp.add(transporterEmail.getName());
			temp.add(transporterEmail.getPhoneNo());
			temp.add(transporterEmail.getTransporterId());
			emailList.add(temp);
		}
//		Setting all the of load fields for the response
		response.setLoadId(load.get().getLoadId());

		response.setLoadingPoint(load.get().getLoadingPoint());
		response.setLoadingPointCity(load.get().getLoadingPointCity());
		response.setLoadingPointState(load.get().getLoadingPointState());

		response.setUnloadingPoint(load.get().getUnloadingPoint());
		response.setUnloadingPointCity(load.get().getUnloadingPointCity());
		response.setUnloadingPointState(load.get().getUnloadingPointState());

		response.setCompanyName(load.get().getCompanyName());

		response.setLoadingPoint2(load.get().getLoadingPoint2());
		response.setLoadingPointCity2(load.get().getLoadingPointCity2());
		response.setLoadingPointState2(load.get().getLoadingPointState2());
		response.setLoadingPointGeoId(load.get().getLoadingPointGeoId());
		response.setUnloadingPointGeoId(load.get().getUnloadingPointGeoId());

		response.setUnloadingPoint2(load.get().getUnloadingPoint2());
		response.setUnloadingPointCity2(load.get().getUnloadingPointCity2());
		response.setUnloadingPointState2(load.get().getUnloadingPointState2());

		response.setPostLoadId(load.get().getPostLoadId());
		response.setProductType(load.get().getProductType());
		response.setTruckType(load.get().getTruckType());
		response.setNoOfTrucks(load.get().getNoOfTrucks());
		response.setNoOfTyres(load.get().getNoOfTyres());
		response.setWeight(load.get().getWeight());
		response.setLoadingDate(load.get().getLoadingDate());
		response.setPublishMethod(load.get().getPublishMethod());
		response.setLoadingTime(load.get().getLoadingTime());
		response.setPostLoadDate(load.get().getPostLoadDate());
		response.setStatus(load.get().getStatus());
		response.setLR(load.get().getLR());
		response.setComment(load.get().getComment());
		response.setRate(load.get().getRate());
		String temp = String.valueOf(load.get().getUnitValue());
		if ("PER_TON".equals(temp)) {
			response.setUnitValue(CreateLoadResponse.UnitValue.PER_TON);
		}
		else if ("PER_TRUCK".equals(temp)) {
			response.setUnitValue(CreateLoadResponse.UnitValue.PER_TRUCK);
		}
		response.setTimestamp(load.get().getTimestamp());

		response.setTransporterList(emailList);

//		Sending the response
		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public UpdateLoadResponse updateLoad(String loadId, LoadRequest updateLoad) {
		log.info("updateLoad service is started");

		Optional<Load> Id = loadDao.findByLoadId(loadId);
		if (Id.isEmpty())
			throw new EntityNotFoundException(Load.class, "id", loadId.toString());

		String temp = "";
		Load load = Id.get();

		temp = updateLoad.getLoadingPoint();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingPoint(temp.trim());
		}
		List<String>loadingPointGeoId=updateLoad.getLoadingPointGeoId();
		if(loadingPointGeoId!=null){
			load.setLoadingPointGeoId(loadingPointGeoId);
		}
		List<String>unloadingPointGeoId=updateLoad.getUnloadingPointGeoId();
		if(unloadingPointGeoId!=null){
			load.setUnloadingPointGeoId(unloadingPointGeoId);
		}

		temp = updateLoad.getLoadingPointCity();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingPointCity(temp.trim());
		}

		temp = updateLoad.getLoadingPointState();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingPointState(temp.trim());
		}

		temp = updateLoad.getUnloadingPoint();
		if (StringUtils.isNotBlank(temp)) {
			load.setUnloadingPoint(temp.trim());
		}

		temp = updateLoad.getUnloadingPointCity();
		if (StringUtils.isNotBlank(temp)) {
			load.setUnloadingPointCity(temp.trim());
		}

		temp = updateLoad.getUnloadingPointState();
		if (StringUtils.isNotBlank(temp)) {
			load.setUnloadingPointState(temp.trim());
		}

		temp = updateLoad.getCompanyName();
		if (StringUtils.isNotBlank(temp)){
			load.setCompanyName(temp.trim());
		}

		temp = updateLoad.getLoadingPoint2();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingPoint2(temp.trim());
		}

		temp = updateLoad.getLoadingPointCity2();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingPointCity2(temp.trim());
		}

		temp = updateLoad.getLoadingPointState2();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingPointState2(temp.trim());
		}

		temp = updateLoad.getUnloadingPoint2();
		if (StringUtils.isNotBlank(temp)) {
			load.setUnloadingPoint2(temp.trim());
		}

		temp = updateLoad.getUnloadingPointCity2();
		if (StringUtils.isNotBlank(temp)) {
			load.setUnloadingPointCity2(temp.trim());
		}

		temp = updateLoad.getUnloadingPointState2();
		if (StringUtils.isNotBlank(temp)) {
			load.setUnloadingPointState2(temp.trim());
		}

		temp = updateLoad.getNoOfTrucks();
		if (StringUtils.isNotBlank(temp)) {
			load.setNoOfTrucks(temp.trim());
		}

		temp = updateLoad.getNoOfTyres();
		if (StringUtils.isNotBlank(temp)) {
			load.setNoOfTyres(temp.trim());
		}
		
		temp = updateLoad.getTruckType();
		if (StringUtils.isNotBlank(temp)) {
			load.setTruckType(temp.trim());
		}

		temp = updateLoad.getProductType();
		if (StringUtils.isNotBlank(temp)) {
			load.setProductType(temp.trim());
		}

		temp = updateLoad.getWeight();
		if (StringUtils.isNotBlank(temp)) {
			load.setWeight(temp.trim());
		}

		temp = updateLoad.getLoadingDate();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingDate(temp.trim());
		}
		
		temp = updateLoad.getPublishMethod();
		if (StringUtils.isNotBlank(temp)) {
			load.setPublishMethod(temp.trim());
		}
		
		temp = updateLoad.getLoadingTime();
		if (StringUtils.isNotBlank(temp)) {
			load.setLoadingTime(temp.trim());
		}

		temp = updateLoad.getPostLoadId();
		if (StringUtils.isNotBlank(temp)) {
			load.setPostLoadId(temp);
		}

		if (updateLoad.getComment() != null) {
			load.setComment(updateLoad.getComment());
		}

		if(updateLoad.getLR() != null) {
			load.setLR(updateLoad.getLR());
		}
		
		temp = updateLoad.getBiddingEndDate();
		if (StringUtils.isNotBlank(temp)) {
			load.setBiddingEndDate(temp.trim());
		}
		
		temp = updateLoad.getBiddingEndTime();
		if (StringUtils.isNotBlank(temp)) {
			load.setBiddingEndTime(temp.trim());
		}
		

		if (updateLoad.getStatus() != null) {

			if (String.valueOf(updateLoad.getStatus()).equals("PENDING")) {
				load.setStatus(Load.Status.PENDING);
			} else if (String.valueOf(updateLoad.getStatus()).equals("ON_GOING")) {
				load.setStatus(Load.Status.ON_GOING);
			} else if (String.valueOf(updateLoad.getStatus()).equals("COMPLETED")) {
				load.setStatus(Load.Status.COMPLETED);
			} else if (String.valueOf(updateLoad.getStatus()).equals("EXPIRED")) {
				load.setStatus(Load.Status.EXPIRED);
			} else {
				throw new BusinessException("Unknown status");

			}
		}

		if (updateLoad.getRate() != null) {
			load.setRate(updateLoad.getRate());
			if (updateLoad.getUnitValue() == null)
				throw new BusinessException("UnitValue can't be null when the rate is provided");

			temp = String.valueOf(updateLoad.getUnitValue());

			if ("PER_TON".equals(temp)) {
				load.setUnitValue(Load.UnitValue.PER_TON);
			} else if ("PER_TRUCK".equals(temp)) {
				load.setUnitValue(Load.UnitValue.PER_TRUCK);
			}
		} else if (updateLoad.getUnitValue() != null) {
			throw new BusinessException("UnitValue can't be set when the rate is not provided");
		}

		UpdateLoadResponse response = new UpdateLoadResponse();
		response.setLoadId(loadId);
		response.setLoadingPoint(load.getLoadingPoint());
		response.setLoadingPointCity(load.getLoadingPointCity());
		response.setLoadingPointState(load.getLoadingPointState());
		response.setUnloadingPoint(load.getUnloadingPoint());
		response.setUnloadingPointCity(load.getUnloadingPointCity());
		response.setUnloadingPointState(load.getUnloadingPointState());
		response.setLoadingPoint2(load.getLoadingPoint2());
		response.setLoadingPointCity2(load.getLoadingPointCity2());
		response.setLoadingPointState2(load.getLoadingPointState2());
		response.setUnloadingPoint2(load.getUnloadingPoint2());
		response.setUnloadingPointCity2(load.getUnloadingPointCity2());
		response.setUnloadingPointState2(load.getUnloadingPointState2());
		response.setPostLoadId(load.getPostLoadId());
		response.setProductType(load.getProductType());
		response.setTruckType(load.getTruckType());
		response.setNoOfTrucks(load.getNoOfTrucks());
		response.setNoOfTyres(load.getNoOfTyres());
		response.setWeight(load.getWeight());
		response.setLoadingDate(load.getLoadingDate());
		response.setPostLoadDate(load.getPostLoadDate());
		response.setComment(load.getComment());
		response.setLR(load.getLR());
		response.setBiddingEndDate(load.getBiddingEndDate());
		response.setBiddingEndTime(load.getBiddingEndTime());
		response.setPublishMethod(load.getPublishMethod());
		response.setLoadingTime(load.getLoadingTime());
		response.setStatus(load.getStatus());
		response.setRate(load.getRate());
		response.setTimestamp(load.getTimestamp());
		response.setLoadingPointGeoId(load.getLoadingPointGeoId());
		response.setUnloadingPointGeoId(load.getUnloadingPointGeoId());

		temp = String.valueOf(load.getUnitValue());
		if ("PER_TON".equals(temp)) {
			response.setUnitValue(UpdateLoadResponse.UnitValue.PER_TON);
		} else if ("PER_TRUCK".equals(temp)) {
			response.setUnitValue(UpdateLoadResponse.UnitValue.PER_TRUCK);
		}

		loadDao.save(load);
		log.info("load is updated in the database");
		log.info("updateLoad service response is returned");
		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteLoad(String loadId) {
		log.info("deleteLoad service is started");
		Optional<Load> L = loadDao.findByLoadId(loadId);
		if (L.isEmpty())
			throw new EntityNotFoundException(Load.class, "id", loadId.toString());
		loadDao.delete(L.get());
		log.info("load is deleted successfully");
	}

	
}
