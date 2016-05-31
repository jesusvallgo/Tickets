package mx.gob.cenapred.tickets.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.adapter.StadisticsCustomFilterAdapter;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.CustomFilterItemEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.DatePickerManager;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;

public class StadisticsCustomFragment extends Fragment {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Contenedor de datos del Bundle
    private BundleEntity bundleEntity = new BundleEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageTitleList = new ArrayList<>();
    private List<String> messageDescriptionList = new ArrayList<>();

    // Mapea los elementos del Fragment

    // Inicializa las variables del Fragment
    private String alertAction = AppPreference.ALERT_ACTION_DEFAULT;
    private String apiKey = "";

    TextView txvDate;
    String dateSelected;
    final Calendar calendar = Calendar.getInstance();
    Integer year = calendar.get(Calendar.YEAR);
    Integer month = calendar.get(Calendar.MONTH);
    Integer day = calendar.get(Calendar.DAY_OF_MONTH);

    // Constructor por default
    public StadisticsCustomFragment() {

    }

    // Generador de instancia de Fragment
    public static StadisticsCustomFragment newInstance() {
        StadisticsCustomFragment fragment = new StadisticsCustomFragment();
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
        rootView = inflater.inflate(R.layout.fragment_stadistics_custom, container, false);

        final ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.stadistics_custom_exp_lsv);
        StadisticsCustomFilterAdapter stadisticsCustomFilterAdapter = new StadisticsCustomFilterAdapter(getActivity());

        final DatePickerManager datePickerDialog = new DatePickerManager(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateSelected = String.valueOf(year) + "-";
                        if (monthOfYear<10){
                            dateSelected += "0";
                        }
                        dateSelected += String.valueOf(monthOfYear) + "-";
                        if (dayOfMonth<10) {
                            dateSelected += "0";
                        }
                        dateSelected += String.valueOf(dayOfMonth);

                        txvDate.setText(dateSelected);
                    }
                },
                year, month, day);


        expandableListView.setAdapter(stadisticsCustomFilterAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CustomFilterItemEntity item = (CustomFilterItemEntity) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                switch (item.getType()) {
                    case "DatePicker":
                        txvDate = (TextView) v.findViewById(R.id.item_value);
                        String[] dateArray = txvDate.getText().toString().split("-");
                        if( dateArray.length == 3 ){
                            year = Integer.parseInt(dateArray[0]);
                            month = Integer.parseInt(dateArray[1]);
                            day = Integer.parseInt(dateArray[2]);
                            datePickerDialog.updateDate(year, month, day);
                        }

                        datePickerDialog.show();
                        break;
                }

                return true;
            }
        });


        /*
        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Mapea los elementos del Fragment

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        try {
            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el ApiKey
            validaCadenaUtil.validarApiKey(apiKey);
        }  catch (NoUserLoginException nulEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_SESSION);
            messageDescriptionList.add(nulEx.getMessage());
        }

        if( messageTitleList.size()>0 ){
            // Indica que debe regresar al Fragment anterior
            alertAction = AppPreference.ALERT_ACTION_GOBACK;

            this.displayMessage(messageTypeList,messageTitleList,messageDescriptionList);
        }
        */

        return rootView;
    }

    private void displayMessage(List<String> messageTypeList, List<String> messageTitleList, List<String> messageDescriptionList) {
        // Crea la lista de errores
        messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);

        // Despliega los errores encontrados
        messagesManager.displayMessage(getActivity(), getContext(), messagesList, alertAction);
    }
}
