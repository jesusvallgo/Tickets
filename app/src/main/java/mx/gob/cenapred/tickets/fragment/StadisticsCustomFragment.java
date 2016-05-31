package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.adapter.StadisticsCustomFilterAdapter;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.CustomFilterItemEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
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

        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.stadistics_custom_exp_lsv);
        expandableListView.setAdapter(new StadisticsCustomFilterAdapter(getActivity()));
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CustomFilterItemEntity item = (CustomFilterItemEntity) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);
                Toast.makeText(getContext(), "Clic en " + item.getLabel(), Toast.LENGTH_SHORT).show();
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

    private void displayMessage(List<String> messageTypeList, List<String> messageTitleList, List<String> messageDescriptionList){
        // Crea la lista de errores
        messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);

        // Despliega los errores encontrados
        messagesManager.displayMessage(getActivity(), getContext(), messagesList, alertAction);
    }
}
