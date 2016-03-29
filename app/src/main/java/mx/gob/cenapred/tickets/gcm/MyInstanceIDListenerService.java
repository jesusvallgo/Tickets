package mx.gob.cenapred.tickets.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    // Arranca si el token ha sido actualizado. Esto ocurre si la seguridad del
    // token anterior ha sido comprometida. Este proceso es llamado por el
    // proveedor del token (InstanceID)
    @Override
    public void onTokenRefresh() {
        // Intenta generar el nuevo token y almacenarlo en el Servidor interno
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
