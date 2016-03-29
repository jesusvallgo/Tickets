package mx.gob.cenapred.tickets.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.adapter.BitacoraEntityAdapter;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;

public class ReportHistoryFragment extends Fragment {
    // **************************** Variables ****************************

    // Para generar la vista del Fragment
    private View rootView;

    // Mapea los elementos del Fragment
    TextView reportHistoryTxvIdReport;
    ListView reportHistoryLsvAction;

    // Inicializa las variables del Fragment
    private List<BitacoraEntity> listHistoryAction = new ArrayList<BitacoraEntity>();

    // Constructor por default
    public ReportHistoryFragment() {

    }

    // Generador de instancia de Fragment
    public static ReportHistoryFragment newInstance() {
        ReportHistoryFragment fragment = new ReportHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Metodo onCreate de acuerdo al ciclo de vida de un Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listHistoryAction = (List<BitacoraEntity>) getArguments().getSerializable("listHistoryAction");
        getArguments().remove("listHistoryAction");
    }

    // Metodo onCreateView de acuerdo al ciclo de vida de un Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Genera la vista para el Fragment
        rootView = inflater.inflate(R.layout.fragment_report_history, container, false);

        // Mapea los layouts del Fragment
        reportHistoryTxvIdReport = (TextView) rootView.findViewById(R.id.report_history_txv_id_report);
        reportHistoryLsvAction = (ListView) rootView.findViewById(R.id.report_history_lsv_action);

        // Obtiene el numero de acciones realizadas para el reporte especificado
        Integer numHistoryAction = listHistoryAction.size();

        // Determina si existen mensajes para desplegar
        if (numHistoryAction > 0) {
            // Establece el ID del reporte
            reportHistoryTxvIdReport.setText("No. Folio: " + listHistoryAction.get(0).getIdReporte().toString());

            // Convierte la lista en un arreglo
            BitacoraEntity[] arrayHistoryAction = new BitacoraEntity[numHistoryAction];
            listHistoryAction.toArray(arrayHistoryAction);

            // Genera el adaptador
            BitacoraEntityAdapter adapter = new BitacoraEntityAdapter(getActivity(), R.layout.layout_custom_listview_history_action, arrayHistoryAction);

            // Carga los errores en el cuerpo del cuadro de dialogo
            reportHistoryLsvAction.setAdapter(adapter);
        } else {

        }

        return rootView;
    }
}