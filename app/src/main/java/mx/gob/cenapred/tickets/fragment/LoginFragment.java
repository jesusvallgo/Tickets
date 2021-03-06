package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.entity.TokenGCMEntity;
import mx.gob.cenapred.tickets.exception.BadInputDataException;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.util.ApiKeyUtil;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;
import mx.gob.cenapred.tickets.webservice.SesionWebService;

public class LoginFragment extends Fragment implements WebServiceListener, View.OnClickListener {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase de CredencialesEntity
    private CredencialesEntity credencialesEntity = new CredencialesEntity();

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
    private LoginFragment loginFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    private EditText loginEdtUsername, loginEdtPassword;
    private TextView loginTxvRegister, loginTxvRecoverPassword;
    private Button loginBtnSend;

    // Variables del fragment
    private String username = "", password = "", apiKey = "";

    // Constructor por default
    public LoginFragment() {

    }

    // Generador de instancia de Fragment
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Mapea los elementos del Fragment
        loginEdtUsername = (EditText) rootView.findViewById(R.id.login_edt_username);
        loginEdtPassword = (EditText) rootView.findViewById(R.id.login_edt_password);
        loginTxvRegister = (TextView) rootView.findViewById(R.id.login_txv_register);
        loginTxvRecoverPassword = (TextView) rootView.findViewById(R.id.login_txv_recoverPassword);
        loginBtnSend = (Button) rootView.findViewById(R.id.login_btn_send);

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Limpia las credenciales de usuario del dispositivo
        appPreferencesManager.clearCredentials();

        // Agrega el token del dispositivo a su entidad correspondiente
        tokenGCMEntity.setTokenDispositivo(appPreferencesManager.getDeviceToken());

        loginTxvRegister.setOnClickListener(this);
        loginTxvRecoverPassword.setOnClickListener(this);

        loginEdtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    tryLogin();
                }
                return false;
            }
        });

        loginBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        // Selecciona el EditText necesario
        //autoFocus();
    }

    private void tryLogin() {
        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();

        try {
            // Oculta el teclado
            keyboardManager.hideSoftKeyboard(getActivity());

            // Recupera el valor limpio del campo E-mail
            username = loginEdtUsername.getText().toString().trim();

            // Recupera el valor del campo password
            password = loginEdtPassword.getText().toString();

            // Valida si el contenido del edittext es un usuario valido
            validaCadenaUtil.validarUsuario(username);

            // Valida si el contenido del edittext es un password valido
            validaCadenaUtil.validarPassword(password);

            // Construye los campos necesarios de la Entidad Credenciales
            credencialesEntity.setUsername(username);
            credencialesEntity.setPassword(password);

            // Instancia para generar el APIKEY
            ApiKeyUtil apiKeyUtil = new ApiKeyUtil();
            apiKey = apiKeyUtil.createApiKey(credencialesEntity);
            validaCadenaUtil.validarApiKey(apiKey);

            // Oculta las opciones del Fragment
            layoutOptions.setVisibility(View.GONE);

            // Muestra el layout de Cargando
            layoutLoading.setVisibility(View.VISIBLE);

            // Construye la peticion
            peticionWSEntity.setMetodo("put");
            peticionWSEntity.setAccion("add");
            peticionWSEntity.setApiKey(apiKey);
            peticionWSEntity.setTokenGCMEntity(tokenGCMEntity);

            // Llamada al cliente para validar credenciales y loguearse
            SesionWebService sesionWebService = new SesionWebService();
            sesionWebService.webServiceListener = loginFragment;
            sesionWebService.execute(peticionWSEntity);
        } catch (JsonProcessingException jsonEx){
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_BUILD_JSON_FAIL);
            messageDescriptionList.add(jsonEx.getMessage());
        } catch (NoUserLoginException nulEx){
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_SESSION);
            messageDescriptionList.add(nulEx.getMessage());
        } catch (NoInputDataException nidEx){
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_INPUT_DATA);
            messageDescriptionList.add(nidEx.getMessage());
        } catch (BadInputDataException bidEx) {
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_BAD_INPUT_DATA);
            messageDescriptionList.add(bidEx.getMessage());
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_WS_REQUEST_FAIL);
            messageDescriptionList.add(ex.getMessage());
        } finally {
            if (messageTitleList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
                ResponseWebServiceEntity responseWebServiceEntity = new ResponseWebServiceEntity();
                responseWebServiceEntity.setListaMensajes(messagesList);

                // Llama al metodo que procesa la respuesta
                onCommunicationFinish(responseWebServiceEntity);
            }
        }
    }

    // Metodo para determinar el EditText que debe estar seleccionado
    private void autoFocus() {
        // Inicializa la variable auxiliar
        EditText focusEditText;

        // Determina el EditText adecuado
        if (loginEdtUsername.getText().length() == 0) {
            focusEditText = loginEdtUsername;
        } else {
            focusEditText = loginEdtPassword;
        }

        // Establece el foco en el EditText adecuado
        focusEditText.requestFocus();

        // Carga el teclado para el EditText indicado
        keyboardManager.showSoftKeyboard(focusEditText, getActivity());
    }

    // Metodo que procesa la respuesta del WebService
    @Override
    public void onCommunicationFinish(ResponseWebServiceEntity responseWebServiceEntity) {
        if (responseWebServiceEntity.getListaMensajes() != null) {
            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_DEFAULT);

            // Oculta el layout de Cargando
            layoutLoading.setVisibility(View.GONE);

            // Muestra las opciones del Fragment
            layoutOptions.setVisibility(View.VISIBLE);

            // Selecciona el EditText necesario
            autoFocus();
        } else if (responseWebServiceEntity.getEmpleado() != null) {
            // Almacena las credenciales de usuario en el dispositivo
            AppPreferencesManager appPreferencesManager = new AppPreferencesManager(getContext());
            appPreferencesManager.saveCredentials(apiKey);
            appPreferencesManager.saveUserData(responseWebServiceEntity.getEmpleado());

            // Redirige al Fragment de bienvenida
            ((MainActivity) getActivity()).manageFragment(R.id.nav_welcome, null);
        }
    }

    @Override
    public void communicationStatus(Boolean running) {
        ((MainActivity) getActivity()).asyncTaskRunning = running;
    }

    @Override
    public void onClick(View view) {
        ((MainActivity) getActivity()).manageFragment(view.getId(),null);
    }
}