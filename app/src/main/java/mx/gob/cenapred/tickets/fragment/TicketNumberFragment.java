package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.ErrorManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.manager.KeyboardManager;

public class TicketNumberFragment extends Fragment {
    // **************************** Constantes ****************************

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Manejador de los errores
    private ErrorManager errorManager = new ErrorManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Contenedor de datos del Bundle
    private BundleEntity bundleEntity = new BundleEntity();

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageErrorList = new ArrayList<String>();
    private List<String> messageDebugList = new ArrayList<String>();

    // Mapea los elementos del Fragment
    private EditText ticketNumberEdtNumber;
    private Button ticketNumberBtnSend;

    // Inicializa las variables del Fragment
    private String idReport = "";

    // Constructor por default
    public TicketNumberFragment() {

    }

    // Generador de instancia de Fragment
    public static TicketNumberFragment newInstance() {
        TicketNumberFragment fragment = new TicketNumberFragment();
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
        rootView = inflater.inflate(R.layout.fragment_ticket_number, container, false);

        // Manejador de los datos de la sesion de usuario
        appPreferencesManager = new AppPreferencesManager(getContext());

        // Configura el Fragment para ocultar el teclado
        keyboardManager.configureUI(rootView, getActivity());

        // Mapea los elementos del Fragment
        ticketNumberEdtNumber = (EditText) rootView.findViewById(R.id.ticket_number_edt_number);
        ticketNumberBtnSend = (Button) rootView.findViewById(R.id.ticket_number_btn_send);

        ticketNumberBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idReport = ticketNumberEdtNumber.getText().toString().trim();

                // Limpia las listas de error
                messageErrorList.clear();
                messageDebugList.clear();

                try {
                    if (idReport.length() == 9) {
                        bundleEntity.setIdReportBundle(Integer.parseInt(idReport));
                        bundleEntity.setAddToBackStack(true);
                        ((MainActivity) getActivity()).manageFragment(R.id.fragment_report_detail, bundleEntity);
                    } else {
                        throw new NoInputDataException("Es necesario especificar un Número de Folio de 9 dígitos");
                    }
                } catch (NoInputDataException nidEx) {
                    // Agrega el error a mostrar
                    messageErrorList.add("Datos no válidos");
                    messageDebugList.add(nidEx.getMessage());
                } catch (NumberFormatException nfEx) {
                    // Agrega el error a mostrar
                    messageErrorList.add("Datos no válidos");
                    messageDebugList.add("El Número de folio especificado no puede ser leido correctamente");
                } catch (Exception ex) {
                    // Agrega el error a mostrar
                    messageErrorList.add("Error desconocido");
                    messageDebugList.add(ex.getMessage());
                } finally {
                    if (messageErrorList.size() > 0) {
                        // Crea la lista de errores
                        messagesList = errorManager.createMensajesList(messageErrorList, messageDebugList);

                        // Despliega los errores encontrados
                        errorManager.displayError(getActivity(), getContext(), messagesList, AppPreference.ALERT_ACTION_DEFAULT);
                    }
                }
            }
        });

        return rootView;
    }
}
