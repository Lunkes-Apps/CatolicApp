package com.alexandrelunkes.catolicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.alexandrelunkes.catolicapp.R;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 28/04/2016.
 */
public class AdapterListMenuPrincipal extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<ItemListViewPrincipal> list;

    public AdapterListMenuPrincipal(Context context, ArrayList<ItemListViewPrincipal> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ItemListViewPrincipal getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemSuport itemHolder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_listview_principal_esquerdo,null);
            itemHolder = new ItemSuport();
            itemHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView_lista_principal_esq);
            itemHolder.titulo = (TextView) convertView.findViewById(R.id.textView_titulo_lista_principal_esq);
            itemHolder.subtitulo = (TextView) convertView.findViewById(R.id.textView_subtitulo_lista_principal_esq);

            convertView.setTag(itemHolder);

        }else{
            itemHolder = (ItemSuport)convertView.getTag();
        }


        ItemListViewPrincipal item = list.get(position);
        int idImagem = item.getIdImage();


        itemHolder.imageView.setImageResource(item.getIdImage());
        itemHolder.titulo.setText(item.getTitulo());
        itemHolder.subtitulo.setText(item.getSubTitulo());

        if(idImagem == 0){
            itemHolder.imageView.getLayoutParams().height= 0;
            itemHolder.imageView.getLayoutParams().width= 0;
        }

        return convertView;
    }

    public class ItemSuport{
        private ImageView imageView;
        private TextView titulo;
        private TextView subtitulo;

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTitulo() {
            return titulo;
        }

        public void setTitulo(TextView titulo) {
            this.titulo = titulo;
        }

        public TextView getSubtitulo() {
            return subtitulo;
        }

        public void setSubtitulo(TextView subtitulo) {
            this.subtitulo = subtitulo;
        }
    }


}
