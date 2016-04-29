package mx.gob.cenapred.tickets.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.preference.AppPreference;

public class MensajeEntityAdapter extends ArrayAdapter<MensajeEntity> {
    Context myContext;
    int myLayoutResourceID;
    MensajeEntity myData[] = null;

    public MensajeEntityAdapter(Context context, int layoutResourceID, MensajeEntity[] data) {
        super(context, layoutResourceID,data);

        this.myContext = context;
        this.myLayoutResourceID = layoutResourceID;
        this.myData = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MensajeEntityHolder holder = null;
        Integer icon;

        if (row == null) {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            row = inflater.inflate(myLayoutResourceID,parent,false);

            holder = new MensajeEntityHolder();
            holder.message_type = (ImageView)row.findViewById(R.id.message_image);
            holder.message_title = (TextView)row.findViewById(R.id.message_title);
            holder.message_description = (TextView)row.findViewById(R.id.message_description);
            row.setTag(holder);
        } else {
            holder = (MensajeEntityHolder)row.getTag();
        }

        MensajeEntity mensajeEntity = myData[position];
        switch (mensajeEntity.getMensajeTipo()){
            case AppPreference.MESSAGE_ERROR:
                icon = R.mipmap.ic_error;
                break;
            case AppPreference.MESSAGE_SUCCESS:
                icon = R.mipmap.ic_success;
                break;
            default:
                icon = R.mipmap.ic_error;
                break;
        }
        holder.message_type.setImageResource(icon);
        holder.message_title.setText(mensajeEntity.getMensajeTitulo());
        holder.message_description.setText(mensajeEntity.getMensajeDescripcion());

        return row;
    }

    static class MensajeEntityHolder{
        ImageView message_type;
        TextView message_title;
        TextView message_description;
    }
}
