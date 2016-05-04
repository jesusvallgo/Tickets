package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;
import mx.gob.cenapred.tickets.entity.EstatusEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.BadInputDataException;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

public class ReportAddHistoryFragment extends Fragment implements WebServiceListener {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancias a la clases para especificar el tipo de reporte
    private ReporteEntity reporteEntity = new ReporteEntity();

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
    private ReportAddHistoryFragment reportAddHistoryFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    TextView reportAddHistoryTxvTitle, reportAddHistoryTxvCharactersCount;
    Spinner reportAddHistorySpnEstatus;
    EditText reportAddHistoryEdtDescription;
    ImageButton reportAddHistoryBtnSend;

    // Inicializa las variables del Fragment
    private Integer idReport = 0;
    private Integer descriptionMaxLenght = 0;
    private List<EstatusEntity> listEstatus;

    // Variables del fragment
    private String alertAction = AppPreference.ALERT_ACTION_DEFAULT;
    private String apiKey = "";

    // Constructor por default
    public ReportAddHistoryFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportAddHistoryFragment newInstance() {
        ReportAddHistoryFragment fragment = new ReportAddHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idReport = getArguments().getInt("idReport", 0);
        listEstatus = (List<EstatusEntity>) getArguments().getSerializable("listStatus");
        getArguments().remove("listStatus");

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_add_history, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Mapea los layouts del Fragment
        reportAddHistoryTxvTitle = (TextView) rootView.findViewById(R.id.report_add_history_txv_title);
        reportAddHistorySpnEstatus = (Spinner) rootView.findViewById(R.id.report_add_history_spn_status);
        reportAddHistoryEdtDescription = (EditText) rootView.findViewById(R.id.report_add_history_edt_description);
        reportAddHistoryTxvCharactersCount = (TextView) rootView.findViewById(R.id.report_add_history_txv_characters_count);
        reportAddHistoryBtnSend = (ImageButton) rootView.findViewById(R.id.report_add_history_btn_send);

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Obtiene el tamaño maximo de la descripcion
        descriptionMaxLenght = MainConstant.DESCRIPTION_MAX_LENGHT;

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        try {
            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el ApiKey
            validaCadenaUtil.validarApiKey(apiKey);

            // Valida el numero de folio
            validaCadenaUtil.validarFolio(idReport.toString());

            // Establece el ID del reporte
            reportAddHistoryTxvTitle.append(" " + idReport.toString());

            // Obtiene el numero de acciones realizadas para el reporte especificado
            Integer numEstatus = listEstatus.size();

            // Determina si existen mensajes para desplegar
            if (numEstatus > 0) {
                // Llena el spinner de areas de atencion con los datos correspondientes
                ArrayAdapter estatusAdapter = new ArrayAdapter(getContext(), R.layout.layout_custom_spinner_item, listEstatus);
                estatusAdapter.setDropDownViewResource(R.layout.layout_custom_spinner_dropdown);
                reportAddHistorySpnEstatus.setAdapter(estatusAdapter);
            } else {
                // Agrega el error a mostrar
                throw new NoInputDataException(MainConstant.MESSAGE_DESCRIPTION_NO_LIST_STATUS);
            }
        } catch (NoUserLoginException nulEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_SESSION);
            messageDescriptionList.add(nulEx.getMessage());
        } catch (NoInputDataException nidEx){
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_INPUT_DATA);
            messageDescriptionList.add(nidEx.getMessage());
        } catch (BadInputDataException bidEx){
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_BAD_INPUT_DATA);
            messageDescriptionList.add(bidEx.getMessage());
        }

        if( messageTitleList.size()>0 ){
            // Indica que debe regresar al Fragment anterior
            alertAction = AppPreference.ALERT_ACTION_GOBACK;

            // Si existen errores genera la estructura adecuada
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
            ResponseWebServiceEntity respuesta = new ResponseWebServiceEntity();
            respuesta.setListaMensajes(messagesList);

            // Llama al metodo que procesa la respuesta
            onCommunicationFinish(respuesta);
        }

        reportAddHistoryEdtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportAddHistoryTxvCharactersCount.setText(String.valueOf(s.length()) + " / " + descriptionMaxLenght);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reportAddHistoryEdtDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER){
                    return true;
                } else{
                    return false;
                }
            }
        });

        reportAddHistoryBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpia las listas de error
                messageTypeList.clear();
                messageTitleList.clear();
                messageDescriptionList.clear();

                try {
                    // Oculta las opciones del Fragment
                    layoutOptions.setVisibility(View.GONE);

                    // Muestra el layout de Cargando
                    layoutLoading.setVisibility(View.VISIBLE);

                    // Obtiene el "Estatus" seleccionada por el usuario
                    EstatusEntity estatusEntity = (EstatusEntity) reportAddHistorySpnEstatus.getSelectedItem();

                    if (estatusEntity.getIdEstatus() == 0) {
                        throw new BadInputDataException(MainConstant.MESSAGE_DESCRIPTION_EMPTY_STATUS);
                    }

                    // Obtiene la descripcion
                    String description = reportAddHistoryEdtDescription.getText().toString().trim();
                    if (description.equals("")) {
                        throw new BadInputDataException(MainConstant.MESSAGE_DESCRIPTION_EMPTY_DESCRIPTION);
                    }

                    // Genera la lista de acciones (solo un elemento)
                    List<BitacoraEntity> listaBitacora = new ArrayList<>();
                    BitacoraEntity bitacoraEntity = new BitacoraEntity();
                    bitacoraEntity.setAccion(description);
                    listaBitacora.add(bitacoraEntity);

                    // Establece los datos por actualizar en el reporte
                    reporteEntity.setIdReporte(idReport);
                    reporteEntity.setEstatus(estatusEntity);
                    reporteEntity.setBitacora(listaBitacora);

                    // Construye la peticion
                    peticionWSEntity.setMetodo("put");
                    peticionWSEntity.setAccion("history");
                    peticionWSEntity.setApiKey(apiKey);
                    peticionWSEntity.setReporteEntity(reporteEntity);

                    // Llamada al cliente para actualizar el reporte correspondiente
                    ReporteWebService reporteWebService = new ReporteWebService();
                    reporteWebService.webServiceListener = reportAddHistoryFragment;
                    reporteWebService.execute(peticionWSEntity);
                } catch (BadInputDataException bidEx) {
                    // Agrega el error a mostrar
                    messageTypeList.add(AppPreference.MESSAGE_WARNING);
                    messageTitleList.add(MainConstant.MESSAGE_TITLE_BAD_INPUT_DATA);
                    messageDescriptionList.add(bidEx.getMessage());
                } catch (Exception ex) {
                    // Agrega el error a mostrar
                    messageTypeList.add(AppPreference.MESSAGE_ERROR);
                    messageTitleList.add(MainConstant.MESSAGE_TITLE_WS_REQUEST_FAIL);
                    messageDescriptionList.add(ex.getMessage());
                } finally {
                    if (messageTitleList.size() > 0) {
                        // Si existen errores genera la estructura adecuada
                        messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
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
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), alertAction);
        } else {
            // Genera aviso para el usuario que indica que su peticion ha sido exitosa
            Toast.makeText(getContext(), MainConstant.TOAST_REPORT_UPDATE_SUCCESS, Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        }

        // Muestra las opciones del Fragment
        layoutOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void communicationStatus(Boolean running) {
        ((MainActivity) getActivity()).asyncTaskRunning = running;
    }
}