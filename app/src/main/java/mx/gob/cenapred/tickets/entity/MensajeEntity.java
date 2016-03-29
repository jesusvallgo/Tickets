package mx.gob.cenapred.tickets.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MensajeEntity implements Parcelable{
    private String mensajeError;
    private String mensajeDebug;

    public String getMensajeError(){
        return this.mensajeError;
    }

    public String getMensajeDebug() {
        return this.mensajeDebug;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public void setMensajeDebug(String mensajeDebug) {
        this.mensajeDebug = mensajeDebug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mensajeError);
        dest.writeString(mensajeDebug);
    }

    public static final Parcelable.Creator<MensajeEntity> CREATOR = new Creator<MensajeEntity>() {
        @Override
        public MensajeEntity createFromParcel(Parcel source) {
            MensajeEntity mMensajeEntity = new MensajeEntity();
            mMensajeEntity.mensajeError = source.readString();
            mMensajeEntity.mensajeDebug = source.readString();
            return mMensajeEntity;
        }

        @Override
        public MensajeEntity[] newArray(int size) {
            return new MensajeEntity[0];
        }
    };
}
