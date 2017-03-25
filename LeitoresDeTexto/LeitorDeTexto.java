package com.alexandrelunkes.catolicapp.LeitoresDeTexto;

import android.util.Log;


import com.alexandrelunkes.catolicapp.CatolicApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

/**
 * Created by Alexandre Lunkes on 27/05/2015.
 */
public class LeitorDeTexto {

    private InputStream inputStream;
    private BufferedReader reader;

    public LeitorDeTexto(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public LeitorDeTexto(){}

    public StringBuilder returnAllText() {
        StringBuilder sb = new StringBuilder();

        try {
               reader = getReader();
               String linha;
               boolean haveMoreLine = true;

            while(haveMoreLine){
                   linha = reader.readLine();

                   if(linha == null){
                       Log.v(CatolicApp.LOG_APP,"linha = null");
                       haveMoreLine=false;

                   }else {
                       sb.append(linha);
                       sb.append("\n");
                   }
               }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb;
    }


    public String lerLinha(){
        String s="";
        try {
            s = getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }


    public BufferedReader getReader(){

        if(reader == null) {
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return reader;
    }

    public static String simplicarTitulo(String s){
        String aux = s.toLowerCase();
        aux = aux.replaceAll(" ", "_");
        aux = Normalizer.normalize(aux, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");
        return aux;
    }

    public void resetReader(){
        reader = null;
    }


    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
