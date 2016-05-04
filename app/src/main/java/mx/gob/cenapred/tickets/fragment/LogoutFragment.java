package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.entity.TokenGCMEntity;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;
import mx.gob.cenapred.tickets.webservice.SesionWebService;

public class LogoutFragment extends Fragment implements WebServiceListener {
    // **************************** Constantes ****************************

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase de TokenGCMEntity
    private TokenGCMEntity tokenGCMEntity = new TokenGCMEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageTitleList = new ArrayList<>();
    private List<String> messageDescriptionList = new ArrayList<>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private LogoutFragment logoutFragment = this;

    // Variables del fragment
    private String apiKey = "";

    // Constructor por default
    public LogoutFragment() {

    }

    // Generador de instancia de Fragment
    public static LogoutFragment newInstance() {
        LogoutFragment fragment = new LogoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_loading, container, false);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        try {
            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el APIKEY
            validaCadenaUtil.validarApiKey(apiKey);

            // Agrega el token del dispositivo a su entidad correspondiente
            tokenGCMEntity.setTokenDispositivo(appPreferencesManager.getDeviceToken());

            // Construye la peticion
            peticionWSEntity.setMetodo("put");
            peticionWSEntity.setAccion("delete");
            peticionWSEntity.setApiKey(apiKey);
            peticionWSEntity.setTokenGCMEntity(tokenGCMEntity);

            // Llamada al cliente para validar credenciales y cerrar sesion
            SesionWebService sesionWebService = new SesionWebService();
            sesionWebService.webServiceListener = logoutFragment;
            sesionWebService.execute(peticionWSEntity);
        } catch (NoUserLoginException nulEx){
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_USER_LOGIN);
            messageDescriptionList.add(nulEx.getMessage());
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_WS_REQUEST_FAIL);
            messageDescriptionList.add(0, ex.getMessage());
        } finally {
            if (messageTitleList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = messagesManager.createMensajesList(messageTypeList,messageTitleList, messageDescriptionList);
                ResponseWebServiceEntity responseWebServiceEntity = new ResponseWebServiceEntity();
                responseWebServiceEntity.setListaMensajes(messagesList);

                // Llama al metodo que procesa la respuesta
                onCommunicationFinish(responseWebServiceEntity);
            }
        }

        return rootView;
    }

    @Override
    public void onCommunicationFinish(ResponseWebServiceEntity responseWebServiceEntity) {
        if (responseWebServiceEntity.getListaMensajes() != null) {
            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_GOBACK);
        } else {
            // Limpia las credenciales de usuario del dispositivo
            AppPreferencesManager appPreferencesManager = new AppPreferencesManager(getContext());
            appPreferencesManager.clearCredentials();

            // Redirige al Fragment de login
            ((MainActivity) getActivity()).manageFragment(R.id.fragment_login, null);
        }
    }
}
