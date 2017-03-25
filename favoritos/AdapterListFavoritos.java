package com.alexandrelunkes.catolicapp.favoritos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 21/08/2016.
 */
public class AdapterListFavoritos extends BaseAdapter {

    private List<Favorito> favoritos;
    private LayoutInflater inflater;

    public AdapterListFavoritos(Context context, List<Favorito> list) {
        inflater = LayoutInflater.from(context);
        this.favoritos = list;
    }


    @Override
    public int getCount() {
        return favoritos.size();
    }

    @Override
    public Object getItem(int position) {
        return favoritos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_adapter_layout_favorito, null);

            holder = new Holder();
            holder.cont = (TextView) convertView.findViewById(R.id.item_id);
            holder.passagem = (TextView) convertView.findViewById(R.id.item_passagem);
            holder.data = (TextView) convertView.findViewById(R.id.item_data);
            holder.anote = (ImageView) convertView.findViewById(R.id.item_anota);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.anote.setAlpha(favoritos.get(position).getAlfa());
        holder.passagem.setText(favoritos.get(position).getPassagem());
        holder.data.setText(favoritos.get(position).getData());
        holder.cont.setText(Integer.toString(position+1));
        holder.isSelected = favoritos.get(position).getIsSelected();

        if(holder.isSelected){
            convertView.setAlpha(0.5F);
        }else {
            convertView.setAlpha(1F);
        }

        return convertView;
    }

    public class Holder {

        private TextView cont;
        private TextView passagem;
        private TextView data;
        private ImageView anote;
        public boolean isSelected = false;

        public TextView getPassagem() {
            return passagem;
        }

        public TextView getCont() {
            return cont;
        }

        public void setCont(TextView cont) {
            this.cont = cont;
        }

        public void setPassagem(TextView passagem) {
            this.passagem = passagem;
        }

        public TextView getData() {
            return data;
        }

        public void setData(TextView data) {
            this.data = data;
        }


    }


}
