package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MenuManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;

public class WelcomeFragment extends Fragment implements View.OnClickListener {
    // **************************** Constantes ****************************

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    AppPreferencesManager appPreferencesManager;

    // Manejador del menu de usuario
    MenuManager menuManager = new MenuManager();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageTitleList = new ArrayList<>();
    private List<String> messageDescriptionList = new ArrayList<>();

    // Elementos del Fragment
    TabHost welcomeTabHost;
    LinearLayout btnTicketTechnicalSupport, btnTicketDevelopment, btnTicketNetworking, btnMyTicketPending, btnSearchTicketNumber, btnRequestPending, btnStadisticsGeneral;
    Integer indexTab = 0;
    private String apiKey = "";

    // Constructor por default
    public WelcomeFragment() {

    }

    // Generador de instancia de Fragment
    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
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
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        // Mapea los elementos del fragment
        welcomeTabHost = (TabHost) rootView.findViewById(R.id.welcomeTabHost);
        btnTicketTechnicalSupport = (LinearLayout) rootView.findViewById(R.id.welcome_btn_ticket_technical_support);
        btnTicketDevelopment = (LinearLayout) rootView.findViewById(R.id.welcome_btn_ticket_development);
        btnTicketNetworking = (LinearLayout) rootView.findViewById(R.id.welcome_btn_ticket_networking);
        btnMyTicketPending = (LinearLayout) rootView.findViewById(R.id.welcome_btn_my_ticket_pending);
        btnSearchTicketNumber = (LinearLayout) rootView.findViewById(R.id.welcome_btn_search_ticket_number);
        btnRequestPending = (LinearLayout) rootView.findViewById(R.id.welcome_btn_request_pending);
        btnStadisticsGeneral = (LinearLayout) rootView.findViewById(R.id.welcome_btn_stadistics_general);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        try {
            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el ApiKey
            validaCadenaUtil.validarApiKey(apiKey);

            // Agrega los Tabs y botones necesarios
            menuManager.updateWelcomeTab(getActivity(), rootView, appPreferencesManager.getUserRoleId(), indexTab);
        }  catch (NoUserLoginException nulEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_SESSION);
            messageDescriptionList.add(nulEx.getMessage());
        } catch (NoInputDataException nidEx){
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_SESSION);
            messageDescriptionList.add(nidEx.getMessage());
        }

        if( messageTitleList.size()>0 ){
            // Crea la lista de errores
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);

            // Despliega los errores encontrados
            messagesManager.displayMessage(getActivity(), getContext(), messagesList, AppPreference.ALERT_ACTION_FINISH);
        }

        // Agrega el evento onClick a los elementos del Fragment
        btnTicketTechnicalSupport.setOnClickListener(this);
        btnTicketDevelopment.setOnClickListener(this);
        btnTicketNetworking.setOnClickListener(this);
        btnMyTicketPending.setOnClickListener(this);
        btnSearchTicketNumber.setOnClickListener(this);
        btnRequestPending.setOnClickListener(this);
        btnStadisticsGeneral.setOnClickListener(this);

        return rootView;
    }

    // Metodo onClick para los elementos del Fragmnet
    @Override
    public void onClick(View v) {
        // Almacena el Tab seleccionado
        indexTab = welcomeTabHost.getCurrentTab();

        // Llama al metodo que manipula las opciones presionadas
        ((MainActivity) getActivity()).manageFragment(v.getId(), null);
    }
}
