package mx.gob.cenapred.tickets.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            holder.error_title = (TextView)row.findViewById(R.id.error_title);
            holder.error_description = (TextView)row.findViewById(R.id.error_description);
            row.setTag(holder);
        } else {
            holder = (MensajeEntityHolder)row.getTag();
        }

        MensajeEntity mensajeEntitie = myData[position];
        holder.error_title.setText(mensajeEntitie.getMensajeError());
        holder.error_description.setText(mensajeEntitie.getMensajeDebug());

        return row;
    }

    static class MensajeEntityHolder{
        TextView error_title;
        TextView error_description;
    }
}
