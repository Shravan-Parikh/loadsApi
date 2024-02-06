package com.TruckBooking.LoadsApi.EmailTask;

import com.TruckBooking.LoadsApi.Dao.TransporterEmailDao;
import com.TruckBooking.LoadsApi.Entities.Load;
import com.TruckBooking.LoadsApi.Entities.TransporterEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Slf4j
@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TransporterEmailDao transporterEmailDao;
    @Autowired
    private EntityManager entityManager;

    @Scheduled(fixedRate = 60000)
    public void sendEmail() throws MessagingException {
        List<TransporterEmail> emailList=transporterEmailDao.findByStatus("not-sent");
        for(TransporterEmail transporterEmail:emailList){

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            Load load=transporterEmail.getLoad();

            String companyName = load.getCompanyName();
            String subject="RFQ for "+load.getWeight()+"MT "+"from " +load.getLoadingPointCity()+" to "+load.getUnloadingPointCity();
            String body="<h2>Load Details</h2>"+
                    "This company has posted a load: "+companyName+"<br>"
                    +load.getLoadingPoint()+","+load.getLoadingPointCity()+","+load.getLoadingPointState()
                    +"<br> to <br>"+load.getUnloadingPoint()+","+load.getUnloadingPointCity()+","+load.getUnloadingPointState()+
                    "<hr>"+
                    "<h3>Requirements</h3>"+
                    "Truck Type: "+load.getTruckType()+"<br>"+
                    "Tyre :"+load.getNoOfTyres()+"<br>"+
                    "Weight :"+load.getWeight()+"<br>"+
                    "Product Type :"+load.getProductType();
            try{
                helper.setTo(transporterEmail.getEmail());
                helper.setSubject(subject);
                helper.setText(body,true);
            }
            catch (Exception e){
                log.info(String.valueOf(e));
                transporterEmail.setStatus("wrong email");
                transporterEmailDao.save(transporterEmail);
                continue;
            }
            try{
                javaMailSender.send(message);
            }
            catch(Exception e){
                log.info(String.valueOf(e));
                continue;
            }
            transporterEmail.setStatus("sent");
            transporterEmailDao.save(transporterEmail);
        }
    }
}
