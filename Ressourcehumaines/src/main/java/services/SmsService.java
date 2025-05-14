package services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService {
//    public static final String ACCOUNT_SID = "AC50923b89c730b8640d640d3254300100";
    public static final String AUTH_TOKEN = "4b0f2d2d2790155d32a8f90e81a12129";
    public static final String FROM_NUMBER = "+17624225809"; // Ton numéro Twilio


    public static void sendSms(String to, String body) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(to),           // Numéro du destinataire
                new PhoneNumber(FROM_NUMBER),  // Numéro Twilio
                body
        ).create();

        System.out.println("SMS envoyé : " + message.getSid());
    }

}
