package mx.gob.cenapred.tickets.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GooglePlayServices extends AppCompatActivity {
    // Constante para definir el tiempo de respuesta a los servicios de Google
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public Boolean validaGooglePlayServices(String TAG,Context context){
        // Instancia a la clase de GoogleApiAvailability
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        // Obtiene el estatus del Servicio de Google Play
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            // El dispositivo no cuenta con el servicio
            if (apiAvailability.isUserResolvableError(resultCode)) {
                // Muestra el error correspondiente
                apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            // Al no contar con los requisitos, devuelve un FALSE
            return false;
        }

        // El dispositivo cumple con los requisitos, por lo tanto, devuelve TRUE
        return true;
    }
}
