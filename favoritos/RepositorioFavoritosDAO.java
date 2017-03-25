package com.alexandrelunkes.catolicapp.favoritos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alexandrelunkes.catolicapp.SqliteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 21/08/2016.
 */
public class RepositorioFavoritosDAO {

    //Nome da tabela
    protected static final String NOME_TABELA = "favoritos";
    private static final int VERSAO = 1;

    public static final String DATABASE_CREATE = "CREATE TABLE " + NOME_TABELA +
            "(" + Favorito._ID +" integer primary key autoincrement, " +
            Favorito.PASSAGEM +" text not null, "+
            Favorito.ANOTACAO +" text, "+
            Favorito.DATA +" text not null);";

    public static final String DATA_BASE_DELETE = "DROP TABLE IF EXISTS " + NOME_TABELA;

    private SQLiteDatabase db;
    private static RepositorioFavoritosDAO instance;

    public static RepositorioFavoritosDAO getInstance(Context context){
        if(instance == null) {
            instance = new RepositorioFavoritosDAO(context);
        }
        return instance;
    }

    private RepositorioFavoritosDAO(Context context){
        SqliteHelper sqlHelper = SqliteHelper.getInstance(context);
        db = sqlHelper.getWritableDatabase();
    }

    public int salvarFavorito(Favorito item){
        int id = item.getId();

        if(id!=0){
            atualizar(item);
        }else {
            id = inserir(item);
        }

        return id;
    }

    public int inserir(Favorito item){
        int id = (int) db.insert(NOME_TABELA,"",valuesItemFavorito(item));
        return id;
    }

    public ContentValues valuesItemFavorito(Favorito item){
        ContentValues values = new ContentValues();
        values.put(Favorito.PASSAGEM, item.getPassagem());
        values.put(Favorito.DATA,item.getData());
        values.put(Favorito.ANOTACAO,item.getAnotacao());

        return values;
    }

    public int atualizar(Favorito item){
        ContentValues values = valuesItemFavorito(item);
        String _id = String.valueOf(item.getId());
        String where = Favorito._ID + "=?";
        String[] args = new String[]{_id};
        int count =  db.update(NOME_TABELA,values,where,args);
        Log.i("teste_atualizar","atualizado comentario "+item.getAnotacao());
        return count;
    }

    public int deletarItem(int id){
        String where = Favorito._ID + "=?";
        String _id = String.valueOf(id);
        String[] args = new String[]{_id};
        int count = db.delete(NOME_TABELA,where,args);
        Log.i("teste_deletar","deletando item de id "+id);
        return count;
    }

    public List<Favorito> listarTodoHistorico(){
        //Select * from carro where _id=?
        List<Favorito> lista = new ArrayList<>();
        String query = "SELECT * FROM " + NOME_TABELA;

        Cursor cursor = db.rawQuery(query,null);

        int indexId = cursor.getColumnIndex(Favorito._ID);
        int indexPassagem = cursor.getColumnIndex(Favorito.PASSAGEM);
        int indexData = cursor.getColumnIndex(Favorito.DATA);
        int indexAnota = cursor.getColumnIndex(Favorito.ANOTACAO);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(indexId);
                String passagem = cursor.getString(indexPassagem);
                String data = cursor.getString(indexData);
                String anota = cursor.getString(indexAnota);

                Favorito item = new Favorito();
                item.setId(id);
                item.setPassagem(passagem);
                item.setData(data);
                item.setAnotacao(anota);

                if (anota==null||anota.equals("")){
                    item.setHasAnota(false);
                }else {
                    item.setHasAnota(true);
                }

                lista.add(item);

            }while (cursor.moveToNext());
        }
        return lista;
    }


}
