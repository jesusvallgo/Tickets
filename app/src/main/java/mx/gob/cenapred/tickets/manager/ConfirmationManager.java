package mx.gob.cenapred.tickets.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.listener.ConfirmationTicketListener;

public class ConfirmationManager {
    public ConfirmationTicketListener listener;

    public void displayNewReportConfirmation(final View view ,Context context){
        // Crea el cuerpo del cuadro de dialogo
        LayoutInflater inflater = LayoutInflater.from(context);
        View customBody = inflater.inflate(R.layout.layout_custom_alertdialog_confirmation, null);

        // Crea el constructor del cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(MainConstant.CONFIRMATION_TITLE_NEW_REPORT);

        // Asigna el cuerpo al cuadro de dialogo
        builder.setView(customBody);

        // Agrega el boton para cerrar el cuadro de dialogo
        builder.setPositiveButton(MainConstant.CONFIRMATION_POSITIVE_NEW_REPORT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClickButton(view, Boolean.TRUE);
            }
        });

        builder.setNegativeButton(MainConstant.CONFIRMATION_NEGATIVE_NEW_REPORT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClickButton(view,Boolean.FALSE);
            }
        });

        // Carga los errores en el cuerpo del cuadro de dialogo
        TextView description = (TextView) customBody.findViewById(R.id.confirmation_description);
        description.setText(MainConstant.CONFIRMATION_DESCRIPTION_NEW_REPORT);

        // Muestra el cuadro de dialogo
        AlertDialog customAlert = builder.create();
        customAlert.show();
    }
}
