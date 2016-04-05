package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.AreaAtencionEntity;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.EstatusEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.ErrorManager;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

public class ReportUpdateHistoryFragment extends Fragment implements WebServiceListener {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

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
    private ReportUpdateHistoryFragment reportUpdateHistoryFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    TextView reportUpdateHistoryTxvIdReport, reportUpdateHistoryTxvCharacters;
    Spinner reportUpdateHistorySpnEstatus;
    EditText reportUpdateHistoryEdtAction;
    ImageButton reportUpdateHistoryBtnSend;

    // Inicializa las variables del Fragment
    private Integer idReport = 0;
    private List<EstatusEntity> listEstatus;

    // Constructor por default
    public ReportUpdateHistoryFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportUpdateHistoryFragment newInstance() {
        ReportUpdateHistoryFragment fragment = new ReportUpdateHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idReport = (Integer) getArguments().getInt("idReport", 0);
        listEstatus = (List<EstatusEntity>) getArguments().getSerializable("listStatus");
        getArguments().remove("listStatus");
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_update_history, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Construye los campos necesarios de la Entidad Credenciales
        credencialesEntity.setUsername(appPreferencesManager.getUserLogin());
        credencialesEntity.setPassword(appPreferencesManager.getUserPassword());

        // Mapea los layouts del Fragment
        reportUpdateHistoryTxvIdReport = (TextView) rootView.findViewById(R.id.report_update_history_txv_id_report);
        reportUpdateHistorySpnEstatus = (Spinner) rootView.findViewById(R.id.report_update_history_spn_status);
        reportUpdateHistoryEdtAction = (EditText) rootView.findViewById(R.id.report_update_history_edt_action);
        reportUpdateHistoryTxvCharacters = (TextView) rootView.findViewById(R.id.report_update_history_txv_characters);
        reportUpdateHistoryBtnSend = (ImageButton) rootView.findViewById(R.id.report_update_history_btn_send);

        // Obtiene el numero de acciones realizadas para el reporte especificado
        Integer numEstatus = listEstatus.size();

        // Determina si existen mensajes para desplegar
        if (numEstatus > 0) {
            // Establece el ID del reporte
            reportUpdateHistoryTxvIdReport.setText("No. Folio: " + idReport.toString());

            // Llena el spinner de areas de atencion con los datos correspondientes
            ArrayAdapter estatusAdapter = new ArrayAdapter(getContext(), R.layout.layout_custom_spinner_estatus, listEstatus);
            estatusAdapter.setDropDownViewResource(R.layout.layout_custom_spinner_estatus);
            reportUpdateHistorySpnEstatus.setAdapter(estatusAdapter);
        }

        reportUpdateHistoryEdtAction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportUpdateHistoryTxvCharacters.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reportUpdateHistoryBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpia las listas de error
                messageErrorList.clear();
                messageDebugList.clear();

                try {
                    // Oculta las opciones del Fragment
                    layoutOptions.setVisibility(View.GONE);

                    // Muestra el layout de Cargando
                    layoutLoading.setVisibility(View.VISIBLE);

                    // Obtiene el "Estatus" seleccionada por el usuario
                    EstatusEntity estatusEntity = (EstatusEntity) reportUpdateHistorySpnEstatus.getSelectedItem();

                    if (estatusEntity.getIdEstatus() == 0) {
                        throw new NoInputDataException("Debe seleccionar un Estatus válido");
                    }

                    if (reportUpdateHistoryEdtAction.getText().toString().trim().equals("")) {
                        throw new NoInputDataException("Debe especificar la acción realizada");
                    }

                    // Genera la lista de acciones (solo un elemento)
                    List<BitacoraEntity> listaBitacora = new ArrayList<BitacoraEntity>();
                    BitacoraEntity bitacoraEntity = new BitacoraEntity();
                    bitacoraEntity.setAccion(reportUpdateHistoryEdtAction.getText().toString().trim());
                    listaBitacora.add(bitacoraEntity);

                    // Establece los datos por actualizar en el reporte
                    reporteEntity.setIdReporte(idReport);
                    reporteEntity.setEstatus(estatusEntity);
                    reporteEntity.setBitacora(listaBitacora);

                    // Construye la peticion
                    peticionWSEntity.setMetodo("put");
                    peticionWSEntity.setAccion("history");
                    peticionWSEntity.setCredencialesEntity(credencialesEntity);
                    peticionWSEntity.setReporteEntity(reporteEntity);

                    // Llamada al cliente para actualizar el reporte correspondiente
                    ReporteWebService reporteWebService = new ReporteWebService();
                    reporteWebService.webServiceListener = reportUpdateHistoryFragment;
                    reporteWebService.execute(peticionWSEntity);
                } catch (NoInputDataException nidEx) {
                    // Agrega el error a mostrar
                    messageErrorList.add("Datos no válidos");
                    messageDebugList.add(nidEx.getMessage());
                } catch (Exception ex) {
                    // Agrega el error a mostrar
                    messageErrorList.add("Error al realizar la petición al Web Service");
                    messageDebugList.add(ex.getMessage());
                } finally {
                    if (messageErrorList.size() > 0) {
                        // Si existen errores genera la estructura adecuada
                        messagesList = errorManager.createMensajesList(messageErrorList, messageDebugList);
                        ResponseWebServiceEntity respuesta = new ResponseWebServiceEntity();
                        respuesta.setListaMensajes(messagesList);

                        // Llama al metodo que procesa la respuesta
                        onCommunicationFinish(respuesta);
                    }
                }
            }
        });

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
        } else {
            getActivity().onBackPressed();
        }

        // Muestra las opciones del Fragment
        layoutOptions.setVisibility(View.VISIBLE);
    }
}