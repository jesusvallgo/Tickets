package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.AreaAtencionEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.ErrorManager;
import mx.gob.cenapred.tickets.preference.AppPreference;

public class ReportDelegateFragment extends Fragment implements WebServiceListener{
    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase para especificar las credenciales de usuario
    private CredencialesEntity credencialesEntity = new CredencialesEntity();

    // Instancias a la clases para especificar el tipo de reporte
    private ReporteEntity reporteEntity = new ReporteEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageErrorList = new ArrayList<String>();
    private List<String> messageDebugList = new ArrayList<String>();

    // Manejador de los errores
    private ErrorManager errorManager = new ErrorManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private ReportDelegateFragment reportDelegateFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    TextView reportDelegateTxvIdReport;
    Spinner reportDelegateSpnAtentionArea;

    // Inicializa las variables del Fragment
    private Integer idReport = 0;
    private List<AreaAtencionEntity> listAtentionArea;

    // Constructor por default
    public ReportDelegateFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportDelegateFragment newInstance() {
        ReportDelegateFragment fragment = new ReportDelegateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idReport = (Integer) getArguments().getInt("idReport", 0);
        listAtentionArea = (List<AreaAtencionEntity>) getArguments().getSerializable("listAtentionArea");
        getArguments().remove("listAtentionArea");
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_delegate, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Construye los campos necesarios de la Entidad Credenciales
        credencialesEntity.setUsername(appPreferencesManager.getUserLogin());
        credencialesEntity.setPassword(appPreferencesManager.getUserPassword());

        // Mapea los layouts del Fragment
        reportDelegateTxvIdReport = (TextView) rootView.findViewById(R.id.report_delegate_txv_id_report);
        reportDelegateSpnAtentionArea = (Spinner) rootView.findViewById(R.id.report_delegate_spn_atention_area);

        // Obtiene el numero de acciones realizadas para el reporte especificado
        Integer numAreaAtencion = listAtentionArea.size();

        // Determina si existen mensajes para desplegar
        if (numAreaAtencion > 0) {
            // Establece el ID del reporte
            reportDelegateTxvIdReport.setText("No. Folio: " + idReport.toString());

            // Llena el spinner de areas de atencion con los datos correspondientes
            ArrayAdapter areaAtencionAdapter = new ArrayAdapter(getContext(), R.layout.layout_custom_spinner_estatus, listAtentionArea);
            areaAtencionAdapter.setDropDownViewResource(R.layout.layout_custom_spinner_estatus);
            reportDelegateSpnAtentionArea.setAdapter(areaAtencionAdapter);
        } else {

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
            errorManager.displayError(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_DEFAULT);
        } else{
            getActivity().onBackPressed();
        }
    }
}