package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.AreaAtencionEntity;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;
import mx.gob.cenapred.tickets.listener.ConfirmationListener;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.ConfirmationManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

public class ReportNewShortcutFragment extends Fragment implements View.OnClickListener, WebServiceListener, ConfirmationListener {
    // **************************** Constantes ****************************

    // Instancia a la clase para validar datos de entrada
    private final ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancias a la clases para especificar el tipo de reporte
    private ReporteEntity reporteEntity = new ReporteEntity();
    private AreaAtencionEntity areaAtencionEntity = new AreaAtencionEntity();

    // Contenedor de datos del Bundle
    private BundleEntity bundleEntity = new BundleEntity();

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
    private ReportNewShortcutFragment reportNewShortcutFragment = this;

    // Layouts del Fragment
    private FrameLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    LinearLayout btnLowToner, btnPaperJam, btnVirus, btnCrashOs, btnPasswordLost, btnDomainLost, btnOther;

    // Variables del fragment
    private Integer idAttentionArea, idLayout;
    private String alertAction = AppPreference.ALERT_ACTION_DEFAULT;
    private String apiKey = "";

    // Constructor por default
    public ReportNewShortcutFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportNewShortcutFragment newInstance() {
        ReportNewShortcutFragment fragment = new ReportNewShortcutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idAttentionArea = getArguments().getInt("idAttentionArea", 0);

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el fragment
        rootView = inflater.inflate(R.layout.fragment_report_new_shortcut, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (FrameLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        try {
            // Recupera el APIKEY almacenada en el dispositivo
            apiKey = appPreferencesManager.getApiKey();

            // Valida el ApiKey
            validaCadenaUtil.validarApiKey(apiKey);

            // Determina si existen mensajes para desplegar
            if (idAttentionArea == 0) {
                throw new NoInputDataException(MainConstant.MESSAGE_DESCRIPTION_EMPTY_ATTENTION_AREA);
            }

            // Establece el ID del Area de Atencion
            areaAtencionEntity.setIdAreaAtencion(idAttentionArea);
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
        }

        // Determina el contenido del FrameLayout
        switch (idAttentionArea) {
            case 1:
                LayoutInflater.from(getContext()).inflate(R.layout.layout_report_new_technical_support, layoutOptions);

                // Mapea los elementos del Fragment

                btnLowToner = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_low_toner);
                btnPaperJam = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_paper_jam);
                btnVirus = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_virus);
                btnCrashOs = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_crash_os);
                btnPasswordLost = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_password_lost);
                btnDomainLost = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_domain_lost);
                btnOther = (LinearLayout) rootView.findViewById(R.id.technical_support_btn_other);

                // Agrega el evento onClick a los elementos del Fragment
                btnLowToner.setOnClickListener(this);
                btnPaperJam.setOnClickListener(this);
                btnVirus.setOnClickListener(this);
                btnCrashOs.setOnClickListener(this);
                btnPasswordLost.setOnClickListener(this);
                btnDomainLost.setOnClickListener(this);
                btnOther.setOnClickListener(this);

                break;
            default:
                break;
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

        return rootView;
    }

    // Metodo onClick para los elementos del Fragmnet
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.technical_support_btn_other) {
            // Se abre el nuevo fragment para especificar la solicitud
            bundleEntity.setIdAreaAtencion(idAttentionArea);
            ((MainActivity) getActivity()).manageFragment(R.id.fragment_report_other, bundleEntity);
        } else {
            // Solicita la confirmacion del envio del reporte
            ConfirmationManager confirmationManager = new ConfirmationManager();
            confirmationManager.listener = reportNewShortcutFragment;
            confirmationManager.displayNewReportConfirmation(v, getContext(),AppPreference.CONFIRMATION_NEW_REPORT);
        }
    }

    // Metodo que procesa la respuesta del WebService
    @Override
    public void onCommunicationFinish(ResponseWebServiceEntity responseWebServiceEntity) {
        if (responseWebServiceEntity.getListaMensajes() != null) {
            // Oculta el layout de Cargando
            layoutLoading.setVisibility(View.GONE);

            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), alertAction);

            // Muestra las opciones del Fragment
            layoutOptions.setVisibility(View.VISIBLE);
        } else {
            // Genera aviso para el usuario que indica que su peticion ha sido exitosa
            Toast.makeText(getContext(), MainConstant.TOAST_REPORT_CREATE_SUCCESS, Toast.LENGTH_LONG).show();

            // Redirige al Fragment de bienvenida
            ((MainActivity) getActivity()).manageFragment(R.id.nav_welcome, null);
        }
    }

    @Override
    public void communicationStatus(Boolean running) {
        ((MainActivity) getActivity()).asyncTaskRunning = running;
    }

    @Override
    public void onClickButton(View view, Boolean confirmation) {
        if (confirmation) {
            // Resetea las variables del Fragment
            String description = "";

            // Carga las variables del Fragment de acuerdo al boton seleccionado
            switch (view.getId()) {
                case R.id.technical_support_btn_low_toner:
                    description = getString(R.string.technical_support_description_low_toner);
                    break;
                case R.id.technical_support_btn_paper_jam:
                    description = getString(R.string.technical_support_description_paper_jam);
                    break;
                case R.id.technical_support_btn_virus:
                    description = getString(R.string.technical_support_description_virus);
                    break;
                case R.id.technical_support_btn_crash_os:
                    description = getString(R.string.technical_support_description_crash_os);
                    break;
                case R.id.technical_support_btn_password_lost:
                    description = getString(R.string.technical_support_description_password_lost);
                    break;
                case R.id.technical_support_btn_domain_lost:
                    description = getString(R.string.technical_support_description_domain_lost);
                    break;
                default:
                    break;
            }

            try {
                // Oculta las opciones del Fragment
                layoutOptions.setVisibility(View.GONE);

                // Muestra el layout de Cargando
                layoutLoading.setVisibility(View.VISIBLE);

                // Construye los campos necesarios de la Entidad Reporte
                reporteEntity.setAreaAtencion(areaAtencionEntity);
                reporteEntity.setDescripcion(description);

                // Construye la peticion
                peticionWSEntity.setMetodo("post");
                peticionWSEntity.setApiKey(apiKey);
                peticionWSEntity.setReporteEntity(reporteEntity);

                // Llamada al cliente para agregar el reporte correspondiente
                ReporteWebService reporteWebService = new ReporteWebService();
                reporteWebService.webServiceListener = reportNewShortcutFragment;
                reporteWebService.execute(peticionWSEntity);
            } catch (Exception ex) {
                // Limpia las listas de error
                messageTypeList.clear();
                messageTitleList.clear();
                messageDescriptionList.clear();

                // Agrega el error a mostrar
                messageTypeList.add(AppPreference.MESSAGE_ERROR);
                messageTitleList.add(MainConstant.MESSAGE_TITLE_WS_REQUEST_FAIL);
                messageDescriptionList.add(ex.getMessage());
            } finally {
                if (messageTypeList.size() > 0) {
                    // Si existen errores genera la estructura adecuada
                    messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
                    ResponseWebServiceEntity respuesta = new ResponseWebServiceEntity();
                    respuesta.setListaMensajes(messagesList);

                    // Llama al metodo que procesa la respuesta
                    onCommunicationFinish(respuesta);
                }
            }
        }
    }
}