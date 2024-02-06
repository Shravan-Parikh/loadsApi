package com.TruckBooking.Invoice_Services.Controller;

import com.TruckBooking.Invoice_Services.Entity.Invoice;
import com.TruckBooking.Invoice_Services.Model.InvoiceRequest;
import com.TruckBooking.Invoice_Services.Services.InvoiceService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping
@Slf4j
public class InvoiceController {
    private InvoiceService invoiceService;
    @Autowired
    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService =invoiceService;
    }
    @PostMapping("/invoice")
    public ResponseEntity<Object>postInvoiceService( @Valid @RequestBody InvoiceRequest invoice) {
        log.info("Invoice created sucessfully");

        return new ResponseEntity<>(invoiceService.addInvoice(invoice), HttpStatus.CREATED);


    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<Object>getInvoiceById(@PathVariable String invoiceId){
        log.info("getInvoiceServiceBYinvoiceID started sucessfully");
        return new ResponseEntity<>(invoiceService.getInvoiceById(invoiceId),HttpStatus.OK);
    }

    @GetMapping("/invoice")
    public ResponseEntity<List<Invoice>> findinvoiceService(@RequestParam(name="transporterId",required = false)String transporterId,
          @RequestParam(name="shipperId",required = false)String shipperId,
        @RequestParam(name = "fromTimestamp") Timestamp fromTimestamp,
        @RequestParam(name = "toTimestamp") Timestamp toTimestamp){

        return new ResponseEntity<>(invoiceService.getInvoice(transporterId,shipperId,fromTimestamp,toTimestamp), HttpStatus.OK);

    }
    @PutMapping("/invoice/{invoiceId}")
    public ResponseEntity<Object> updateInvoice(@PathVariable String invoiceId , @RequestBody InvoiceRequest invoiceModel){
        log.info("updateInvoices started");
        return new ResponseEntity<>(invoiceService.updateInvoice(invoiceId,invoiceModel),HttpStatus.OK);
    }
    @DeleteMapping("/invoice/{invoiceId}")
    public ResponseEntity<Object>deleteInvoice(@PathVariable String invoiceId){
        log.info("deleteInvoice started");

        invoiceService.deleteInvoice(invoiceId);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
    }




}
