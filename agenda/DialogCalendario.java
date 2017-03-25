package com.alexandrelunkes.catolicapp.agenda;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;


import java.util.Calendar;


/**
 * Created by Alexandre Lunkes on 03/05/2016.
 */
public class DialogCalendario extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    DataLiturgiaClickedListener myCallBaclk;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,year,month,day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,12,32);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.set(2015,11,32);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myCallBaclk = (DataLiturgiaClickedListener) activity;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.dismiss();
        myCallBaclk.onDataLiturgiaClicked(view);
    }

    public interface DataLiturgiaClickedListener{
        public void onDataLiturgiaClicked(DatePicker date);
    }


}
