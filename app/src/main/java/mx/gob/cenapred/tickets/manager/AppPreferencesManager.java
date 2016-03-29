package mx.gob.cenapred.tickets.manager;

import android.content.Context;
import android.content.SharedPreferences;

import mx.gob.cenapred.tickets.entity.CredencialesEntity;
import mx.gob.cenapred.tickets.entity.EmpleadoEntity;
import mx.gob.cenapred.tickets.preference.AppPreference;

/**
 * Created by CENAPRED on 23/02/2016.
 */
public class AppPreferencesManager {
    private SharedPreferences sharedPreferences;

    public AppPreferencesManager(Context context) {
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getTokenRegistered() {
        return this.sharedPreferences.getBoolean(AppPreference.TOKEN_REGISTERED, false);
    }

    public boolean getCredentials() {
        String userLogin = this.getUserLogin();
        String userPassword = this.getUserPassword();
        if (userLogin.isEmpty() || userPassword.isEmpty()) {
            return false;
        }
        return true;
    }

    public void saveCredentials(CredencialesEntity credencialesEntity) {
        this.sharedPreferences.edit().putString(AppPreference.USER_LOGIN, credencialesEntity.getUsername()).apply();
        this.sharedPreferences.edit().putString(AppPreference.USER_PASSWORD, credencialesEntity.getPassword()).apply();
    }

    public void saveUserData(EmpleadoEntity empleadoEntity){
        this.sharedPreferences.edit().putString(AppPreference.USER_NAME, empleadoEntity.getNombreCompletoPorNombre()).apply();
        this.sharedPreferences.edit().putInt(AppPreference.USER_ROLE, empleadoEntity.getUsuario().getRol().getIdRol()).apply();
    }

    public void clearCredentials() {
        this.sharedPreferences.edit().putString(AppPreference.USER_LOGIN, "").apply();
        this.sharedPreferences.edit().putString(AppPreference.USER_PASSWORD, "").apply();
        this.sharedPreferences.edit().putString(AppPreference.USER_NAME, "").apply();
        this.sharedPreferences.edit().putInt(AppPreference.USER_ROLE, 0).apply();
    }

    public String getUserLogin(){
        return this.sharedPreferences.getString(AppPreference.USER_LOGIN, "");
    }

    public String getUserPassword(){
        return this.sharedPreferences.getString(AppPreference.USER_PASSWORD, "");
    }

    public String getDeviceToken(){
        return this.sharedPreferences.getString(AppPreference.DEVICE_TOKEN, "");
    }

    public Integer getUserRole() {
        return this.sharedPreferences.getInt(AppPreference.USER_ROLE, 0);
    }

    public String getUserName(){
        return this.sharedPreferences.getString(AppPreference.USER_NAME, "");
    }
}
