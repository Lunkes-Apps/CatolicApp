package com.alexandrelunkes.catolicapp.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Alexandre Lunkes on 12/05/2016.
 */
public class TaskTeste extends AsyncTask {

    Context context;
    ProgressDialog dialog;
    public TaskTeste(Context context) {
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = ProgressDialog.show(context,"aaaaaaa","assasdasd");

    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        dialog.dismiss();

    }
}
