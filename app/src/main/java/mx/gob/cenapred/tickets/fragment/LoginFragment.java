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

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.entity.TokenGCMEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.ErrorManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.webservice.SesionWebService;

public class LoginFragment extends Fragment implements WebServiceListener {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

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
    private List<String> messageErrorList = new ArrayList<String>();
    private List<String> messageDebugList = new ArrayList<String>();

    // Manejador de los errores
    private ErrorManager errorManager = new ErrorManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private LoginFragment loginFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    private EditText loginEdtUsername, loginEdtPassword;
    private Button loginBtnSend;

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

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Mapea los elementos del Fragment
        loginEdtUsername = (EditText) rootView.findViewById(R.id.login_edt_username);
        loginEdtPassword = (EditText) rootView.findViewById(R.id.login_edt_password);
        loginBtnSend = (Button) rootView.findViewById(R.id.login_btn_send);

        // Agrega el token del dispositivo a su entidad correspondiente
        tokenGCMEntity.setTokenDispositivo(appPreferencesManager.getDeviceToken());

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
        autoFocus();
    }

    private void tryLogin() {
        try {
            // Oculta las opciones del Fragment
            layoutOptions.setVisibility(View.GONE);

            // Muestra el layout de Cargando
            layoutLoading.setVisibility(View.VISIBLE);

            // Construye los campos necesarios de la Entidad Credenciales
            credencialesEntity.setUsername(loginEdtUsername.getText().toString().trim());
            credencialesEntity.setPassword(loginEdtPassword.getText().toString());

            // Construye la peticion
            peticionWSEntity.setMetodo("put");
            peticionWSEntity.setAccion("add");
            peticionWSEntity.setCredencialesEntity(credencialesEntity);
            peticionWSEntity.setTokenGCMEntity(tokenGCMEntity);

            // Llamada al cliente para validar credenciales y loguearse
            SesionWebService sesionWebService = new SesionWebService();
            sesionWebService.webServiceListener = loginFragment;
            sesionWebService.execute(peticionWSEntity);
        } catch (Exception ex) {
            // Limpia las listas de error
            messageErrorList.clear();
            messageDebugList.clear();

            // Agrega el error a mostrar
            messageErrorList.add(0, "Error al realizar la petición al Web Service");
            messageDebugList.add(0, ex.getMessage());
        } finally {
            if (messageErrorList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = errorManager.createMensajesList(messageErrorList, messageDebugList);
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
            errorManager.displayError(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_DEFAULT);

            // Oculta el layout de Cargando
            layoutLoading.setVisibility(View.GONE);

            // Muestra las opciones del Fragment
            layoutOptions.setVisibility(View.VISIBLE);

            // Selecciona el EditText necesario
            autoFocus();
        } else if (responseWebServiceEntity.getEmpleado() != null) {
            // Almacena las credenciales de usuario en el dispositivo
            AppPreferencesManager appPreferencesManager = new AppPreferencesManager(getContext());
            appPreferencesManager.saveCredentials(credencialesEntity);
            appPreferencesManager.saveUserData(responseWebServiceEntity.getEmpleado());

            // Redirige al Fragment de bienvenida
            ((MainActivity) getActivity()).manageFragment(R.id.nav_welcome, null);
        }
    }
}
