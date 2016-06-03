package mx.gob.cenapred.tickets.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.listener.ConfirmationListener;
import mx.gob.cenapred.tickets.preference.AppPreference;

public class ConfirmationManager {
    public ConfirmationListener listener;

    public void displayNewReportConfirmation(final View view ,Context context, String confirmationType){
        // Crea el cuerpo del cuadro de dialogo
        LayoutInflater inflater = LayoutInflater.from(context);
        View customBody = inflater.inflate(R.layout.layout_custom_alertdialog_confirmation, null);

        String title = "";
        String description = "";
        switch (confirmationType){
            case AppPreference.CONFIRMATION_NEW_REPORT:
                title = MainConstant.CONFIRMATION_TITLE_NEW_REPORT;
                description = MainConstant.CONFIRMATION_DESCRIPTION_NEW_REPORT;
                break;
            case AppPreference.CONFIRMATION_EMAIL_STADISTICS:
                title = MainConstant.CONFIRMATION_TITLE_MAIL_STADISTICS;
                description = MainConstant.CONFIRMATION_DESCRIPTION_MAIL_STADISTICS;
                break;
        }

        // Crea el constructor del cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        // Asigna el cuerpo al cuadro de dialogo
        builder.setView(customBody);

        // Agrega el boton para cerrar el cuadro de dialogo
        builder.setPositiveButton(MainConstant.CONFIRMATION_BUTTON_POSITIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClickButton(view, Boolean.TRUE);
            }
        });

        builder.setNegativeButton(MainConstant.CONFIRMATION_BUTTON_NEGATIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClickButton(view,Boolean.FALSE);
            }
        });

        // Carga los errores en el cuerpo del cuadro de dialogo
        TextView txvDescription = (TextView) customBody.findViewById(R.id.confirmation_description);
        txvDescription.setText(description);

        // Muestra el cuadro de dialogo
        AlertDialog customAlert = builder.create();
        customAlert.show();
    }
}
