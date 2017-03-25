package com.alexandrelunkes.catolicapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;
import com.alexandrelunkes.catolicapp.favoritos.RepositorioFavoritosDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alexandre Lunkes on 21/08/2016.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String LOG_SQLITE_OPEN_HELPER = "teste_SqliteHelper";
    private static final String NOME_BANCO = "catolicapp_data_base";
    private static final String KEYS_ENABLE = "PRAGMA foreign_key=%s;";


    private static final int version = 1;
    private static SqliteHelper instance;
    private Context context;

    private String scriptCreate1;
    private String scriptDelete1;
    private String scriptDeleteBiblia;

    private SqliteHelper(Context context) {
        super(context, NOME_BANCO, null, version);
        scriptCreate1 = RepositorioFavoritosDAO.DATABASE_CREATE;
        scriptDelete1 = RepositorioFavoritosDAO.DATA_BASE_DELETE;
        scriptDeleteBiblia = RepositorioBibliaDAO.DATA_BASE_DELETE;
        this.context = context;
    }

    public static SqliteHelper getInstance(Context context){
        if(instance == null){
            instance = new SqliteHelper(context);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_SQLITE_OPEN_HELPER,"Criando banco de dados ");
        db.execSQL(scriptCreate1);
        executarSqlAsset("create.sql",db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(scriptDelete1);
        db.execSQL(scriptDeleteBiblia);

        onCreate(db);
    }

    public static void enableForeiKeys(SQLiteDatabase db, final boolean enabled){
        db.execSQL(String.format(KEYS_ENABLE,enabled));
    }

    private void executarSqlAsset(String path, SQLiteDatabase db){

        try {
            final InputStream input = context.getAssets().open("scripts/"+path);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String linha = null;

            while ((linha=reader.readLine())!=null){
                if(!linha.equalsIgnoreCase("")){
                    Log.i("teste_banco",linha+  " esta foi a linha");
                    db.execSQL(linha);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_SQLITE_OPEN_HELPER,"Falhou a execucao "+path+" "+e);
        }


    }


}

