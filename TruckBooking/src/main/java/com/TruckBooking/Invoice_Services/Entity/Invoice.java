package com.TruckBooking.Invoice_Services.Entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Table(name = "InvoiceServices")
@Data
@Entity

public class Invoice {

    @Id
    public String invoiceId;
    @NotBlank(message = "transporterId can not be null")
    public String transporterId;
    public String transporterName;//optional
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name="bookingId")
    private List<String>bookingId=new ArrayList<>();
    @CreationTimestamp
    public Timestamp invoiceTimestamp;



    public String shipperId; //optional

    public String invoiceDate; //optional

    public String invoiceNo; //optional

    public String invoiceAmount; //optional

    public String partyName; //optional

    public String dueDate; //optional



    public String invoiceStatus;




}
