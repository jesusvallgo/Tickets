package mx.gob.cenapred.tickets.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;

public class ApiKeyUtil {
    public String createApiKey(CredencialesEntity credencialesEntity) throws JsonProcessingException {
        // Instancia para mapear entidades
        ObjectMapper mapper = new ObjectMapper();

        // Convierte la entidad Credenciales en un String en formato JSON
        String jsonCredenciales = mapper.writeValueAsString(credencialesEntity);

        // Instancia para cifrar cadenas
        Crypto crypto = new Crypto();

        // Cifra la cadena JSON que sera enviada a traves de la URL
        String apiKey = crypto.encryptMessage(jsonCredenciales, MainConstant.PASSWORD_CRYPTO);

        return apiKey;
    }
}
