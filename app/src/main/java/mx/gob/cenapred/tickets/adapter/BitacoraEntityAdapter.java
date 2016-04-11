package mx.gob.cenapred.tickets.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.BitacoraEntity;

public class BitacoraEntityAdapter extends ArrayAdapter<BitacoraEntity> {
    Context myContext;
    int myLayoutResourceID;
    BitacoraEntity myData[] = null;

    public BitacoraEntityAdapter(Context context, int layoutResourceID, BitacoraEntity[] data) {
        super(context, layoutResourceID,data);

        this.myContext = context;
        this.myLayoutResourceID = layoutResourceID;
        this.myData = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BitacoraEntityHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            row = inflater.inflate(myLayoutResourceID,parent,false);

            holder = new BitacoraEntityHolder();
            holder.date = (TextView)row.findViewById(R.id.report_view_history_txv_date);
            holder.user = (TextView)row.findViewById(R.id.report_view_history_txv_user);
            holder.action = (TextView)row.findViewById(R.id.report_view_history_txv_action);
            row.setTag(holder);
        } else {
            holder = (BitacoraEntityHolder)row.getTag();
        }

        BitacoraEntity bitacoraEntity = myData[position];
        holder.date.setText(bitacoraEntity.getFecha());
        holder.user.setText(bitacoraEntity.getEmpleado().getNombreCompletoPorNombre());
        holder.action.setText(bitacoraEntity.getAccion());

        return row;
    }

    static class BitacoraEntityHolder{
        TextView date;
        TextView user;
        TextView action;
    }
}
