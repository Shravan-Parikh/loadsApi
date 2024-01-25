package com.TruckBooking.ContractRateUpload.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TruckBooking.ContractRateUpload.EmailSender.SendEmail;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TruckBooking.ContractRateUpload.Dao.ContractRateRepo;
import com.TruckBooking.ContractRateUpload.Dao.IndentDao;
import com.TruckBooking.ContractRateUpload.Entity.Indent;
import com.TruckBooking.ContractRateUpload.Entity.Rates;
import com.TruckBooking.LoadsApi.Dao.LoadDao;
import com.TruckBooking.LoadsApi.Dao.TransporterEmailDao;
import com.TruckBooking.LoadsApi.Entities.Load;
import com.TruckBooking.LoadsApi.Entities.Load.Status;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContractRateService {

    @Autowired
    LoadDao loadDao;

    @Autowired
    TransporterEmailDao transporterEmailDao;

    @Autowired
    ContractRateRepo contractRateRepo;

    @Autowired
    IndentDao indentDao;

    @Autowired
    private SendEmail email;

    // check that file is of excel type or not
    public boolean isExcelFile(MultipartFile file) {

        String contentType = file.getContentType();
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    }

    // this function helps us to save excel file.
    public boolean saveRates(MultipartFile file, String shipperId) {

        try {
            List<Rates> products = extractRateListFromExcel(file.getInputStream(), shipperId);
            contractRateRepo.saveAll(products);
            log.info("Saved");
            return true;
        } catch (Exception e) {
            log.error(String.valueOf(e));
            return false;
        }
    }

    // convert Excel sheet data to a list of rates
    public List<Rates> extractRateListFromExcel(InputStream is, String shipperId){

        List<Rates> rateList = new ArrayList<>();

        try (XSSFWorkbook workbook=new XSSFWorkbook(is)){

            XSSFSheet sheet = workbook.getSheet("Sheet1");
            Rates rates;
            int rowNumber=0,cId;
            try{
                for (Row row : sheet) {

                    // Since first row contains The name of the entries to be performed therefore we've to skip
                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }
                    rowNumber++;
                    rates = new Rates();

                // To protect any error last column should always be a non-empty Column (that doesn't accept null values
                // 7 is since we have only these many fields to accept
                    for (cId=0; cId<7;cId++) {
                // This line protects the null/ blank cells from being skipped.
                        Cell cell =row.getCell(cId, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (cell.getCellType().toString().equals("BLANK")) {

                            // These cId's represent the fields that can accept null values
                            // i.e. Rate, TransporterId and TransporterName
                            if (cId == 2 || cId == 3 || cId == 5) {
                                continue;
                            }
                            log.error("Row " + rowNumber + " Contains blank entries " + cId);
                            break;
                        }
                        switch (cId) {
                            case 0:
                                rates.setUnloadingPointCity(cell.toString());
                                break;
                            case 1:
                                rates.setWeight(String.valueOf((int) cell.getNumericCellValue()));
                                break;
                            case 2:
                                rates.setRate((int) cell.getNumericCellValue());
                                break;
                            case 3:
                                rates.setTransporterId(cell.toString());
                                break;
                            case 4:
                                rates.setTransporterEmail(cell.toString());
                                break;
                            case 5:
                                rates.setTransporterName(cell.toString());
                                break;
                            case 6:
                                rates.setLoadingPointCity(cell.toString());
                                break;
                            default:
                                break;
                        }
                    }
                    // >=7 means that no non-empty cells contains null values.
                    //since we only have to add those rows that satisfy the conditions.
                    if (cId >= 7) {
                        rates.setShipperId(shipperId);
                        rateList.add(rates);
                    }
                }
            }
            catch (Exception e){
                log.error(e.getMessage());
            }
        }
        catch(Exception e){
            log.error("No Excel Sheet Detected");
        }
        return rateList;
    }

    public Indent saveIndent(Indent indent ){
        indent.setPosition(0);
        indent.setStatus(Status.NOT_ASSIGNED);
        indentDao.save(indent);
        return indent;
    }

    // Find the ranks for particular LoadId and arrange them in ascending order in Indent Table
    @Scheduled(fixedRate = 60000)
    public void findRank(){
        List<Load> loads = loadDao.findByPublishMethodAndStatus("contract",Status.PENDING);

        // Instanced for storing in Indent Table
        List<String> transporterId;
        List<String> transporterEmail;

        List<Rates> rateList;
        Indent indentTable;

        for (Load x : loads){
            rateList = contractRateRepo.findByLoadingPointCityAndUnloadingPointCityAndWeightOrderByRateAsc(x.getLoadingPointCity(),x.getUnloadingPointCity(),x.getWeight());
            if (!rateList.isEmpty()){
                transporterId = new ArrayList<>();
                transporterEmail = new ArrayList<>();
                for (Rates y : rateList) {
                    transporterId.add(y.getTransporterId());
                    transporterEmail.add(y.getTransporterEmail());
                }
                indentTable = new Indent(x.getLoadId(), transporterId, 0, transporterEmail, Status.NOT_ASSIGNED);
                x.setStatus(Status.NOT_ASSIGNED);
                loadDao.save(x);
                indentDao.save(indentTable);
            }
        }
        triggerMail();
    }

    // Function for sending mails
    public void triggerMail()  {
        List<Indent> responses = this.indentDao.findAll();
        for (Indent it : responses) {
            if ((it.getStatus()) == Status.NOT_ASSIGNED) {
                List<String> list = it.getTransporterEmail();
                Load load = loadDao.findByLoadId(it.getLoadId()).get();

                // subject format "indent for 25MT from ambala to delhi"
                String subject = "Indent for "+load.getWeight()+" from "+ load.getLoadingPointCity() +" to "+ load.getUnloadingPointCity();

                // body format  //Asian Paint has posted a load
                //Loading Point: Mumbai,Mumbai,Maharashtra, India
                //to
                //Unloading Point: Delhi Public School Bengaluru South, Road Mango Garden Layout Konanakune,Bengaluru,Karnataka, India
                //Requirements
                //Truck Type: TRAILER_BODY
                //Tyre :6
                //Weight :25
                //Product Type :Agriculture and Food
                String body = load.getLoadingPoint()+" has posted a load\n" +
                        "Loading Point: " + load.getLoadingPoint() + ", " + load.getLoadingPointCity() + ", " + load.getLoadingPointState() + ", India\n" +
                        "to\n" +
                        "Unloading Point: " +load.getUnloadingPoint() + ", " + load.getUnloadingPointCity() + ", " + load.getUnloadingPointState() + ", India\n" +
                        "Requirements\n" +
                        "Truck Type: " + load.getTruckType() +"\n" +
                        "Tyre : " + load.getNoOfTyres() + "\n" +
                        "Weight : " + load.getWeight() + "\n" +
                        "Product Type : " + load.getProductType();

//              address = new SendEmail(it.getTransporterEmail().get(it.getPosition()), body, subject);
                email.send(it.getTransporterEmail().get(it.getPosition()), subject, body);
                if (email.isSend){
                    it.setStatus(Status.INDENT_ASSIGNED);
                }
                else{
                    // if address of transporter is wrong then system is gonna change the position to the next one
                    // i.e. indent will be assigned to the next transporter immediately
                    if (it.getPosition()+1<list.size()){
                        it.setPosition(it.getPosition() + 1);
                    }
                    else {
                        it.setStatus(Status.TRANSPORTER_REJECTED);// To look here
                    }
                }
                indentDao.save(it);
            }
        }
    }

    // Scheduler to reassign indent if it's been rejected by the Transporter
    @Scheduled(fixedRate = 120000)
    public void checkRejected(){
        List<Indent> rejectedIndents = indentDao.findByStatus(Status.TRANSPORTER_REJECTED);
        if (!rejectedIndents.isEmpty()){
            for (Indent indent: rejectedIndents){
                if (indent.getPosition()+1<indent.getTransporterEmail().size()){
                    indent.setPosition(indent.getPosition()+1);
                    indent.setStatus(Status.NOT_ASSIGNED);
                }
                else{
                    indent.setStatus(Status.TRANSPORTER_REJECTED);// To change here
                }
                indentDao.save(indent);
            }
        }
        triggerMail();
    }

    // Scheduler to automatically assign the load to next indent after a
    // certain interval of time i.e. after 2-2:30 hrs indent will be assigned to the next Transporter
    @Scheduled(fixedRate = 1800000)
    public void checkUnassigned(){
        List<Indent> unassignedIndents = indentDao.findByStatus(Status.INDENT_ASSIGNED);
        if (!unassignedIndents.isEmpty()){
            // For Getting current time
            Date date = new Date();
            for (Indent indent: unassignedIndents){
                // These lines check if the time duration is greater than 2 hours
                int initial = indent.getAssignedTime().getHours();
                int current = date.getHours();
                if (current - initial < 2){
                    continue;
                }
                if (indent.getPosition()+1<indent.getTransporterEmail().size()){
                    indent.setPosition(indent.getPosition()+1);
                    indent.setStatus(Status.NOT_ASSIGNED);
                }
                else{
                    indent.setStatus(Status.TRANSPORTER_REJECTED);// To change here
                }
                indentDao.save(indent);
            }
        }
        triggerMail();
    }

    public List<Rates> getRates(String shipperId){
        return contractRateRepo.findByShipperId(shipperId);
    }
}