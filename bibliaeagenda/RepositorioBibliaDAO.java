package com.alexandrelunkes.catolicapp.bibliaeagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.nfc.tech.NfcA;
import android.util.Log;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.SqliteHelper;
import com.alexandrelunkes.catolicapp.favoritos.Favorito;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alexandre Lunkes on 06/09/2016.
 */
public class RepositorioBibliaDAO {

    protected static final String NOME_TABELA = "biblia";
    private static final int VERSAO = 1;

    public static final String DATA_BASE_DELETE = "DROP TABLE IF EXISTS " + NOME_TABELA;

    private SQLiteDatabase db;
    private static RepositorioBibliaDAO instance;
    private Context context;

    public static RepositorioBibliaDAO getInstance(Context context) {
        if (instance == null) {
            instance = new RepositorioBibliaDAO(context);
        }
        return instance;
    }

    private RepositorioBibliaDAO(Context context) {
        SqliteHelper sqlHelper = SqliteHelper.getInstance(context);
        db = sqlHelper.getWritableDatabase();
        this.context = context;
    }

    public int salvarItem(ItemBiblia item) {
        int id = item.getId();

        if (id != 0) {
            atualizar(item);
        } else {
            id = inserir(item);
        }

        return id;
    }

    private int inserir(ItemBiblia item) {
        int id = (int) db.insert(NOME_TABELA, "", valuesOfItem(item));
        return id;
    }

    private int atualizar(ItemBiblia item) {
        ContentValues values = valuesOfItem(item);
        String _id = String.valueOf(item.getId());
        String where = ItemBiblia.ID + "=?";
        String[] args = new String[]{_id};
        int count = db.update(NOME_TABELA, values, where, args);
        return count;
    }

    private ContentValues valuesOfItem(ItemBiblia item) {

        ContentValues values = new ContentValues();
        values.put(ItemBiblia.TESTAMENTO, item.getTestamento());
        values.put(ItemBiblia.LIVRO, item.getLivro());
        values.put(ItemBiblia.CAPITULO, item.getCapitulo());
        values.put(ItemBiblia.VERSICULO, item.getVersiculo());
        values.put(ItemBiblia.TEXTO, item.getTexto());
        return values;
    }

    public Livro listarLivro(int testamento, int idLivro) {
        String nomeLivro = " ";
        if (testamento == 0) {
            nomeLivro = context.getResources().getStringArray(R.array.lista_antigo_testamento_livros_array)[idLivro];
        } else {
            if(idLivro<27)
            nomeLivro = context.getResources().getStringArray(R.array.lista_novo_testamento_livros_array)[idLivro];
        }

        Livro livro = new Livro(nomeLivro);
        List<List<String>> capitulos = new ArrayList<>();
        List<String> verLis = new ArrayList<>();

        String query = "SELECT " + ItemBiblia.TEXTO + ", " + ItemBiblia.CAPITULO + " FROM " + NOME_TABELA + " WHERE " +
                ItemBiblia.TESTAMENTO + " = " + String.valueOf(testamento) + " AND " +
                ItemBiblia.LIVRO + " = " + String.valueOf(idLivro) + ";";

        Cursor cursor = db.rawQuery(query, null);

        int capIndex = cursor.getColumnIndex(ItemBiblia.CAPITULO);
        int textoIndex = cursor.getColumnIndex(ItemBiblia.TEXTO);

        int capAux = 0;
        int ver = 0;

        if (cursor.moveToFirst()) {

            do {
                int cap = cursor.getInt(capIndex);
                String texto = cursor.getString(textoIndex);

                if (capAux == cap) {
                    verLis.add(texto);
                } else {
                    capitulos.add(verLis);
                    capAux = cap;
                    verLis = new ArrayList<>();
                    verLis.add(texto);
                }

            } while (cursor.moveToNext());

            capitulos.add(verLis);


            livro.setCapitulos(capitulos);
        }

        return livro;
    }

    public void carregarBiblia() {

        String[] itens = null;

        try {
            itens = context.getResources().getAssets().list("scripts/livros");
        } catch (IOException e) {
            e.printStackTrace();
        }

        db.beginTransaction();
        SQLiteStatement statement;

        Log.i("teste_bigData", "inicio da insercion");

        for (String s : itens) {
            try {
                InputStream input = context.getAssets().open("scripts/livros/" + s);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String linha = null;
                do {
                    linha = reader.readLine();
                    if (linha != null) {
                        if(s.equals("inserir_i_joao.sql"))Log.i("teste_falha",linha);
                        statement = db.compileStatement(linha);
                        statement.execute();
                    }
                } while (linha != null);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i("teste_bigData", "fim da insercion");

    }

    public String retornaTexto(int testamento, int livro) {

        StringBuilder sbTexto = new StringBuilder();

        Livro livroAux = listarLivro(testamento, livro);

        for(List<String> cap:livroAux.getCapitulos()){
            for(String s: cap){
                sbTexto.append(s);
                sbTexto.append("\n");
            }
        }


        return sbTexto.toString();
    }


    public void recriar() {
        db.execSQL(DATA_BASE_DELETE);
        db.execSQL("CREATE TABLE biblia(id integer primary key autoincrement, testamento integer not null, livro integer not null, capitulo integer not null, versiculo integer not null, texto text not null);");
    }

    public List<ItemBiblia> buscarPesquisa(String pesquisa) {


        String query = "SELECT * FROM "+ NOME_TABELA + " WHERE " + ItemBiblia.TEXTO +
                " LIKE "+"'%"+ pesquisa+"%';";

        List<ItemBiblia> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery(query,null);

        int testaIndex = cursor.getColumnIndex(ItemBiblia.TESTAMENTO);
        int livIndex = cursor.getColumnIndex(ItemBiblia.LIVRO);
        int capIndex = cursor.getColumnIndex(ItemBiblia.CAPITULO);
        int verIndex  = cursor.getColumnIndex(ItemBiblia.VERSICULO);
        int textIndex = cursor.getColumnIndex(ItemBiblia.TEXTO);


        if(cursor.moveToFirst()){
            do{

                int test;
                int liv;
                int cap;
                int ver;
                String texto;

                test = cursor.getInt(testaIndex);
                liv = cursor.getInt(livIndex);
                cap = cursor.getInt(capIndex);
                ver = cursor.getInt(verIndex);
                texto = cursor.getString(textIndex);


                lista.add(new ItemBiblia(test,liv,cap,ver,texto));

            }while (cursor.moveToNext());
        }

       return lista;
    }

    public String retornaTexto(int testamento, int livro, int i2, int i3) {

        StringBuilder sbTexto = new StringBuilder();

        Livro livroAux = listarLivro(testamento, livro);

        return livroAux.getCapitulos().get(i2).get(i3);

    }




}
