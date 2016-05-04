package mx.gob.cenapred.tickets.webservice;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;

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
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;

public class ReporteWebService extends AsyncTask<PeticionWSEntity, Void, ResponseWebServiceEntity> {
    public WebServiceListener webServiceListener = null;

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> mensajes = new ArrayList<>();
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageTitleList = new ArrayList<>();
    private List<String> messageDescriptionList = new ArrayList<>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    @Override
    protected void onPreExecute(){
        webServiceListener.communicationStatus(Boolean.TRUE);
    }

    @Override
    protected ResponseWebServiceEntity doInBackground(PeticionWSEntity... peticion) {
        // Inicializa la respuesta del Web Service
        ResponseEntity<ResponseWebServiceEntity> responseEntity;

        // Inicializa la respuesta de error
        ResponseWebServiceEntity responseWebServiceError = new ResponseWebServiceEntity();

        try {
            // Construye la URL del Web Service a consultar
            String urlWs = MainConstant.URL_WS + "reporte?apiKey=" + URLEncoder.encode(peticion[0].getApiKey(), "UTF-8");

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
            HttpEntity<ReporteEntity> requestEntity;

            // Construye y envia la peticion de acuerdo al metodo indicado por el usuario
            // Regresa un arreglo de tipo MensajeEntitie que contendra los posibles errores
            switch (peticion[0].getMetodo()) {
                case "get":
                    urlWs += "&idReport=" + peticion[0].getReporteEntity().getIdReporte();
                    requestEntity = new HttpEntity<>(httpHeaders);
                    responseEntity = restTemplate.exchange(urlWs, HttpMethod.GET, requestEntity, ResponseWebServiceEntity.class);
                    break;
                case "put":
                    urlWs += "&action=" + peticion[0].getAccion();
                    requestEntity = new HttpEntity<>(peticion[0].getReporteEntity(), httpHeaders);
                    responseEntity = restTemplate.exchange(urlWs, HttpMethod.PUT, requestEntity, ResponseWebServiceEntity.class);
                    break;
                case "post":
                    requestEntity = new HttpEntity<>(peticion[0].getReporteEntity(), httpHeaders);
                    responseEntity = restTemplate.exchange(urlWs, HttpMethod.POST, requestEntity, ResponseWebServiceEntity.class);
                    break;
                default:
                    throw new Exception("No se ha especificado un método válido.");
            }

            // Regresa el contenido de la respuesta del Web Service
            return responseEntity.getBody();
        } catch (JsonProcessingException jsonEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add("Error al construir la petición JSON");
            messageDescriptionList.add(jsonEx.getMessage());
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add("Error al consultar el Web Service");
            messageDescriptionList.add(ex.getMessage());
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
        webServiceListener.communicationStatus(Boolean.FALSE);
        webServiceListener.onCommunicationFinish(responseWebServiceEntity);
    }
}
