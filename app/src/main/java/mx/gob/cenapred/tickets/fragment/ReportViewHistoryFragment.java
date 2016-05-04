package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.adapter.BitacoraEntityAdapter;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;

public class ReportViewHistoryFragment extends Fragment {
    // **************************** Constantes ****************************

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Mapea los elementos del Fragment
    TextView reportHistoryTxvTitle;
    ListView reportHistoryLsvAction;

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageTitleList = new ArrayList<>();
    private List<String> messageDescriptionList = new ArrayList<>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Inicializa las variables del Fragment
    private List<BitacoraEntity> listHistoryAction = new ArrayList<BitacoraEntity>();
    private String apiKey = "";

    // Constructor por default
    public ReportViewHistoryFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportViewHistoryFragment newInstance() {
        ReportViewHistoryFragment fragment = new ReportViewHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listHistoryAction = (List<BitacoraEntity>) getArguments().getSerializable("listHistoryAction");
        getArguments().remove("listHistoryAction");

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_view_history, container, false);

        // Mapea los layouts del Fragment
        reportHistoryTxvTitle = (TextView) rootView.findViewById(R.id.report_view_history_txv_title);
        reportHistoryLsvAction = (ListView) rootView.findViewById(R.id.report_view_history_lsv_action);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        try {
            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el ApiKey
            validaCadenaUtil.validarApiKey(apiKey);

            // Obtiene el numero de acciones realizadas para el reporte especificado
            Integer numHistoryAction = listHistoryAction.size();

            // Determina si existen mensajes para desplegar
            if (numHistoryAction > 0) {
                // Establece el ID del reporte
                reportHistoryTxvTitle.append(" " + listHistoryAction.get(0).getIdReporte().toString());

                // Convierte la lista en un arreglo
                BitacoraEntity[] arrayHistoryAction = new BitacoraEntity[numHistoryAction];
                listHistoryAction.toArray(arrayHistoryAction);

                // Genera el adaptador
                BitacoraEntityAdapter adapter = new BitacoraEntityAdapter(getActivity(), R.layout.layout_custom_listview_history, arrayHistoryAction);

                // Carga los errores en el cuerpo del cuadro de dialogo
                reportHistoryLsvAction.setAdapter(adapter);
            } else {
                throw new NoInputDataException(MainConstant.MESSAGE_DESCRIPTION_NO_LIST_HISTORY);
            }
        } catch (NoUserLoginException nulEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_USER_LOGIN);
            messageDescriptionList.add(nulEx.getMessage());
        } catch (NoInputDataException nidEx){
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_INPUT_DATA);
            messageDescriptionList.add(nidEx.getMessage());
        }

        if( messageTitleList.size()>0 ){
            // Si existen errores genera la estructura adecuada
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
            messagesManager.displayMessage(getActivity(), getContext(), messagesList, AppPreference.ALERT_ACTION_GOBACK);
        }

        return rootView;
    }
}