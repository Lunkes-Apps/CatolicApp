package com.alexandrelunkes.catolicapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.ItemListLivro;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 25/05/2016.
 */
public class AdapaterListLivro extends BaseAdapter {

    private ArrayList<ItemListLivro> lista;
    private LayoutInflater inflater;
    private static final String LOG = "AdapterListLivro_LOG";


    public AdapaterListLivro(Context context,ArrayList<ItemListLivro> lista) {
        this.lista = lista;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        CatolicApp.logCatolicApp(LOG+"getCount");

        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        CatolicApp.logCatolicApp(LOG+"getItem");
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        CatolicApp.logCatolicApp(LOG+"getItemId");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CatolicApp.logCatolicApp(LOG+"getView "+position);
        SuportItemLivro suportItemLivro;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_list_simples,null);
            suportItemLivro = new SuportItemLivro();
            suportItemLivro.texto = (TextView) convertView.findViewById(R.id.text_item_simples);
            convertView.setTag(suportItemLivro);
        }else {
            suportItemLivro = (SuportItemLivro) convertView.getTag();
        }

        if(lista.get(position).isSelecionado()){
            suportItemLivro.texto.setText(lista.get(position).getTexto());
            suportItemLivro.texto.setBackgroundColor(Color.argb(255,220,196,154));
        }else {
            suportItemLivro.texto.setText(lista.get(position).getTexto());
            suportItemLivro.texto.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        CatolicApp.logCatolicApp(LOG+"getViewTypeCount");
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class SuportItemLivro{
        TextView texto;
        boolean selecionado;

    }


}
