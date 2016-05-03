package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.exception.BadInputDataException;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ValidaCadenaUtil;

public class SearchTicketNumberFragment extends Fragment {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Contenedor de datos del Bundle
    private BundleEntity bundleEntity = new BundleEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<String>();
    private List<String> messageTitleList = new ArrayList<String>();
    private List<String> messageDescriptionList = new ArrayList<String>();

    // Mapea los elementos del Fragment
    private EditText searchTicketNumberEdtNumber;
    private ImageButton searchTicketNumberBtnSearch;

    // Inicializa las variables del Fragment
    private String idReport = "";

    // Constructor por default
    public SearchTicketNumberFragment() {

    }

    // Generador de instancia de Fragment
    public static SearchTicketNumberFragment newInstance() {
        SearchTicketNumberFragment fragment = new SearchTicketNumberFragment();
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_search_ticket_number, container, false);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Mapea los elementos del Fragment
        searchTicketNumberEdtNumber = (EditText) rootView.findViewById(R.id.ticket_number_edt_number);
        searchTicketNumberBtnSearch = (ImageButton) rootView.findViewById(R.id.ticket_number_btn_search);

        searchTicketNumberEdtNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    tryGetTicket();
                }
                return false;
            }
        });

        searchTicketNumberBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryGetTicket();
            }
        });

        return rootView;
    }

    private void tryGetTicket() {
        idReport = searchTicketNumberEdtNumber.getText().toString().trim();

        // Limpia las listas de error
        messageTypeList.clear();
        messageTitleList.clear();
        messageDescriptionList.clear();

        try {
            // Instancia al validador de datos de entrada
            ValidaCadenaUtil validaCadenaUtil = new ValidaCadenaUtil();

            // Validacion del numero de folio
            validaCadenaUtil.validarFolio(idReport);

            // Transforma el numero de folio a su tipo correspondiente
            bundleEntity.setIdReportBundle(Integer.parseInt(idReport));

            // Indica que debe poder regresar en el backstack
            bundleEntity.setAddToBackStack(true);

            // Cambia de Fragment
            ((MainActivity) getActivity()).manageFragment(R.id.fragment_report_detail, bundleEntity);
        } catch (BadInputDataException bidEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_WARNING);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_BAD_INPUT_DATA);
            messageDescriptionList.add(bidEx.getMessage());
        } catch (NumberFormatException nfEx) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_BAD_INPUT_DATA);
            messageDescriptionList.add(MainConstant.MESSAGE_DESCRIPTION_BAD_NUMBER_FORMAT);
        } catch (Exception ex) {
            // Agrega el error a mostrar
            messageTypeList.add(AppPreference.MESSAGE_ERROR);
            messageTitleList.add(MainConstant.MESSAGE_TITLE_UNKNOWKN_FAIL);
            messageDescriptionList.add(ex.getMessage());
        } finally {
            if (messageTitleList.size() > 0) {
                // Crea la lista de errores
                messagesList = messagesManager.createMensajesList(messageTypeList, messageTitleList, messageDescriptionList);

                // Despliega los errores encontrados
                messagesManager.displayMessage(getActivity(), getContext(), messagesList, AppPreference.ALERT_ACTION_DEFAULT);
            }
        }
    }
}
