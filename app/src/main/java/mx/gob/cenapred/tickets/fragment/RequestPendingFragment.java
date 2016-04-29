package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.adapter.ReporteItemAdapter;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.webservice.ListadoWebService;

public class RequestPendingFragment extends Fragment implements WebServiceListener {
    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase ResponseWebServiceEntity
    private ResponseWebServiceEntity responseWebServiceEntity = new ResponseWebServiceEntity();

    // Lista Cache de Reportes
    private List<ReporteEntity> listaReporteCache = new ArrayList<ReporteEntity>();

    // Instancia a la clase para especificar las credenciales de usuario
    private CredencialesEntity credencialesEntity = new CredencialesEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<String>();
    private List<String> messageTitleList = new ArrayList<String>();
    private List<String> messageDescriptionList = new ArrayList<String>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private RequestPendingFragment requestPendingFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    ListView requestPendingLsvItem;

    // Constructor por default
    public RequestPendingFragment() {

    }

    // Generador de instancia de Fragment
    public static RequestPendingFragment newInstance() {
        RequestPendingFragment fragment = new RequestPendingFragment();
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
        rootView = inflater.inflate(R.layout.fragment_request_pending, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Mapea los elementos del Fragment
        requestPendingLsvItem = (ListView) rootView.findViewById(R.id.request_pending_lsv_item);

        requestPendingLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReporteEntity item = (ReporteEntity) requestPendingLsvItem.getItemAtPosition(position);
                BundleEntity bundleEntity = new BundleEntity();
                bundleEntity.setIdReportBundle(item.getIdReporte());
                bundleEntity.setAddToBackStack(true);
                ((MainActivity) getActivity()).manageFragment(R.id.fragment_report_detail, bundleEntity);
            }
        });

        if( listaReporteCache != null && listaReporteCache.size()>0 ){
            responseWebServiceEntity.setListaReporte(listaReporteCache);
            onCommunicationFinish(responseWebServiceEntity);
        } else {
            // Manejador de los datos de la sesion de usuario
            appPreferencesManager = new AppPreferencesManager(getContext());

            // Construye los campos necesarios de la Entidad Credenciales
            credencialesEntity.setUsername(appPreferencesManager.getUserLogin());
            credencialesEntity.setPassword(appPreferencesManager.getUserPassword());

            try {
                // Construye la peticion
                peticionWSEntity.setMetodo("get");
                peticionWSEntity.setTipo("requestPending");
                peticionWSEntity.setCredencialesEntity(credencialesEntity);

                // Llamada al cliente para consultar los detalles del reporte
                ListadoWebService listadoWebService = new ListadoWebService();
                listadoWebService.webServiceListener = requestPendingFragment;
                listadoWebService.execute(peticionWSEntity);
            } catch (Exception ex) {
                // Limpia las listas de error
                messageTypeList.clear();
                messageTitleList.clear();
                messageDescriptionList.clear();

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

        return rootView;
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
            listaReporteCache = responseWebServiceEntity.getListaReporte();
            // Convierte la lista en un arreglo
            ReporteEntity[] arrayReporte = new ReporteEntity[responseWebServiceEntity.getListaReporte().size()];
            responseWebServiceEntity.getListaReporte().toArray(arrayReporte);

            // Genera el adaptador
            ReporteItemAdapter reporteItemAdapter = new ReporteItemAdapter(getActivity(), R.layout.layout_custom_listview_report, arrayReporte, true);

            // Carga los errores en el cuerpo del cuadro de dialogo
            requestPendingLsvItem.setAdapter(reporteItemAdapter);
        } else {
            // Limpia las listas de error
            messageTypeList.clear();
            messageTitleList.clear();
            messageDescriptionList.clear();

            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add("Notificación");
            messageDescriptionList.add("No existen reportes pendientes por atender");

            // Si existen errores genera la estructura adecuada
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
            responseWebServiceEntity = new ResponseWebServiceEntity();
            responseWebServiceEntity.setListaMensajes(messagesList);

            // Llama al metodo que procesa la respuesta
            onCommunicationFinish(responseWebServiceEntity);
        }

        layoutOptions.setVisibility(View.VISIBLE);
    }
}