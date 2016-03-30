package mx.gob.cenapred.tickets.webservice;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.ErrorManager;
import mx.gob.cenapred.tickets.util.Crypto;

public class SesionWebService extends AsyncTask<PeticionWSEntity, Void, ResponseWebServiceEntity> {
    public WebServiceListener webServiceListener = null;

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> mensajes = new ArrayList<MensajeEntity>();
    private List<String> mensajeErrorList = new ArrayList<String>();
    private List<String> mensajeDebugList = new ArrayList<String>();

    // Manejador de los errores
    private ErrorManager errorManager = new ErrorManager();

    @Override
    protected ResponseWebServiceEntity doInBackground(PeticionWSEntity... peticion) {
        // Inicializa la respuesta del Web Service
        ResponseEntity<ResponseWebServiceEntity> responseEntity = null;

        // Inicializa la respuesta de error
        ResponseWebServiceEntity responseWebServiceError = new ResponseWebServiceEntity();

        try {
            ObjectMapper mapper = new ObjectMapper();
            final String jsonCredenciales = mapper.writeValueAsString(peticion[0].getCredencialesEntity());
            //final String jsonPet = mapper.writeValueAsString(peticion[0].getCredencialesEntity());
            //System.out.println(jsonPet);

            // Instancia para recuperar las constantes
            MainConstant mainConstant = new MainConstant();

            // Instancia para cifrar cadenas
            Crypto crypto = new Crypto();

            // Cifra la cadena JSON que sera enviada a traves de la URL
            String apiKey = crypto.encryptMessage(jsonCredenciales, mainConstant.getPasswordCrypto());

            // Construye la URL del Web Service a consultar
            String urlWs = mainConstant.getUrlWS() + "sesion?apiKey=" + URLEncoder.encode(apiKey, "UTF-8");
            //System.out.println(urlWs);

            // Construye las cabeceras HTTP
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            // Construye la plantilla para la peticion REST
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Inicializa la entidad que sera enviada como peticion al Web Service
            HttpEntity requestEntity = null;

            // Construye y envia la peticion de acuerdo al metodo indicado por el usuario
            // Regresa un arreglo de tipo MensajeEntitie que contendra los posibles errores
            switch (peticion[0].getMetodo()) {
                case "get":
                    requestEntity = new HttpEntity<>(httpHeaders);
                    responseEntity = restTemplate.exchange(urlWs, HttpMethod.GET, requestEntity, ResponseWebServiceEntity.class);
                    break;
                case "delete":
                    requestEntity = new HttpEntity<>(httpHeaders);
                    responseEntity = restTemplate.exchange(urlWs, HttpMethod.DELETE, requestEntity, ResponseWebServiceEntity.class);
                    break;
                default:
                    throw new Exception("No se ha especificado un método válido.");
            }

            // Regresa el contenido de la respuesta del Web Service
            return responseEntity.getBody();
        } catch (JsonProcessingException jsonEx) {
            // Agrega el error a mostrar
            mensajeErrorList.add(0, "Error al construir la petición JSON");
            mensajeDebugList.add(0, jsonEx.getMessage());
        } catch (Exception ex) {
            // Agrega el error a mostrar
            mensajeErrorList.add(0, "Error al consultar el Web Service");
            mensajeDebugList.add(0, ex.getMessage());
        } finally {
            if (mensajeErrorList.size() > 0) {
                mensajes = errorManager.createMensajesList(mensajeErrorList, mensajeDebugList);
                responseWebServiceError.setListaMensajes(mensajes);
            }
        }

        return responseWebServiceError;
    }

    @Override
    protected void onPostExecute(ResponseWebServiceEntity responseWebServiceEntity) {
        super.onPostExecute(responseWebServiceEntity);
        webServiceListener.onCommunicationFinish(responseWebServiceEntity);
    }
}
