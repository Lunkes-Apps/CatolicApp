package com.alexandrelunkes.catolicapp.bibliaeagenda;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.SqliteHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 06/09/2016.
 */
public class RepositorioAgendaDAO {

    protected static final String NOME_TABELA = "agenda";
    private static final int VERSAO = 1;

    public static final String DATA_BASE_DELETE = "DROP TABLE IF EXISTS " + NOME_TABELA;

    private SQLiteDatabase db;
    private static RepositorioAgendaDAO instance;
    private Context context;

    static public RepositorioAgendaDAO getInstance(Context context) {

        if (instance == null) {
            instance = new RepositorioAgendaDAO(context);
        }
        return instance;
    }

    private RepositorioAgendaDAO(Context context) {
        SqliteHelper sqliteHelper = SqliteHelper.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    public void carregarAgenda() {

        db.beginTransaction();
        SQLiteStatement statement;

        try {
            InputStream input = context.getAssets().open("scripts/createDadosAgenda.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String linha = null;
            do {
                linha = reader.readLine();
                if (linha != null) {
                    Log.i("teste_banco_agenda", linha);
                    statement = db.compileStatement(linha);
                    statement.execute();
                }


            } while (linha != null);


        } catch (IOException e) {
            e.printStackTrace();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i("teste_banco_agenda", "fim da insercion");

    }


    public ItemAgenda leiturasPorData(String data) {

        String query = "SELECT primeira, salmo, segunda, evangelho FROM " + NOME_TABELA + " WHERE data = " + "'" + data + "';";
        String query2 = "SELECT dat FROM agenda;";

        Cursor cursor = db.rawQuery(query, null);

        int pId = cursor.getColumnIndex(ItemAgenda.PRIMEIRA);
        int salId = cursor.getColumnIndex(ItemAgenda.SALMO);
        int sId = cursor.getColumnIndex(ItemAgenda.SEGUNDA);
        int evId = cursor.getColumnIndex(ItemAgenda.EVANGELHO);
        //int dId = cursor.getColumnIndex(ItemAgenda.DATA);

        ItemAgenda agenda = null;

       // Log.i("teste_banco_agenda", query);
        Log.i("teste_banco_agenda", " encontrou "+ cursor.getCount());


        if (cursor.moveToFirst()) {
            do {
                Log.i("teste_banco_agenda", "cursor foi");
                String primeira = cursor.getString(pId);
                String salmo = cursor.getString(salId);
                String segunda = cursor.getString(sId);
                String evangelho = cursor.getString(evId);

//                String dataa = cursor.getString(dId);
              agenda = new ItemAgenda(data, primeira, salmo, segunda, evangelho);

            } while (cursor.moveToNext());

        }

        Log.i("teste_banco_agenda", agenda.getEvangelho()+" este mesmo");

        return agenda;
    }

    public ItemAgenda[] listarTodaAgenda(){

        String query = "SELECT * FROM "+NOME_TABELA;
        Cursor cursor = db.rawQuery(query,null);

        List<ItemAgenda> lista = new ArrayList<>();

        int datId = cursor.getColumnIndex(ItemAgenda.DATA);
        int pId = cursor.getColumnIndex(ItemAgenda.PRIMEIRA);
        int salId = cursor.getColumnIndex(ItemAgenda.SALMO);
        int sId = cursor.getColumnIndex(ItemAgenda.SEGUNDA);
        int evId = cursor.getColumnIndex(ItemAgenda.EVANGELHO);

        if(cursor.moveToFirst()){

            do{
                String primeira = cursor.getString(pId);
                String salmo = cursor.getString(salId);
                String segunda = cursor.getString(sId);
                String evangelho = cursor.getString(evId);
                String data = cursor.getString(datId);

                lista.add(new ItemAgenda(data, primeira, salmo, segunda, evangelho));

            }while (cursor.moveToNext());

        }
        return lista.toArray(new ItemAgenda[lista.size()]);
    }


    public void deletBanco() {
        db.execSQL(DATA_BASE_DELETE);
        db.execSQL("CREATE TABLE agenda(id integer primary key autoincrement, data text not null, primeira text not null, salmo text not null, segunda text, evangelho text not null);");
    }
}
