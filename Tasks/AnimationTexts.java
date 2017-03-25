package com.alexandrelunkes.catolicapp.Tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;

/**
 * Created by Alexandre Lunkes on 14/05/2016.
 */
public class AnimationTexts extends AsyncTask<Integer,Integer,String>{
    int linhas;
    int unidadeTam;
    int totalLooping;
    int unidadeLooping;
    private OnExecuteAnimationText myCallBack;
    boolean isVisible = false;
    private TextoPergaminho textView;


    public AnimationTexts(Fragment fragment, TextoPergaminho textView) {
        this.textView = textView;
        //Log.i("teste_texto",textView.getText().toString());
        myCallBack = (OnExecuteAnimationText) fragment;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        linhas = textView.getLineCount();
        unidadeTam = (int) ((textView.getLineHeight() * 1.3F)+0.5F);
        Log.i("teste_texto","linhas qty "+textView.getLineCount() + " altura linha "+unidadeTam);
        unidadeLooping = unidadeTam/8;
        totalLooping = linhas*8;

    }

    @Override
    protected String doInBackground(Integer... params) {


        CatolicApp.logCatolicApp("Animation total "+ totalLooping);

        for(int i=0; i<totalLooping;i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(1);
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        switch (values[0]){

            case 0: isVisible = myCallBack.getFragmentIsVisible();break;
            case 1: myCallBack.onIncrementaTamanho(unidadeLooping);break;
            case 2: linhas = textView.getLineCount();
                    unidadeTam = textView.getLineHeight();
                    unidadeLooping = unidadeTam/8;
                    totalLooping = linhas*8;break;
            default:break;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


    public interface OnExecuteAnimationText{
        void onIncrementaTamanho(int unidade);
        boolean getFragmentIsVisible();
    }


}


