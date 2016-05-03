package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.AreaAtencionEntity;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.BadInputDataException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

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
    private List<String> messageTypeList = new ArrayList<>();
    private List<String> messageTitleList = new ArrayList<>();
    private List<String> messageDescriptionList = new ArrayList<>();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Instancia para obener el Fragment que se debe pasar a la interfaz
    private ReportDelegateFragment reportDelegateFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    TextView reportDelegateTxvTitle;
    Spinner reportDelegateSpnAtentionArea;
    Button reportDelegateBtnAction;

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
        idReport = getArguments().getInt("idReport", 0);
        listAtentionArea = (List<AreaAtencionEntity>) getArguments().getSerializable("listAttentionArea");
        getArguments().remove("listAttentionArea");

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
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
        reportDelegateTxvTitle = (TextView) rootView.findViewById(R.id.report_delegate_txv_title);
        reportDelegateSpnAtentionArea = (Spinner) rootView.findViewById(R.id.report_delegate_spn_atention_area);
        reportDelegateBtnAction = (Button) rootView.findViewById(R.id.report_delegate_btn_action);

        // Determina si se recibio el numero de folio
        if( idReport>0 ){
            // Establece el ID del reporte
            reportDelegateTxvTitle.append(" " + idReport.toString());
        } else {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_INPUT_DATA);
            messageDescriptionList.add(MainConstant.MESSAGE_DESCRIPTION_NO_ID_REPORT);
        }

        // Obtiene el numero de acciones realizadas para el reporte especificado
        Integer numAreaAtencion = listAtentionArea.size();

        // Determina si existen mensajes para desplegar
        if (numAreaAtencion > 0) {
            // Llena el spinner de areas de atencion con los datos correspondientes
            ArrayAdapter areaAtencionAdapter = new ArrayAdapter(getContext(), R.layout.layout_custom_spinner_item, listAtentionArea);
            areaAtencionAdapter.setDropDownViewResource(R.layout.layout_custom_spinner_dropdown);
            reportDelegateSpnAtentionArea.setAdapter(areaAtencionAdapter);
        } else {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_INPUT_DATA);
            messageDescriptionList.add(MainConstant.MESSAGE_DESCRIPTION_NO_LIST_ATTENTION_AREA);
        }

        if( messageTitleList.size()>0 ){
            // Si existen errores genera la estructura adecuada
            messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
            messagesManager.displayMessage(getActivity(), getContext(), messagesList, AppPreference.ALERT_ACTION_GOBACK);
        }

        reportDelegateBtnAction.setOnClickListener(new View.OnClickListener() {
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

                    // Obtiene el "Area de Atencion" seleccionada por el usuario
                    AreaAtencionEntity areaAtencionEntity = (AreaAtencionEntity) reportDelegateSpnAtentionArea.getSelectedItem();

                    // Genera la lista de acciones (solo un elemento)
                    List<BitacoraEntity> listaBitacora = new ArrayList<>();
                    BitacoraEntity bitacoraEntity = new BitacoraEntity();
                    bitacoraEntity.setAccion(getString(R.string.report_delegate_default_text) + " " + areaAtencionEntity.getAreaAtencion());
                    listaBitacora.add(bitacoraEntity);

                    // Establece los datos por actualizar en el reporte
                    reporteEntity.setIdReporte(idReport);
                    reporteEntity.setAreaAtencion(areaAtencionEntity);
                    reporteEntity.setBitacora(listaBitacora);

                    if (areaAtencionEntity.getIdAreaAtencion() != 0) {
                        // Construye la peticion
                        peticionWSEntity.setMetodo("put");
                        peticionWSEntity.setAccion("delegate");
                        peticionWSEntity.setCredencialesEntity(credencialesEntity);
                        peticionWSEntity.setReporteEntity(reporteEntity);

                        // Llamada al cliente para actualizar el reporte correspondiente
                        ReporteWebService reporteWebService = new ReporteWebService();
                        reporteWebService.webServiceListener = reportDelegateFragment;
                        reporteWebService.execute(peticionWSEntity);
                    } else {
                        throw new BadInputDataException(MainConstant.MESSAGE_DESCRIPTION_EMPTY_ATTENTION_AREA);
                    }
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
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), AppPreference.ALERT_ACTION_DEFAULT);
        } else{
            // Genera aviso para el usuario que indica que su peticion ha sido exitosa
            Toast.makeText(getContext(), getString(R.string.general_toast_delegate_report_successful), Toast.LENGTH_LONG).show();

            getActivity().onBackPressed();
        }

        // Muestra las opciones del Fragment
        layoutOptions.setVisibility(View.VISIBLE);
    }
}