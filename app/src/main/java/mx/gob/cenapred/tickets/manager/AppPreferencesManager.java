package mx.gob.cenapred.tickets.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.EmpleadoEntity;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.ApiKeyUtil;
import mx.gob.cenapred.tickets.util.Crypto;

public class AppPreferencesManager {
    private SharedPreferences sharedPreferences;

    // Constructor de clase
    public AppPreferencesManager(Context context) {
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    // Metodo para obtener el estado de registro del Token del dispositivo
    public boolean getTokenRegistered() {
        return this.sharedPreferences.getBoolean(AppPreference.TOKEN_REGISTERED, false);
    }

    // Metodo para obtener el estado de la sesion de usuario en el dispositivo
    public boolean getLoginStatus() {
        String apiKey = this.getApiKey();
        if( apiKey.isEmpty() ){
            return false;
        } else {
            return true;
        }
    }

    // Metodo para almacenar las credenciales de usuario en el dispositivo
    public void saveCredentials(String apiKey) {
        // Almacena el APIKEY
        this.sharedPreferences.edit().putString(AppPreference.API_KEY, apiKey).apply();
    }

    // Metodo para almacenar la informacion del perfil de usuario en el dispositivo
    public void saveUserData(EmpleadoEntity empleadoEntity){
        // Almacena el NOMBRE
        this.sharedPreferences.edit().putString(AppPreference.USER_NAME, empleadoEntity.getNombreCompletoPorNombre()).apply();

        // Almacena el ROL
        this.sharedPreferences.edit().putInt(AppPreference.USER_ROLE, empleadoEntity.getUsuario().getRol().getIdRol()).apply();
    }

    // Metodo para limpiar las credenciales de usuario en el dispositivo
    public void clearCredentials() {
        // Limpia el APIKEY
        this.sharedPreferences.edit().putString(AppPreference.API_KEY,"").apply();

        // Limpia el NOMBRE
        this.sharedPreferences.edit().putString(AppPreference.USER_NAME, "").apply();

        // Limpia el ROL
        this.sharedPreferences.edit().putInt(AppPreference.USER_ROLE, 0).apply();
    }

    // Metodo para obtener el APIKEY
    public String getApiKey(){
        return this.sharedPreferences.getString(AppPreference.USER_PASSWORD, "");
    }

    // Metodo para obtener el NOMBRE
    public String getUserName(){
        return this.sharedPreferences.getString(AppPreference.USER_NAME, "");
    }

    // Metodo para obtener el ROL
    public Integer getUserRole() {
        return this.sharedPreferences.getInt(AppPreference.USER_ROLE, 0);
    }

    // Metodo para obtener el TOKEN DEL DISPOSITIVO
    public String getDeviceToken(){
        return this.sharedPreferences.getString(AppPreference.DEVICE_TOKEN, "");
    }
}
