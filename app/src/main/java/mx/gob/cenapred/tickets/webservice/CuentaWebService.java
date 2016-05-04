package mx.gob.cenapred.tickets.webservice;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;

public class CuentaWebService extends AsyncTask<PeticionWSEntity, Void, ResponseWebServiceEntity> {
    public WebServiceListener webServiceListener = null;

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> mensajes = new ArrayList<MensajeEntity>();
    private List<String> messageTypeList = new ArrayList<String>();
    private List<String> messageTitleList = new ArrayList<String>();
    private List<String> messageDescriptionList = new ArrayList<String>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    @Override
    protected ResponseWebServiceEntity doInBackground(PeticionWSEntity... peticion) {
        // Inicializa la respuesta del Web Service
        ResponseEntity<ResponseWebServiceEntity> responseEntity = null;

        // Inicializa la respuesta de error
        ResponseWebServiceEntity responseWebServiceError = new ResponseWebServiceEntity();

        try {
            // Construye la URL del Web Service a consultar
            String urlWs = MainConstant.URL_WS + "cuenta?email=" + URLEncoder.encode(peticion[0].getEmail(), "UTF-8") + "&type=" + peticion[0].getTipo();

            // Construye las cabeceras HTTP
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setReadTimeout(2000); // 2 segundos

            // Construye la plantilla para la peticion REST
            RestTemplate restTemplate = new RestTemplate(requestFactory);
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
                default:
                    throw new Exception(MainConstant.MESSAGE_DESCRIPTION_WS_NO_VALID_METHOD);
            }

            // Regresa el contenido de la respuesta del Web Service
            return responseEntity.getBody();
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_WS_COMMUNICATION_FAIL);
            messageDescriptionList.add(0, ex.getMessage());
        } finally {
            if (messageTitleList.size() > 0) {
                mensajes = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
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
