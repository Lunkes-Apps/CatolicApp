package com.alexandrelunkes.catolicapp.paginas;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListMenuPrincipal;
import com.alexandrelunkes.catolicapp.adapters.AdapterListSimplesPergaminho;
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 02/06/2016.
 */
public class ListaOracoesFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private OnOracaoSelectedListener myCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_oracoes_lista,null);
        listView = (ListView) v.findViewById(R.id.list_oracoes);

        String[] oracoes = v.getResources().getStringArray(R.array.lista_oracoes_catolicas);

        ArrayList<String> lista = new ArrayList<>();

        for(String s: oracoes){
            lista.add(s);
        }

        listView.setAdapter(new AdapterListSimplesPergaminho(getContext(),lista));
        listView.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myCallBack = (OnOracaoSelectedListener) activity;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getAdapter().getItem(position);
        myCallBack.onSelectedOracao(item);
    }

    public interface OnOracaoSelectedListener{
        void onSelectedOracao(String oracao);
    }


}
