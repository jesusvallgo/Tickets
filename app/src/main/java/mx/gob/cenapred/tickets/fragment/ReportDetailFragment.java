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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.entity.PeticionWSEntity;
import mx.gob.cenapred.tickets.entity.ReporteEntity;
import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;
import mx.gob.cenapred.tickets.exception.BadInputDataException;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.listener.WebServiceListener;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;
import mx.gob.cenapred.tickets.webservice.ReporteWebService;

public class ReportDetailFragment extends Fragment implements WebServiceListener {
    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Instancia a la clase de PeticionWSEntity
    private PeticionWSEntity peticionWSEntity = new PeticionWSEntity();

    // Instancias a la clases para especificar el tipo de reporte
    private ReporteEntity reporteEntity = new ReporteEntity();

    // Contenedor de datos del Bundle
    private BundleEntity bundleEntity = new BundleEntity();

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
    private ReportDetailFragment reportDetailFragment = this;

    // Layouts del Fragment
    private LinearLayout layoutOptions;
    private RelativeLayout layoutLoading;

    // Mapea los elementos del Fragment
    TextView reportDetailTxvIdReport, reportDetailTxvDate, reportDetailTxvUser, reportDetailTxvArea, reportDetailTxvAreaAtencion, reportDetailTxvDescription, reportDetailTxvEstatus;

    // Inicializa las variables del Fragment
    private Integer idReport = 0;
    private Boolean addToBackStack = false;
    private String alertAction = AppPreference.ALERT_ACTION_DEFAULT;
    private String apiKey = "";

    private MenuItem menuItemHistory, menuItemDelegate, menuItemUpdate;
    private Boolean menuItemHistoryVisible = false, menuItemDelegateVisible = false, menuItemUpdateVisible = false;

    // Constructor por default
    public ReportDetailFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportDetailFragment newInstance() {
        ReportDetailFragment fragment = new ReportDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idReport = (Integer) getArguments().getInt("idReport", 0);
        addToBackStack = (Boolean) getArguments().getBoolean("addToBackStack", false);

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_detail, container, false);

        // Mapea los layouts del Fragment
        layoutOptions = (LinearLayout) rootView.findViewById(R.id.layout_options);
        layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        apiKey = appPreferencesManager.getApiKey();

        // Mapea los elementos del Fragment
        reportDetailTxvIdReport = (TextView) rootView.findViewById(R.id.report_detail_txv_id_report);
        reportDetailTxvDate = (TextView) rootView.findViewById(R.id.report_detail_txv_date);
        reportDetailTxvUser = (TextView) rootView.findViewById(R.id.report_detail_txv_user);
        reportDetailTxvArea = (TextView) rootView.findViewById(R.id.report_detail_txv_area);
        reportDetailTxvAreaAtencion = (TextView) rootView.findViewById(R.id.report_detail_txv_area_atencion);
        reportDetailTxvDescription = (TextView) rootView.findViewById(R.id.report_detail_txv_description);
        reportDetailTxvEstatus = (TextView) rootView.findViewById(R.id.report_detail_txv_estatus);

