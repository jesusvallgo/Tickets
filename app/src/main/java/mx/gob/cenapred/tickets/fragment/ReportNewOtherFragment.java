package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.AreaAtencionEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

public class ReportNewOtherFragment extends Fragment implements WebServiceListener {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();
    private final MainConstant mainConstant = new MainConstant();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancia a la clase para especificar las credenciales de usuario
    private CredencialesEntity credencialesEntity = new CredencialesEntity();

    // Instancias a la clases para especificar el tipo de reporte
    private ReporteEntity reporteEntity = new ReporteEntity();
    private AreaAtencionEntity areaAtencionEntity = new AreaAtencionEntity();

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
    private ReportNewOtherFragment reportNewOtherFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    TextView reportNewOtherTxvCharactersCount;
    EditText reportNewOtherEdtDescription;
    ImageButton reportNewOtherBtnSend;

    // Inicializa las variables del Fragment
    private Integer idAtentionArea = 0;
    private Integer descriptionMaxLenght = 0;

    // Constructor por default
    public ReportNewOtherFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportNewOtherFragment newInstance() {
        ReportNewOtherFragment fragment = new ReportNewOtherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idAtentionArea = getArguments().getInt("idAtentionArea", 0);
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_new_other, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Obtiene el tamaño maximo de la descripcion
        descriptionMaxLenght = mainConstant.getDescriptionNewReportMaxLenght();

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Construye los campos necesarios de la Entidad Credenciales
        credencialesEntity.setUsername(appPreferencesManager.getUserLogin());
        credencialesEntity.setPassword(appPreferencesManager.getUserPassword());

        // Mapea los layouts del Fragment
        reportNewOtherEdtDescription = (EditText) rootView.findViewById(R.id.report_new_other_edt_description);
        reportNewOtherTxvCharactersCount = (TextView) rootView.findViewById(R.id.report_new_other_txv_characters_count);
        reportNewOtherBtnSend = (ImageButton) rootView.findViewById(R.id.report_new_other_btn_send);

        // Determina si existen mensajes para desplegar
        if (idAtentionArea == 0) {
            // Limpia las listas de error
            messageTypeList.clear();
            messageTitleList.clear();
            messageDescriptionList.clear();

            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(getString(R.string.general_message_title_bad_input_data));
            messageDescriptionList.add(getString(R.string.general_message_description_no_atention_area));

            // Si existen errores genera la estructura adecuada
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
            ResponseWebServiceEntity respuesta = new ResponseWebServiceEntity();
            respuesta.setListaMensajes(messagesList);

            // Llama al metodo que procesa la respuesta
            onCommunicationFinish(respuesta);
        } else {
            areaAtencionEntity.setIdAreaAtencion(idAtentionArea);
        }

        reportNewOtherEdtDescription.setFilters(new InputFilter[] {new InputFilter.LengthFilter(descriptionMaxLenght)});
        reportNewOtherEdtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportNewOtherTxvCharactersCount.setText(String.valueOf(s.length()) + " / " + descriptionMaxLenght);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reportNewOtherEdtDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER){
                    return true;
                } else{
                    return false;
                }
            }
        });

        reportNewOtherBtnSend.setOnClickListener(new View.OnClickListener() {
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

                    // Obtiene la descripcion
                    String description = reportNewOtherEdtDescription.getText().toString().trim();
                    if (description.equals("")) {
                        throw new NoInputDataException(getString(R.string.general_message_description_no_description));
                    }

                    // Construye los campos necesarios de la Entidad Reporte
                    reporteEntity.setAreaAtencion(areaAtencionEntity);
                    reporteEntity.setDescripcion(description);

                    // Construye la peticion
                    peticionWSEntity.setMetodo("post");
                    peticionWSEntity.setCredencialesEntity(credencialesEntity);
                    peticionWSEntity.setReporteEntity(reporteEntity);

                    // Llamada al cliente para actualizar el reporte correspondiente
                    ReporteWebService reporteWebService = new ReporteWebService();
                    reporteWebService.webServiceListener = reportNewOtherFragment;
                    reporteWebService.execute(peticionWSEntity);
                } catch (NoInputDataException nidEx) {
                    // Agrega el error a mostrar
                    messageTypeList.add(AppPreference.MESSAGE_ERROR);
                    messageTitleList.add(getString(R.string.general_message_title_bad_input_data));
                    messageDescriptionList.add(nidEx.getMessage());
                } catch (Exception ex) {
                    // Agrega el error a mostrar
                    messageTypeList.add(AppPreference.MESSAGE_ERROR);
                    messageTitleList.add(getString(R.string.general_message_title_ws_request_fail));
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
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_DEFAULT);
        } else {
            // Genera aviso para el usuario que indica que su peticion ha sido exitosa
            Toast.makeText(getContext(), getString(R.string.general_toast_create_report_successful), Toast.LENGTH_LONG).show();

            // Redirige al Fragment de bienvenida
            ((MainActivity) getActivity()).manageFragment(R.id.nav_welcome, null);
        }

        // Muestra las opciones del Fragment
        layoutOptions.setVisibility(View.VISIBLE);
    }
}