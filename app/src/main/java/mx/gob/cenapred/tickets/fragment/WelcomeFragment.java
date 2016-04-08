package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MenuManager;

public class WelcomeFragment extends Fragment implements View.OnClickListener {
    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Manejador de las preferencias de la aplicacion
    AppPreferencesManager appPreferencesManager;

    // Manejador del menu de usuario
    MenuManager menuManager = new MenuManager();

    // Elementos del Fragment
    LinearLayout btnTicketTechnicalSupport, btnTicketDevelopers, btnTicketNetworking, btnMyTicketPending, btnSearchTicketNumber, btnRequestPending, btnStadisticsGeneral;

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

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Agrega los Tabs y botones necesarios
        menuManager.updateWelcomeTab(getActivity(), rootView, appPreferencesManager.getUserRole());

        // Mapea los elementos del fragment
        btnTicketTechnicalSupport = (LinearLayout) rootView.findViewById(R.id.welcome_btn_ticket_technical_support);
        btnTicketDevelopers = (LinearLayout) rootView.findViewById(R.id.welcome_btn_ticket_developers);
        btnTicketNetworking = (LinearLayout) rootView.findViewById(R.id.welcome_btn_ticket_networking);
        btnMyTicketPending = (LinearLayout) rootView.findViewById(R.id.welcome_btn_my_ticket_pending);
        btnSearchTicketNumber = (LinearLayout) rootView.findViewById(R.id.welcome_btn_search_ticket_number);
        btnRequestPending = (LinearLayout) rootView.findViewById(R.id.welcome_btn_request_pending);
        btnStadisticsGeneral = (LinearLayout) rootView.findViewById(R.id.welcome_btn_stadistics_general);

        // Agrega el evento onClick a los elementos del Fragment
        btnTicketTechnicalSupport.setOnClickListener(this);
        btnTicketDevelopers.setOnClickListener(this);
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
        // Llama al metodo que manipula las opciones presionadas
        ((MainActivity) getActivity()).manageFragment(v.getId(), null);
    }
}
