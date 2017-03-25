package com.alexandrelunkes.catolicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.alexandrelunkes.catolicapp.R;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 29/04/2016.
 */
public class AdapterListSimples extends BaseAdapter {


    private String[] itens;
    private Context context;
    private ArrayList<Integer> itensSelecionados = new ArrayList<>();

    public AdapterListSimples(Context contexts, String[]itens) {
        this.context = contexts;
        this.itens = itens;
    }


    @Override
    public int getCount() {
        return itens.length;
    }

    @Override
    public Object getItem(int position) {
        return itens[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_simples,null);
            textView = (TextView) convertView.findViewById(R.id.text_item_simples);
            convertView.setTag(textView);
        }else{
            textView = (TextView) convertView.getTag();
        }
        textView.setText(itens[position]);

        return convertView;
    }


}
