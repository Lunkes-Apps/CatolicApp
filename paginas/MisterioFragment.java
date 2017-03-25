package com.alexandrelunkes.catolicapp.paginas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.alexandrelunkes.catolicapp.Interfaces.OnButtonClickedInPager;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.CollectionPagerMisterios;
import com.alexandrelunkes.catolicapp.adapters.ItemAdapterMisterios;
import com.alexandrelunkes.catolicapp.itens.ToolbarNewFont;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 16/06/2016.
 */
public class MisterioFragment extends Fragment implements OnButtonClickedInPager{

    private ViewPager viewPager;
    private CollectionPagerMisterios collection;
    private int position;
    private String titulo;
    private String texto;
    private ArrayList<ItemAdapterMisterios> lista;


    public static MisterioFragment newInstance(int position) {

        Bundle args = new Bundle();

        args.putInt("position",position);

        MisterioFragment fragment = new MisterioFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_misterio,null);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager_misterio);

        position = getArguments().getInt("position");
        lista = new ArrayList<>();

        String[] aux = {"Primeiro Mistério","Segundo Mistério","Terciro Mistério","Quarto Mistério","Quinto Mistério"};
        String[] auxMist = {"Gozosos","Luminoso","Doloroso","Glorioso"};

        String[]textos = getResources().getStringArray(R.array.lista_textos_misterios_gozosos);


        for(int i = 0; i<aux.length; i++){
            ItemAdapterMisterios pagina = ItemAdapterMisterios.newInstance(aux[i]+" "+auxMist[position],textos[i]);
            lista.add(pagina);
        }

        collection = new CollectionPagerMisterios(getFragmentManager(),lista);
        viewPager.setAdapter(collection);
        return v;
    }

    @Override
    public void onButtonClicked(View view) {
           switch (view.getId()){
               case R.id.button_anterior:
                   executaAnterior();break;
               case R.id.button_proximo:
                   executaProximo();
           }
    }

    private void executaProximo() {

        if(viewPager.getCurrentItem()==(viewPager.getChildCount()-1)){
            Toast.makeText(getContext(),"Última página",Toast.LENGTH_LONG).show();
        }else {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }

    }

    private void executaAnterior() {

        if(viewPager.getCurrentItem() == 0){
            Toast.makeText(getContext(),"Primeira página",Toast.LENGTH_LONG).show();
        }else {
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }

    }

    @Override
    public void onLongButtonClicked(View view) {

    }

    @Override
    public void onItemSelectedToShare(int position) {

    }
}
