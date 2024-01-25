package com.TruckBooking.Invoice_Services.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class InvoiceRequest {

    @NotBlank(message = "transporterId can not be null")
    public String transporterId;
    public String invoiceStatus;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name="bookingId")

    public List<String>bookingId;
    @CreationTimestamp
    public Timestamp invoiceTimestamp;

    public String transporterName;

    public String shipperId;

    public String invoiceDate;

    public String invoiceNo;

    public String invoiceAmount;

    public String partyName;

    public String dueDate;



}
