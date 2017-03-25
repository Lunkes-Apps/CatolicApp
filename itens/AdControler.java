package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.util.Log;

import com.alexandrelunkes.catolicapp.Interfaces.OnAdReturnCommand;
import com.google.android.gms.ads.AdListener;

/**
 * Created by Alexandre Lunkes on 15/09/2016.
 */
public class AdControler extends AdListener {

    private Context context;
    private OnAdReturnCommand myCallBack;

    public AdControler(Context context, OnAdReturnCommand myCallBack) {
        this.context = context;
        this.myCallBack = myCallBack;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        myCallBack.onOpenAd();
        Log.i("teste_ad", "banner fechou");
    }


}