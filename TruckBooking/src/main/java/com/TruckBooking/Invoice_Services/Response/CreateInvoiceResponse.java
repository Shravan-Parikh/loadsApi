package com.TruckBooking.Invoice_Services.Response;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.sql.Timestamp;
import java.util.List;

@Data
public class CreateInvoiceResponse {
    public String invoiceId;
    public String transporterId;
    public String transporterName;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name="bookingId")
    public List<String>bookingId;


    public  String invoiceStatus;

    public String shipperId;
    public String invoiceDate;
    public String invoiceNo;

    public String invoiceAmount;
    public String partyName;
    public String dueDate;


}
