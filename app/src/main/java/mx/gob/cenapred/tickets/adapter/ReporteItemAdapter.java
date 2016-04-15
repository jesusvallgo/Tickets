package mx.gob.cenapred.tickets.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.ReporteEntity;

public class ReporteItemAdapter extends ArrayAdapter<ReporteEntity> {
    Context myContext;
    int myLayoutResourceID;
    ReporteEntity myData[] = null;

    public ReporteItemAdapter(Context context, int layoutResourceID, ReporteEntity[] data) {
        super(context, layoutResourceID, data);

        this.myContext = context;
        this.myLayoutResourceID = layoutResourceID;
        this.myData = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ReporteEntityHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            row = inflater.inflate(myLayoutResourceID,parent,false);

            holder = new ReporteEntityHolder();
            holder.date = (TextView)row.findViewById(R.id.ticket_txv_date);
            holder.user = (TextView)row.findViewById(R.id.ticket_txv_user);
            holder.description = (TextView)row.findViewById(R.id.ticket_txv_description);
            row.setTag(holder);
        } else {
            holder = (ReporteEntityHolder)row.getTag();
        }

        ReporteEntity reporteEntity = myData[position];
        holder.date.setText(reporteEntity.getFecha());
        holder.user.setText(reporteEntity.getEmpleado().getNombreCompletoPorNombre());
        holder.description.setText(reporteEntity.getDescripcion());

        return row;
    }

    static class ReporteEntityHolder{
        TextView date;
        TextView user;
        TextView description;
    }
}
