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
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.webservice.CuentaWebService;

public class RecoverPasswordFragment extends Fragment implements WebServiceListener{
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de CredencialesEntity
    private CredencialesEntity credencialesEntity = new CredencialesEntity();

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageErrorList = new ArrayList<>();
    private List<String> messageDebugList = new ArrayList<>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private RecoverPasswordFragment recoverPasswordFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    private EditText loginEdtEmail;
    private Button loginBtnRequest;

    // Constructor por default
    public RecoverPasswordFragment() {

    }

    // Generador de instancia de Fragment
    public static RecoverPasswordFragment newInstance() {
        RecoverPasswordFragment fragment = new RecoverPasswordFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_register_recoverpassword, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Mapea los elementos del Fragment
        loginEdtEmail = (EditText) rootView.findViewById(R.id.login_edt_email);
        loginBtnRequest = (Button) rootView.findViewById(R.id.login_btn_request);

        loginEdtEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    tryConnect();
                }
                return false;
            }
        });

        loginBtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryConnect();
            }
        });

        return rootView;
    }

    private void tryConnect() {
        try {
            // Oculta el teclado
            keyboardManager.hideSoftKeyboard(getActivity());

            // Oculta las opciones del Fragment
            layoutOptions.setVisibility(View.GONE);

            // Muestra el layout de Cargando
            layoutLoading.setVisibility(View.VISIBLE);

            // Construye los campos necesarios de la Entidad Credenciales
            credencialesEntity.setUsername(loginEdtEmail.getText().toString().trim());

            // Construye la peticion
            peticionWSEntity.setMetodo("get");
            peticionWSEntity.setTipo("update");
            peticionWSEntity.setCredencialesEntity(credencialesEntity);

            // Llamada al cliente para validar credenciales y loguearse
            CuentaWebService cuentaWebService = new CuentaWebService();
            cuentaWebService.webServiceListener = recoverPasswordFragment;
            cuentaWebService.execute(peticionWSEntity);
        } catch (Exception ex) {
            // Limpia las listas de error
            messageTypeList.clear();
            messageErrorList.clear();
            messageDebugList.clear();

            // Agrega el error a mostrar
            messageTypeList.add(0, AppPreference.MESSAGE_ERROR);
            messageErrorList.add(0, getString(R.string.general_message_title_ws_request_fail));
            messageDebugList.add(0, ex.getMessage());
        } finally {
            if (messageErrorList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = messagesManager.createMensajesList(messageTypeList, messageErrorList, messageDebugList);
                ResponseWebServiceEntity responseWebServiceEntity = new ResponseWebServiceEntity();
                responseWebServiceEntity.setListaMensajes(messagesList);

                // Llama al metodo que procesa la respuesta
                onCommunicationFinish(responseWebServiceEntity);
            }
        }
    }

    @Override
    public void onCommunicationFinish(ResponseWebServiceEntity responseWebServiceEntity) {
        if (responseWebServiceEntity.getListaMensajes() != null) {
            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_DEFAULT);
        } else {
            messageTypeList.add(0, AppPreference.MESSAGE_SUCCESS);
            messageErrorList.add(0, "El correo electrónico se envio correctamente");
            messageDebugList.add(0, "Siga las instrucciones para actualizar su contraseña");
            messagesList = messagesManager.createMensajesList(messageTypeList, messageErrorList, messageDebugList);
            responseWebServiceEntity.setListaMensajes(messagesList);

            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_GOBACK);
        }

        // Oculta el layout de Cargando
        layoutLoading.setVisibility(View.GONE);

        // Muestra las opciones del Fragment
        layoutOptions.setVisibility(View.VISIBLE);
    }
}