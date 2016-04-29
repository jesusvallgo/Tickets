package mx.gob.cenapred.tickets.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MensajeEntity implements Parcelable{
    private String mensajeTipo;
    private String mensajeTitulo;
    private String mensajeDescripcion;

    public String getMensajeTipo() {
        return mensajeTipo;
    }

    public void setMensajeTipo(String mensajeTipo) {
        this.mensajeTipo = mensajeTipo;
    }

    public String getMensajeTitulo() {
        return mensajeTitulo;
    }

    public void setMensajeTitulo(String mensajeTitulo) {
        this.mensajeTitulo = mensajeTitulo;
    }

    public String getMensajeDescripcion() {
        return mensajeDescripcion;
    }

    public void setMensajeDescripcion(String mensajeDescripcion) {
        this.mensajeDescripcion = mensajeDescripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mensajeTitulo);
        dest.writeString(mensajeDescripcion);
    }

    public static final Parcelable.Creator<MensajeEntity> CREATOR = new Creator<MensajeEntity>() {
        @Override
        public MensajeEntity createFromParcel(Parcel source) {
            MensajeEntity mMensajeEntity = new MensajeEntity();
            mMensajeEntity.mensajeTitulo = source.readString();
            mMensajeEntity.mensajeDescripcion = source.readString();
            return mMensajeEntity;
        }

        @Override
        public MensajeEntity[] newArray(int size) {
            return new MensajeEntity[0];
        }
    };
}
