package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Alexandre Lunkes on 14/09/2016.
 */
public class CheckNetWorkManager {

    private Context context;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;

    public CheckNetWorkManager(Context context) {
        this.context = context;
         cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
         activeNetwork = cm.getActiveNetworkInfo();
    }

    public boolean isConected(){

       Log.i("teste_conexao"," boolean "+String.valueOf(activeNetwork != null &&
               activeNetwork.isConnectedOrConnecting()));

       return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
