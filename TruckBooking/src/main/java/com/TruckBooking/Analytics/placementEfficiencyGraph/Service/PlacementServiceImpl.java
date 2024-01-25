package com.TruckBooking.Analytics.placementEfficiencyGraph.Service;

import com.TruckBooking.Analytics.placementEfficiencyGraph.Dao.PlacementDao;
import com.TruckBooking.Analytics.placementEfficiencyGraph.Entities.PlacementInfo;
import com.TruckBooking.Analytics.placementEfficiencyGraph.Model.PlacementEfficiencyResponse;
import com.TruckBooking.Booking.Dao.BookingDao;
import com.TruckBooking.Booking.Entities.BookingData;
import com.TruckBooking.ContractRateUpload.Dao.ContractRateRepo;
import com.TruckBooking.ContractRateUpload.Dao.IndentDao;
import com.TruckBooking.ContractRateUpload.Entity.Indent;
import com.TruckBooking.ContractRateUpload.Entity.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class PlacementServiceImpl implements PlacementService{

    private static Timestamp lastSchedulerTimestamp = Timestamp.from(Instant.now());

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private IndentDao indentDao;

    @Autowired
    private PlacementDao placementDao;

    @Autowired
    private ContractRateRepo contractRateRepo;

    @Override
    public Object getPlacementData(String id){
        if (id.charAt(0) == 't'){ //checking if id given is transporterId or shipperId  since ids begin with the respective id name
            // transporterId = "transporter:heX codE"
            Optional<PlacementInfo> optional = placementDao.findById(id);
            if (optional.isPresent()){

                return setResponse(optional.get());  // returning the calculated efficiency response.
            }
            else return "TransporterId does not exists.";    // If transporterId is wrong or not found
        }
        else{
            // shipperId = "shipperId:heX codE"
            Optional<List<PlacementInfo>> optional = placementDao.findByShipperId(id);
            if (optional.isPresent()){

                List<PlacementEfficiencyResponse> responseList = new ArrayList<>();
                for (PlacementInfo info: optional.get()){
                    responseList.add(setResponse(info));      // adding in a list since all the transporter belong to this particular shipper.
                }
                return responseList;
            }
            else return "ShipperId does not exists";          // If shipperId is wrong or not found
        }
    }

    public PlacementEfficiencyResponse setResponse(PlacementInfo info){

        PlacementEfficiencyResponse response = new PlacementEfficiencyResponse();
        HashMap<String, float[]> responseMap = new HashMap<>();                            // creating entities for response.

        Map<String, String> map= info.getPlacementMap();                           // actual map containing data.

        for (String str: map.keySet()){

            String[] arr = map.get(str).substring(1,map.get(str).length()-1).split(", ");
            // Above line will convert from  "[12, 43, 75, 87, 9]"   to ["12", "43", "75", "87", "9"]
            // Basically converting Stringified array to an array of String  to get these values as Numbers.

            float[] responseArr = new float[arr.length];           // initializing a float array to store placement efficiency percentage.

            float total=0;
            for (int i=0; i<arr.length; i++){
                responseArr[i]=Float.parseFloat(arr[i]);           // converting each element of String array "arr" to float and calculating sum/total deliveries.
                total+=responseArr[i];
            }
            for (int i=0; i<arr.length; i++){
                responseArr[i]= (responseArr[i]/total)*100;        // Taking out and storing percentage for each time duration.
            }

            responseMap.put(str, responseArr);
        }

        response.setTransporterId(info.getTransporterId());
        response.setTransporterName(info.getTransporterName());
        response.setShipperId(info.getShipperId());
        response.setPlacementEfficiencyMap(responseMap);           // setting up all the fields of response
        return response;
    }

    @Scheduled(fixedDelay = 6000000)   // every 10 mins
    public void FindPlacement() {
        List<BookingData> bookingDataList = bookingDao.findByTimestampIsAfter(lastSchedulerTimestamp);
        lastSchedulerTimestamp = Timestamp.from(Instant.now());

        if (!bookingDataList.isEmpty()) {
            //System.out.println("Bookings are Found");

            for (BookingData bookingData : bookingDataList) {
                // getting transporterId from booking Data
                // shipperId from contractService through transporterId
                // these ids will be stored.
                String transporterId = bookingData.getTransporterId();
                Rates rates = contractRateRepo.findByTransporterId(transporterId);
                String shipperId = rates.getShipperId();
                String transporterName = rates.getTransporterName();

                String loadId = bookingData.getLoadId(); // to get the data from indent table load Id is reqd.
                Timestamp bookingTimestamp = bookingData.getTimestamp();
                long bookingMillis = bookingTimestamp.getTime(); // getting bookingMillis to make booking calendar and find the year, month, week of booking
                Calendar bookingCalendar = Calendar.getInstance();
                bookingCalendar.setTimeInMillis(bookingMillis);
                String bookingYear = "year: " + bookingCalendar.get(Calendar.YEAR);  // in which booking was made
                String bookingMonth = "month: " +( 1+ bookingCalendar.get(Calendar.MONTH)); // adding 1 since monthNumber begins with 0 i.e. 0 -> Jan
                String bookingWeek = "week: " + bookingCalendar.get(Calendar.WEEK_OF_YEAR);

                Indent indent = indentDao.findByLoadId(loadId);

                int index = getIndex(indent.getAssignedTime(), bookingMillis);

                Optional<com.TruckBooking.Analytics.placementEfficiencyGraph.Entities.PlacementInfo> optional = placementDao.findById(transporterId); // finding if the placement for this transporter existed before or not
                PlacementInfo placementInfo;
                if (optional.isPresent()) {
                    placementInfo = optional.get();
                    Map<String, String> placementMap = placementInfo.getPlacementMap();

                    setArray(bookingYear, index, placementMap);
                    setArray(bookingMonth, index, placementMap);
                    setArray(bookingWeek, index, placementMap);

                } else {
                    placementInfo = new PlacementInfo();
                    HashMap<String, String> placementMap = new HashMap<>();
                    int[] placementArray = new int[5];
                    placementArray[index] = 1;
                    placementMap.put(bookingYear, Arrays.toString(placementArray));
                    placementMap.put(bookingMonth, Arrays.toString(placementArray));
                    placementMap.put(bookingWeek, Arrays.toString(placementArray));
                    placementInfo.setPlacementMap(placementMap);
                    placementInfo.setShipperId(shipperId);
                    placementInfo.setTransporterId(transporterId);
                    placementInfo.setTransporterName(transporterName);
                }

                placementDao.save(placementInfo);
            }
        }
    }

    private int getIndex(Timestamp indentTimestamp, long bookingMillis) {  // [0,0,0,0,0] 5 indexes and where to add according to calculates time duration
        long indentMillis = indentTimestamp.getTime(); // getting indentMillis to calculate the time difference between
        // load assigning to the transporter and transporter assigning the vehicle for load\

        int millisInAnHour = 3600000;  // millis in 1 hour
        int index; // index for the list at which we will add the booking
        long difference = bookingMillis - indentMillis;

        if (difference <= 5 * millisInAnHour) {  // 0-5 hours
            index = 0;
        } else if (difference <= 10 * millisInAnHour) {// 5-10 hours
            index = 1;
        } else if (difference <= 15 * millisInAnHour) {  // 10-15 hours
            index = 2;
        } else if (difference <= 20 * millisInAnHour) {  // 15-20 hrs
            index = 3;
        } else {
            index = 4;    // >20 hours
        }

        return index;
    }

    private void setArray(String bookingTime, int index, Map<String, String> placementMap) {
        int[] placementArray;
        if (placementMap.containsKey(bookingTime)) {
            String[] arr = placementMap.get(bookingTime).substring(1,placementMap.get(bookingTime).length()-1).split(", ");
            // Above line will convert from  "[12, 43, 75, 87, 9]"   to ["12", "43", "75", "87", "9"]
            // Basically converting Stringified array to an array of String  to get these values as Numbers.
            arr[index] = Integer.parseInt(arr[index])+1+"";
            placementMap.put(bookingTime, Arrays.toString(arr));
        } else {
            placementArray = new int[5];
            placementArray[index] = 1;
            placementMap.put(bookingTime, Arrays.toString(placementArray));
        }
    }
}
