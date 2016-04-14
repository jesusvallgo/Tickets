package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.adapter.BitacoraEntityAdapter;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.ErrorManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.webservice.ListadoWebService;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

public class MyTicketPendingFragment extends Fragment implements WebServiceListener{
    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase ResponseWebServiceEntity
    private ResponseWebServiceEntity responseWebServiceEntity;

    // Instancia a la clase para especificar las credenciales de usuario
    private CredencialesEntity credencialesEntity = new CredencialesEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageErrorList = new ArrayList<String>();
    private List<String> messageDebugList = new ArrayList<String>();

    // Manejador de los errores
    private ErrorManager errorManager = new ErrorManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private MyTicketPendingFragment myTicketPendingFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    ListView myTicketPendingLsvItem;

    // Constructor por default
    public MyTicketPendingFragment() {

    }

    // Generador de instancia de Fragment
    public static MyTicketPendingFragment newInstance() {
        MyTicketPendingFragment fragment = new MyTicketPendingFragment();
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
        rootView = inflater.inflate(R.layout.fragment_my_ticket_pending, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Construye los campos necesarios de la Entidad Credenciales
        credencialesEntity.setUsername(appPreferencesManager.getUserLogin());
        credencialesEntity.setPassword(appPreferencesManager.getUserPassword());

        // Mapea los elementos del Fragment
        myTicketPendingLsvItem = (ListView) rootView.findViewById(R.id.my_ticket_pending_lsv_item);

        try {
            // Construye la peticion
            peticionWSEntity.setMetodo("get");
            peticionWSEntity.setTipo("myPending");
            peticionWSEntity.setCredencialesEntity(credencialesEntity);

            // Llamada al cliente para consultar los detalles del reporte
            ListadoWebService listadoWebService = new ListadoWebService();
            listadoWebService.webServiceListener = myTicketPendingFragment;
            listadoWebService.execute(peticionWSEntity);
        } catch (Exception ex) {
            // Limpia las listas de error
            messageErrorList.clear();
            messageDebugList.clear();

            // Agrega el error a mostrar
            messageErrorList.add("Error al realizar la petición al Web Service");
            messageDebugList.add(ex.getMessage());
        } finally {
            if (messageErrorList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = errorManager.createMensajesList(messageErrorList, messageDebugList);
                responseWebServiceEntity = new ResponseWebServiceEntity();
                responseWebServiceEntity.setListaMensajes(messagesList);

                // Llama al metodo que procesa la respuesta
                onCommunicationFinish(responseWebServiceEntity);
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
            errorManager.displayError(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_GOBACK);
        } else if (responseWebServiceEntity.getListaReporte()!=null && responseWebServiceEntity.getListaReporte().size()>0){
            String[] array = new String[2];
            array[0] = "0";
            array[1] = "1";

            // Genera el adaptador
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, array);

            // Carga los errores en el cuerpo del cuadro de dialogo
            myTicketPendingLsvItem.setAdapter(adapter);
        } else{
            // Limpia las listas de error
            messageErrorList.clear();
            messageDebugList.clear();

            // Agrega el error a mostrar
            messageErrorList.add("Notificación");
            messageDebugList.add("No existen reportes pendientes por atender");

            // Si existen errores genera la estructura adecuada
            messagesList = errorManager.createMensajesList(messageErrorList, messageDebugList);
            responseWebServiceEntity = new ResponseWebServiceEntity();
            responseWebServiceEntity.setListaMensajes(messagesList);

            // Llama al metodo que procesa la respuesta
            onCommunicationFinish(responseWebServiceEntity);
        }

        layoutOptions.setVisibility(View.VISIBLE);
    }
}