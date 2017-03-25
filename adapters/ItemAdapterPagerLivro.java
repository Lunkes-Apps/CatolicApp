package com.alexandrelunkes.catolicapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrelunkes.catolicapp.Interfaces.OnButtonClickedInPager;
import com.alexandrelunkes.catolicapp.Interfaces.OnSelectedToShare;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.dialogs.AdicionarComentarioDialogo;
import com.alexandrelunkes.catolicapp.dialogs.ShareOptionsDialog;
import com.alexandrelunkes.catolicapp.favoritos.Favorito;
import com.alexandrelunkes.catolicapp.itens.ItemListLivro;
import com.alexandrelunkes.catolicapp.paginas.FavoritosActivity;
import com.alexandrelunkes.catolicapp.paginas.LivroCompletoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Alexandre Lunkes on 25/05/2016.
 */
public class ItemAdapterPagerLivro extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        View.OnClickListener, View.OnLongClickListener{



    private TextView textView;
    private ArrayList<ItemListLivro> lista = new ArrayList<>();
    private String[] itens;
    private String titulo;
    private int versiculo;
    private ArrayList<Integer> itensSelecionados = new ArrayList<>();
    private OnButtonClickedInPager myCallback;
    private OnSelectedToShare myCallbackShare;
    private boolean isShared = false;

    public static ItemAdapterPagerLivro newInstance(String[] itens, String titulo,int versiculo) {
        Bundle args = new Bundle();

        args.putStringArray("arrayItens",itens);
        args.putString("arrayTitulo",titulo);
        args.putInt(LivroCompletoActivity.VERSICULO_ID,versiculo);

        ItemAdapterPagerLivro fragment = new ItemAdapterPagerLivro();
        fragment.setArguments(args);
        return fragment;
    }


    public void deSelectAll(){
       itensSelecionados = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_collection_pager_testamento,null);

        Bundle args = getArguments();
        itens = args.getStringArray("arrayItens");
        titulo = args.getString("arrayTitulo");
        versiculo = args.getInt(LivroCompletoActivity.VERSICULO_ID);


        for(String s: itens){
           lista.add(new ItemListLivro(s,false));
        }

        if(savedInstanceState!=null){
            itensSelecionados = savedInstanceState.getIntegerArrayList("Selecionados");
            isShared = savedInstanceState.getBoolean("isShared");
            for(Integer integer: itensSelecionados){
                lista.get(integer.intValue()).setSelecionado(true);
            }
        }


        ListView listView = (ListView) view.findViewById(R.id.listView_testamento);
        AdapaterListLivro adapter = new AdapaterListLivro(getContext(),lista);

        listView.setAdapter(adapter);
        listView.setSelection(versiculo);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        textView = (TextView) view.findViewById(R.id.textView_titulo_testamento);
        textView.setText(titulo);
        textView.setTag(textView.getText().toString());
        textView.setOnClickListener(this);

        ImageView anterior = (ImageView) view.findViewById(R.id.button_anterior);
        ImageView proximo =(ImageView) view.findViewById(R.id.button_proximo);

        anterior.setOnClickListener(this);
        proximo.setOnClickListener(this);

        anterior.setOnLongClickListener(this);
        proximo.setOnLongClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("Selecionados",itensSelecionados);
        outState.putBoolean("isShared",isShared);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myCallback = (OnButtonClickedInPager) activity;
        myCallbackShare = (OnSelectedToShare) activity;

    }

    @Override
    public void onClick(View v) {
        if(myCallbackShare.isSharedMode()){
            Toast.makeText(getContext(),"Você tem versículos selecionados",Toast.LENGTH_LONG).show();
        }else {
            myCallback.onButtonClicked(v);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

           AdapaterListLivro.SuportItemLivro  item = (AdapaterListLivro.SuportItemLivro) view.getTag();
           ItemListLivro i = (ItemListLivro) parent.getAdapter().getItem(position);


         isShared = myCallbackShare.isSharedMode();

         if(isShared){

             if(i.isSelecionado()){
                 item.texto.setBackgroundColor(Color.TRANSPARENT);
                 i.setSelecionado(false);
                 itensSelecionados.remove(new Integer(position));
                 Log.i("teste_size", "size "+itensSelecionados.size());

                 if(itensSelecionados.size()==0){
                     Log.i("teste_size", "size if 0");
                     myCallbackShare.setIsShared(false);
                 }
                 Log.i("teste_size","isShared "+isShared+" position "+position);
                 myCallbackShare.onGetVersiculosToShare(getVersiculos(itensSelecionados),itensSelecionados);
             }else {
                 item.texto.setBackgroundColor(Color.argb(255,220,196,154));
                 i.setSelecionado(true);
                 itensSelecionados.add(new Integer(position));
                 Log.i("teste_size", "size "+itensSelecionados.size());
                 myCallbackShare.onGetVersiculosToShare(getVersiculos(itensSelecionados),itensSelecionados);

                 Log.i("teste_share","isShared "+isShared+" position "+position);
             }

         }

        Log.i("teste_share","isShared "+isShared+" position "+position);
    }

    private String[] getVersiculos(ArrayList<Integer> itensSelecionados) {
        ArrayList<String> vers = new ArrayList<>();

        for(Integer i: itensSelecionados){
            vers.add(lista.get(i.intValue()).getTexto());
        }
        return vers.toArray(new String[vers.size()]);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AdapaterListLivro.SuportItemLivro  item = (AdapaterListLivro.SuportItemLivro) view.getTag();
        ItemListLivro i = (ItemListLivro) parent.getAdapter().getItem(position);

        isShared = myCallbackShare.isSharedMode();

       if(!isShared){
            isShared = true;
            item.texto.setBackgroundColor(Color.argb(255,220,196,154));
            i.setSelecionado(true);
            itensSelecionados.add(new Integer(position));
            myCallbackShare.onGetVersiculosToShare(getVersiculos(itensSelecionados),itensSelecionados);
            myCallbackShare.setIsShared(true);
       }

        return true;
    }

    @Override
    public boolean onLongClick(View v) {

        myCallback.onLongButtonClicked(textView);
        return false;
    }


}
