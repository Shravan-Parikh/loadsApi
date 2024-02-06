package com.TruckBooking.Invoice_Services.Dao;

import com.TruckBooking.Invoice_Services.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice,String> {

    //List<Invoice>findByshipperId(String shipperId);
  //  Optional<InvoiceDetails>findByInvoiceId(String invoiceId);
    Optional<Invoice>findByInvoiceId(String invoiceId);
   // List<Invoice> findByStatus(Invoice.Status status);

    //List<Invoice> findBytransporterId(String transporterId);
    List<Invoice> findByTransporterIdAndInvoiceTimestampBetween(String transporterId, Timestamp fromTimestamp, Timestamp toTimestamp);

    List<Invoice> findByShipperIdAndInvoiceTimestampBetween(String shipperId, Timestamp fromTimestamp, Timestamp toTimestamp);
}





