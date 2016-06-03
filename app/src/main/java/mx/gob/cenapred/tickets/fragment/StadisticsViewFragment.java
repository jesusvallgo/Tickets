package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.adapter.ReporteItemAdapter;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;
import mx.gob.cenapred.tickets.webservice.ListadoWebService;

public class StadisticsViewFragment extends Fragment implements WebServiceListener {
    // **************************** Constantes ****************************

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase ResponseWebServiceEntity
    private ResponseWebServiceEntity responseWebServiceEntity = new ResponseWebServiceEntity();

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
    private StadisticsViewFragment stadisticsViewFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    ListView stadisticsViewLsvItem;

    // Variables del fragment
    private MenuItem menuItemCount;
    private String apiKey = "";
    private Integer numReport = 0;
    String filter = "";

    // Constructor por default
    public StadisticsViewFragment() {

    }

    // Generador de instancia de Fragment
    public static StadisticsViewFragment newInstance() {
        StadisticsViewFragment fragment = new StadisticsViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = getArguments().getString("filter");

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_stadistics_view, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Mapea los elementos del Fragment
        stadisticsViewLsvItem = (ListView) rootView.findViewById(R.id.stadistics_view_lsv_item);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        tryRefresh();

        return rootView;
    }

    private void tryRefresh() {
        try {
            // Muestra el layout de Cargando
            layoutLoading.setVisibility(View.VISIBLE);

            // Oculta el layout de opciones del Fragment
            layoutOptions.setVisibility(View.GONE);

            // Destruye el menu de opciones del Fragment
            setHasOptionsMenu(false);

            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el APIKEY
            validaCadenaUtil.validarApiKey(apiKey);

            // Construye la peticion
            peticionWSEntity.setMetodo("get");
            peticionWSEntity.setTipo("stadistics");
            peticionWSEntity.setFiltro(filter);
            peticionWSEntity.setApiKey(apiKey);

            // Llamada al cliente para consultar los detalles del reporte
            ListadoWebService listadoWebService = new ListadoWebService();
            listadoWebService.webServiceListener = stadisticsViewFragment;
            listadoWebService.execute(peticionWSEntity);
        } catch (NoUserLoginException nulEx) {
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_SESSION);
            messageDescriptionList.add(nulEx.getMessage());
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add("Error al realizar la petición al Web Service");
            messageDescriptionList.add(ex.getMessage());
        } finally {
            if (messageTitleList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
                responseWebServiceEntity = new ResponseWebServiceEntity();
                responseWebServiceEntity.setListaMensajes(messagesList);

                // Llama al metodo que procesa la respuesta
                onCommunicationFinish(responseWebServiceEntity);
            }
        }
    }

    // Metodo que procesa la respuesta del WebService
    @Override
    public void onCommunicationFinish(ResponseWebServiceEntity responseWebServiceEntity) {
        // Oculta el layout de Cargando
        layoutLoading.setVisibility(View.GONE);

        if (responseWebServiceEntity.getListaMensajes() != null) {
            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_GOBACK);
        } else if (responseWebServiceEntity.getListaReporte() != null && responseWebServiceEntity.getListaReporte().size() > 0) {
            numReport = responseWebServiceEntity.getListaReporte().size();

            // Convierte la lista en un arreglo
            ReporteEntity[] arrayReporte = new ReporteEntity[responseWebServiceEntity.getListaReporte().size()];
            responseWebServiceEntity.getListaReporte().toArray(arrayReporte);

            // Genera el adaptador
            ReporteItemAdapter reporteItemAdapter = new ReporteItemAdapter(getActivity(), R.layout.layout_custom_listview_report, arrayReporte, true);

            // Carga los errores en el cuerpo del cuadro de dialogo
            stadisticsViewLsvItem.setAdapter(reporteItemAdapter);
        } else {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NOTIFICATION);
            messageDescriptionList.add(MainConstant.MESSAGE_DESCRIPTION_NO_PENDINDG_REPORT);

            // Si existen errores genera la estructura adecuada
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
            responseWebServiceEntity = new ResponseWebServiceEntity();
            responseWebServiceEntity.setListaMensajes(messagesList);

            // Llama al metodo que procesa la respuesta
            onCommunicationFinish(responseWebServiceEntity);
        }

        // Muestra las opciones del Fragment
        layoutOptions.setVisibility(View.VISIBLE);

        // Indica que el Fragment cuenta con menu de opciones
        setHasOptionsMenu(true);
    }

    @Override
    public void communicationStatus(Boolean running) {
        ((MainActivity) getActivity()).asyncTaskRunning = running;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.report_count_menu, menu);

        // Mapea el item del menu
        menuItemCount = menu.findItem(R.id.item_count);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menuItemCount.setTitle(numReport.toString());
        menuItemCount.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_refresh:
                tryRefresh();
                break;
            default:
                break;
        }

        return true;
    }
}