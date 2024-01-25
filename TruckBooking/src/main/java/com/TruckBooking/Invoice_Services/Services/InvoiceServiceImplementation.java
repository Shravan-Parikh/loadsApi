package com.TruckBooking.Invoice_Services.Services;

import com.TruckBooking.Invoice_Services.Dao.InvoiceDao;
import com.TruckBooking.Invoice_Services.Entity.Invoice;
import com.TruckBooking.Invoice_Services.Model.InvoiceRequest;
import com.TruckBooking.Invoice_Services.Response.CreateInvoiceResponse;
import com.TruckBooking.Invoice_Services.Response.UpdateInvoiceResponse;
import com.TruckBooking.LoadsApi.Exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class InvoiceServiceImplementation implements InvoiceService {

    InvoiceDao invoiceDao;

    @Autowired
    public InvoiceServiceImplementation(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    //  This request is used to create and add details in the invoice table in the database.
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CreateInvoiceResponse addInvoice(InvoiceRequest invoiceRequest) {
        log.info("addInvoice service is started");
        String temp=" ";
        //This object is created to make an entry in the database
        Invoice invoice = new Invoice();
        //This is created to send the response on the postman.
        CreateInvoiceResponse response = new CreateInvoiceResponse();
        //primary key
        temp="invoice:"+ UUID.randomUUID();
        invoice.setInvoiceId(temp);
        response.setInvoiceId(temp);
        temp = " ";
        //This is setting the values in the database and response to be sent on postman
        temp = invoiceRequest.getTransporterId();
            invoice.setTransporterId(temp);
            response.setTransporterId(temp);

        temp = invoiceRequest.getDueDate();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setDueDate(temp);
            response.setDueDate(temp);
        }
       List<String>ansList= invoiceRequest.getBookingId();
        if(ansList!=null){
            invoice.setBookingId(ansList);
            response.setBookingId(ansList);
        }


        temp = invoiceRequest.getShipperId();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setShipperId(temp);
            response.setShipperId(temp);
        }
        temp = invoiceRequest.getInvoiceNo();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setInvoiceNo(temp);
            response.setInvoiceNo(temp);
        }
        temp = invoiceRequest.getInvoiceDate();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setInvoiceDate(temp);
            response.setInvoiceDate(temp);
        }
        temp = invoiceRequest.getInvoiceAmount();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setInvoiceAmount(temp);
            response.setInvoiceAmount(temp);
        }
        temp=invoiceRequest.getTransporterName();
        if(StringUtils.isNotBlank(temp)){
            invoice.setTransporterName(temp);
            response.setTransporterName(temp);
        }
        temp = invoiceRequest.getPartyName();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setPartyName(temp);
            response.setPartyName(temp);
        }
        temp=invoiceRequest.getInvoiceStatus();
        if(StringUtils.isNotBlank(temp)){
            invoice.setInvoiceStatus(temp);
            response.setInvoiceStatus(temp);

        }
        temp = invoiceRequest.getDueDate();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setDueDate(temp);
            response.setDueDate(temp);
        }

   //saving the value in database
         invoiceDao.save(invoice);


        return response;

    }
    //This request retrieves the data for a particular invoiceId provided.
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    @Override
    public CreateInvoiceResponse getInvoiceById(String invoiceId){
        log.info("getInvoicesByInvoiceId Service by Id Started");
        //It fetches the data for a particular invoiceId
        Optional<Invoice> invoice= invoiceDao.findByInvoiceId(invoiceId);
        //This object is created to send the response on postman
        CreateInvoiceResponse response=new CreateInvoiceResponse();
        Invoice invoiceAns=invoice.get();
        //setting the values in the response
        response.setInvoiceId(invoiceAns.getInvoiceId());
        response.setInvoiceNo(invoiceAns.getInvoiceNo());
        response.setInvoiceDate(invoiceAns.getInvoiceDate());
        response.setInvoiceAmount(invoiceAns.getInvoiceAmount());
        response.setTransporterId(invoiceAns.getTransporterId());
        response.setShipperId(invoiceAns.getShipperId());
        response.setPartyName(invoiceAns.getPartyName());
        response.setTransporterName(invoiceAns.getTransporterName());
        response.setInvoiceStatus(invoiceAns.getInvoiceStatus());
        response.setBookingId(invoiceAns.getBookingId());

        response.setDueDate(invoiceAns.getDueDate());
        //returning the response
        return response;


    }
    //This request retrieves the list of InvoiceService for a transporterId

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public List<Invoice> getInvoice(String transporterId, String shipperId, Timestamp fromTimestamp, Timestamp toTimestamp) {

        log.info("getInvoice services with params started");
        List<Invoice> listans = null;
        //getting the value for a transporterdId
        if(transporterId!=null) {
            listans = invoiceDao.findByTransporterIdAndInvoiceTimestampBetween(transporterId ,fromTimestamp,  toTimestamp);
            return listans;
        }
        //getting the value for a shipperId
        if(shipperId!=null){
           listans = invoiceDao. findByShipperIdAndInvoiceTimestampBetween(shipperId,fromTimestamp,toTimestamp);
            return listans;
        }
        log.info("getInvoice service response is returned");
        return  listans ;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override

    //This request updates the data in the database according to the invoiceID provided in the url
    public UpdateInvoiceResponse updateInvoice(String invoiceId, InvoiceRequest invoiceRequest) {
        log.info("UpdateInvoice started");
        String temp = " ";
        //It fetches data for a invoiceId from the database.
        Optional<Invoice> ans = invoiceDao.findByInvoiceId(invoiceId);
        UpdateInvoiceResponse response = new UpdateInvoiceResponse();

        Invoice invoice = ans.get();
        //updating the values in the database
        temp = invoiceRequest.getInvoiceNo();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setInvoiceNo(temp);
            response.setInvoiceNo(invoice.getInvoiceNo());
        }
        temp = invoiceRequest.getInvoiceDate();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setInvoiceDate(temp);
        }
        temp = invoiceRequest.getInvoiceAmount();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setInvoiceAmount(temp);
            response.setInvoiceAmount(invoice.getInvoiceAmount());
        }



        temp = invoiceRequest.getPartyName();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setPartyName(temp);
            response.setPartyName(invoice.getPartyName());
        }
        temp = invoiceRequest.getDueDate();
        if (StringUtils.isNotBlank(temp)) {
            invoice.setDueDate(temp);

        }
        temp=invoiceRequest.getInvoiceStatus();
        if(StringUtils.isNotBlank(temp)){
            invoice.setInvoiceStatus(temp);

        }
        temp=invoiceRequest.getTransporterName();
        if(StringUtils.isNotBlank(temp)){
            invoice.setTransporterName(temp);

        }
        List<String>L=invoiceRequest.getBookingId();
        if(L!=null){
            invoice.setBookingId(L);

        }

        response.setInvoiceId(invoiceId);

       //getting the non updated value from database and setting them in response
        Invoice i = invoiceDao.save(invoice);
        response.setTransporterId(i.getTransporterId());
        response.setBookingId(i.getBookingId());
        response.setInvoiceId(i.getInvoiceId());
        response.setTransporterName(i.getTransporterName());
        response.setInvoiceStatus(i.getInvoiceStatus());
        response.setDueDate(i.getDueDate());
        response.setPartyName(i.getPartyName());
        response.setInvoiceAmount(i.getInvoiceAmount());
        response.setInvoiceDate(i.getInvoiceDate());
        response.setInvoiceNo(i.getInvoiceNo());
        response.setShipperId(i.getShipperId());
        return response;

    }
    //This request retrievs data from the database
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public void deleteInvoice(String invoiceId){
        log.info("deleteInvoice service is started");
        //getting the required invoiceId
        Optional<Invoice> L = invoiceDao.findByInvoiceId(invoiceId);
        if (L.isEmpty())
            throw new EntityNotFoundException(Invoice.class, "id", invoiceId.toString());
        invoiceDao.delete(L.get());
        log.info("invoice is deleted successfully");

    }


}









