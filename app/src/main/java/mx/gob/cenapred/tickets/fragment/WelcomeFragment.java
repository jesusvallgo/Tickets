package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    Button btnChoseGroups, btnTicketTechnicalSupport, btnTicketDevelopers, btnTicketNetworking, btnTicketPending, btnTicketNumber, btnStadisticsDate, btnStadisticsPending;

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

        // Llama al metodo que establece las opciones que puede realizar el usuario logueado
        menuManager.updateWelcomeOptions(rootView, appPreferencesManager);

        // Mapea los elementos del fragment
        btnChoseGroups = (Button) rootView.findViewById(R.id.welcome_btn_chose_groups);
        btnTicketTechnicalSupport = (Button) rootView.findViewById(R.id.welcome_btn_ticket_technical_support);
        btnTicketDevelopers = (Button) rootView.findViewById(R.id.welcome_btn_ticket_developers);
        btnTicketNetworking = (Button) rootView.findViewById(R.id.welcome_btn_ticket_networking);
        btnTicketPending = (Button) rootView.findViewById(R.id.welcome_btn_ticket_pending);
        btnTicketNumber = (Button) rootView.findViewById(R.id.welcome_btn_ticket_number);
        btnStadisticsDate = (Button) rootView.findViewById(R.id.welcome_btn_stadistics_date);
        btnStadisticsPending = (Button) rootView.findViewById(R.id.welcome_btn_stadistics_pending);

        // Agrega una serie de espacios a los elementos del Fragment para su presentacion
        btnChoseGroups.setText(getString(R.string.welcome_btn_space) + btnChoseGroups.getText().toString());
        btnTicketTechnicalSupport.setText(getString(R.string.welcome_btn_space) + btnTicketTechnicalSupport.getText().toString());
        btnTicketDevelopers.setText(getString(R.string.welcome_btn_space) + btnTicketDevelopers.getText().toString());
        btnTicketPending.setText(getString(R.string.welcome_btn_space) + btnTicketPending.getText().toString());
        btnTicketNumber.setText(getString(R.string.welcome_btn_space) + btnTicketNumber.getText().toString());
        btnStadisticsDate.setText(getString(R.string.welcome_btn_space) + btnStadisticsDate.getText().toString());
        btnStadisticsPending.setText(getString(R.string.welcome_btn_space) + btnStadisticsPending.getText().toString());

        // Agrega el evento onClick a los elementos del Fragment
        btnChoseGroups.setOnClickListener(this);
        btnTicketTechnicalSupport.setOnClickListener(this);
        btnTicketDevelopers.setOnClickListener(this);
        btnTicketNetworking.setOnClickListener(this);
        btnTicketPending.setOnClickListener(this);
        btnTicketNumber.setOnClickListener(this);
        btnStadisticsDate.setOnClickListener(this);
        btnStadisticsPending.setOnClickListener(this);

        return rootView;
    }

    // Metodo onClick para los elementos del Fragmnet
    @Override
    public void onClick(View v) {
        // Llama al metodo que manipula las opciones presionadas
        ((MainActivity) getActivity()).manageFragment(v.getId(), null);
    }
}
