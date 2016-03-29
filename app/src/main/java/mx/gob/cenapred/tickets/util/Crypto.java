package mx.gob.cenapred.tickets.util;

import org.springframework.util.support.Base64;

import java.io.IOException;
import java.nio.charset.Charset;

public class Crypto {
    public String encryptMessage(String message, String key){
        Integer charMessage, charKey, keySize = key.length();
        String messageEncrypted = "";
        for(Integer i = 0; i < message.length(); i++){
            charMessage = (int) message.charAt(i);
            charKey = (int) key.charAt(auxCharacterKey(i,keySize));
            messageEncrypted += (char) (charMessage + charKey);
        }

        byte[] bytesMessageEncrypted = messageEncrypted.getBytes(Charset.forName("UTF-8"));
        messageEncrypted = Base64.encodeBytes(bytesMessageEncrypted);

        return messageEncrypted;
    }

    public String decryptMessage(String message, String key) throws IOException {
        Integer charMessage, charKey, keySize = key.length();
        byte[] bytesMessageEncrypted = Base64.decode(message);
        String messageEncrypted = new String(bytesMessageEncrypted, Charset.forName("UTF-8"));
        String messageDecrypted = "";

        for( Integer i = 0; i < messageEncrypted.length(); i++ ){
            charMessage = (int)messageEncrypted.charAt(i);
            charKey = (int) key.charAt(auxCharacterKey(i,keySize));
            messageDecrypted += (char) (charMessage - charKey);
        }

        return messageDecrypted;
    }

    private Integer auxCharacterKey(Integer position, Integer keySize){
        return (position % keySize);
    }
}
