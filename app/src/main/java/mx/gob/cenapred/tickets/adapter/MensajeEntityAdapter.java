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

        MensajeEntity mensajeEntitie = myData[position];
        holder.message_type.setImageResource(R.mipmap.ic_error);
        holder.message_title.setText(mensajeEntitie.getMensajeTitulo());
        holder.message_description.setText(mensajeEntitie.getMensajeDescripcion());

        return row;
    }

    static class MensajeEntityHolder{
        ImageView message_type;
        TextView message_title;
        TextView message_description;
    }
}
