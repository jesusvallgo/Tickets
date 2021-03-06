package mx.gob.cenapred.tickets.manager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import mx.gob.cenapred.tickets.entity.CustomFilterItemEntity;

public class DatePickerManager  extends DatePickerDialog{
    private String title = "Seleccionar fecha";
    public DatePickerManager(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, CustomFilterItemEntity object) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        setTitle(title);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(title);
    }
}
