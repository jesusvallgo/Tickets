package mx.gob.cenapred.tickets.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.preference.AppPreference;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegistrationIntent";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // Intenta el registro en Google Cloud Messaging
            // La primera peticion (registro) va hacia la nube, las siguientes son locales
            // R.string.google_app_id es el Sender ID o No. de Proyecto
            // Generalmente se obtiene del archivo google-services.json

            // Obtiene el token de registro
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.google_app_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // Muestra en bitacora el token obtenido
            Log.i(TAG, getResources().getString(R.string.log_success_token) + ": " + token);

            // Metodo para suscribir a listas de mensajes
            subscribeTopics(token);

            // Almacena el token del dispositivo
            sharedPreferences.edit().putString(AppPreference.DEVICE_TOKEN,token).apply();

            // Actualiza la bandera para indicar que el token ha sido generado
            sharedPreferences.edit().putBoolean(AppPreference.TOKEN_REGISTERED,true).apply();
        } catch (Exception e) {
            // Muestra en bitacora el error obtenido
            Log.d(TAG, getResources().getString(R.string.log_error_token) + ": ", e);

            // Actualiza la bandera para indicar que el token no ha podido ser generado
            sharedPreferences.edit().putBoolean(AppPreference.TOKEN_REGISTERED,false).apply();
        }

        // Informa a la Activity que se ha terminado el registro
        Intent registrationComplete = new Intent(AppPreference.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    // Metodo para subscribir al dispositivo a listas de mensajes
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            //pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