        try {
            // Instancia al validador
            ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

            // Valida si existe una sesion de usuario
            validaCadenaUtil.validarApiKey(apiKey);

            // Valida si se recibio un numero de folio
            validaCadenaUtil.validarFolio(idReport.toString());

            // Construye los campos necesarios de la Entidad Reporte
            reporteEntity.setIdReporte(idReport);

            // Construye la peticion
            peticionWSEntity.setMetodo("get");
            peticionWSEntity.setApiKey(apiKey);
            peticionWSEntity.setReporteEntity(reporteEntity);

            // Llamada al cliente para consultar los detalles del reporte
            ReporteWebService reporteWebService = new ReporteWebService();
            reporteWebService.webServiceListener = reportDetailFragment;
            reporteWebService.execute(peticionWSEntity);
        } catch (NoInputDataException nidEx){
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_NO_INPUT_DATA);
            messageDescriptionList.add(nidEx.getMessage());
        } catch (BadInputDataException bidEx){
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_BAD_INPUT_DATA);
            messageDescriptionList.add(bidEx.getMessage());
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_WS_REQUEST_FAIL);
            messageDescriptionList.add(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (messageTitleList.size() > 0) {
                // Si existen errores genera la estructura adecuada
                messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);
                ResponseWebServiceEntity responseWebServiceEntity = new ResponseWebServiceEntity();
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
            // Decide la accion que debe aplicarse al cerrar el cuadro de dialogo de acuerdo al origen de la apertura del Fragment
            if (addToBackStack) {
                alertAction = AppPreference.ALERT_ACTION_GOBACK;
            } else {
                alertAction = AppPreference.ALERT_ACTION_FINISH;
            }

            // Muestra los errores en pantalla
            messagesManager.displayMessage(getActivity(), getContext(), responseWebServiceEntity.getListaMensajes(), alertAction);
        } else if (responseWebServiceEntity.getReporte() != null) {
            // Llena los campos con la informacion correspondiente
            reportDetailTxvIdReport.setText(responseWebServiceEntity.getReporte().getIdReporte().toString());
            reportDetailTxvDate.setText(responseWebServiceEntity.getReporte().getFecha().toString());
            reportDetailTxvUser.setText(responseWebServiceEntity.getReporte().getEmpleado().getNombreCompletoPorNombre().toString());
            reportDetailTxvArea.setText(responseWebServiceEntity.getReporte().getEmpleado().getArea().getArea().toString());
            reportDetailTxvAreaAtencion.setText(responseWebServiceEntity.getReporte().getAreaAtencion().getAreaAtencion().toString());
            reportDetailTxvDescription.setText(responseWebServiceEntity.getReporte().getDescripcion().toString());
            reportDetailTxvEstatus.setText(responseWebServiceEntity.getReporte().getEstatus().getEstatus().toString());

            if (responseWebServiceEntity.getReporte().getBitacora().size() > 0) {
                // Habilita la bandera para mostrar la opcion de "Ver Historial" del reporte
                menuItemHistoryVisible = true;

                // Carga en el bundle auxiliar la lista de acciones realizadas
                bundleEntity.setListHistoryAction(responseWebServiceEntity.getReporte().getBitacora());
            } else{
                menuItemHistoryVisible = false;
            }

            // Si existen datos del usuario
            if (responseWebServiceEntity.getUsuario() != null) {
                // Si es un usuario con perfil de "Atencion a incidentes" y ademas, el reporte puede ser editado
                if (responseWebServiceEntity.getUsuario().getRol().getIdRol() == 2 && responseWebServiceEntity.getReporte().getEstatus().getEditable() == true) {

                    // Si existen datos de "Area de Atencion"
                    if( responseWebServiceEntity.getListaAreaAtencion() != null ){
                        // Si existe algun Area de Atencion para turnar el reporte
                        if(responseWebServiceEntity.getListaAreaAtencion().size() > 0 ){
                            // Habilita la bandera para mostrar la opcion de "Turnar reporte"
                            menuItemDelegateVisible = true;

                            // Carga en el bundle auxiliar la lista de areas de atencion
                            bundleEntity.setIdReportBundle(idReport);
                            bundleEntity.setListAreaAtencion(responseWebServiceEntity.getListaAreaAtencion());
                        }
                    }

                    // Si existen datos de "Estatus"
                    if( responseWebServiceEntity.getListaEstatus() != null ){
                        // Si existe algun Area de Atencion para turnar el reporte
                        if(responseWebServiceEntity.getListaEstatus().size() > 0 ){
                            // Habilita la bandera para mostrar la opcion de "Turnar reporte"
                            menuItemUpdateVisible = true;

                            // Carga en el bundle auxiliar la lista de areas de atencion
                            bundleEntity.setIdReportBundle(idReport);
                            bundleEntity.setListEstatus(responseWebServiceEntity.getListaEstatus());
                        }
                    }
                } else{
                    menuItemDelegateVisible = false;
                    menuItemUpdateVisible = false;
                }
            }

            // Muestra las opciones del Fragment
            layoutOptions.setVisibility(View.VISIBLE);

            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.report_detail_menu, menu);

        // Mapea el item del menu
        menuItemHistory = menu.findItem(R.id.item_history);
        menuItemDelegate = menu.findItem(R.id.item_delegate);
        menuItemUpdate = menu.findItem(R.id.item_update);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Muestra u oculta los items del menu
        menuItemHistory.setVisible(menuItemHistoryVisible);
        menuItemDelegate.setVisible(menuItemDelegateVisible);
        menuItemUpdate.setVisible(menuItemUpdateVisible);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Integer fragment;
        switch (item.getItemId()) {
            case R.id.item_history:
                fragment = R.id.fragment_report_history;
                break;
            case R.id.item_delegate:
                fragment = R.id.fragment_report_delegate;
                break;
            case R.id.item_update:
                fragment = R.id.fragment_report_update_history;
                break;
            default:
                fragment = 0;
                break;
        }

        if( fragment!=0 ){
            ((MainActivity) getActivity()).manageFragment(fragment, bundleEntity);
        }
        return true;
    }
}