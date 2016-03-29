package mx.gob.cenapred.tickets.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.adapter.MensajeEntityAdapter;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.preference.AppPreference;

/**
 * Created by CENAPRED on 24/02/2016.
 */
public class ErrorManager {
    public MensajeEntity[] createMensajesArray(List<String> mensajeErrorList, List<String> mensajeDebugList) {
        Integer numMensajes = mensajeErrorList.size();
        MensajeEntity[] mensajes = new MensajeEntity[numMensajes];

        MensajeEntity mensajeEntity;
        for (Integer i = 0; i < numMensajes; i++) {
            mensajeEntity = new MensajeEntity();
            mensajeEntity.setMensajeError(mensajeErrorList.get(i));
            mensajeEntity.setMensajeDebug(mensajeDebugList.get(i));
            mensajes[i] = mensajeEntity;
        }

        return mensajes;
    }

    public List<MensajeEntity> createMensajesList(List<String> mensajeErrorList, List<String> mensajeDebugList) {
        Integer numMensajes = mensajeErrorList.size();
        List<MensajeEntity> mensajes = new ArrayList<MensajeEntity>();

        MensajeEntity mensajeEntity;
        for (Integer i = 0; i < numMensajes; i++) {
            mensajeEntity = new MensajeEntity();
            mensajeEntity.setMensajeError(mensajeErrorList.get(i));
            mensajeEntity.setMensajeDebug(mensajeDebugList.get(i));
            mensajes.add(mensajeEntity);
        }

        return mensajes;
    }

    public void displayError(final Activity activity, Context context, List<MensajeEntity> mensajesList, final String accion){
        // Determina si existen mensajes para desplegar
        MensajeEntity[] mensajes;
        if(mensajesList.size()>0){
            // Convierte la lista en arreglo
            Integer numMensajes = mensajesList.size();
            mensajes = new MensajeEntity[numMensajes];
            mensajesList.toArray(mensajes);
        } else {
            mensajes = new MensajeEntity[1];
            mensajes[0].setMensajeError("Desconocido");
            mensajes[0].setMensajeDebug("Se desconoce el origen de la llamada");
        }

        // Genera el adaptador
        MensajeEntityAdapter adapter = new MensajeEntityAdapter(activity, R.layout.layout_custom_listview_error, mensajes);

        // Crea el cuerpo del cuadro de dialogo
        LayoutInflater inflater = LayoutInflater.from(context);
        View customBody = inflater.inflate(R.layout.layout_custom_alertdialog_error, null);

        // Crea el constructor del cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Asigna el cuerpo al cuadro de dialogo
        builder.setView(customBody);

        // Carga los errores en el cuerpo del cuadro de dialogo
        ListView listError = (ListView) customBody.findViewById(R.id.list_custom_error);
        listError.setAdapter(adapter);

        // Agrega el boton para cerrar el cuadro de dialogo
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (accion){
                    case AppPreference.ALERT_ACTION_FINISH:
                        activity.finish();
                        break;
                    case AppPreference.ALERT_ACTION_GOBACK:
                        activity.onBackPressed();
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        });

        // Muestra el cuadro de dialogo
        AlertDialog customAlert = builder.create();
        customAlert.show();
    }
}
