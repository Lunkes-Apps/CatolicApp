package com.alexandrelunkes.catolicapp.adapters;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.paginas.LivroCompletoActivity;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 28/04/2016.
 */
public class ItemCollectionPagerTestamento extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, View.OnLongClickListener{

    public static final String ARG_OBJECT = "object";

    private String[] itens;
    private String titulo;
    private int versiculo;
    private ArrayList<Integer> itensSelecionados;
    OnButtonButtonClickedInPager mCalback;
    OnSelectedToShare mCalbackShare;


    public static ItemCollectionPagerTestamento newInstance(String[] itens, String titulo,int versiculo) {
        Bundle args = new Bundle();

        args.putStringArray("arrayItens",itens);
        args.putString("arrayTitulo",titulo);
        args.putInt(LivroCompletoActivity.VERSICULO_ID,versiculo);

        ItemCollectionPagerTestamento fragment = new ItemCollectionPagerTestamento();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_collection_pager_testamento,null);
        Bundle args = getArguments();
        itens = args.getStringArray("arrayItens");
        titulo = args.getString("arrayTitulo");
        versiculo = args.getInt(LivroCompletoActivity.VERSICULO_ID);

        ListView listView = (ListView) view.findViewById(R.id.listView_testamento);
        AdapterListSimples adapter = new AdapterListSimples(getActivity(),itens);

        listView.setAdapter(adapter);
        listView.setSelection(versiculo);
        listView.setOnItemClickListener(this);

        TextView textView = (TextView) view.findViewById(R.id.textView_titulo_testamento);
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
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCalback = (OnButtonButtonClickedInPager) activity;
        mCalbackShare = (OnSelectedToShare) activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCalback.onLivroClicked(position);
    }

    @Override
    public void onClick(View v) {
        mCalback.onButtonClicked(v);
    }

    @Override
    public boolean onLongClick(View v) {
        mCalback.onLongButtonClicked(v);
        return false;
    }



    public interface OnSelectedToShare{
        void onSelected(String text);
    }


    public interface OnButtonButtonClickedInPager{
        public void onButtonClicked(View view);
        public void onLivroClicked(int position);
        public void onLongButtonClicked(View view);
    }

}
