package com.alexandrelunkes.catolicapp.paginas;

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
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 16/06/2016.
 */
public class MisteriosContempacaoFragment extends Fragment implements AdapterView.OnItemClickListener {


    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_misterios_contemplacao,null);
        listView = (ListView) v.findViewById(R.id.list_misterios);

        String[] misterios = getResources().getStringArray(R.array.lista_misterios);
        String[] subMist = getResources().getStringArray(R.array.lista_sub_misterios);

        ArrayList<ItemListViewPrincipal> lista = new ArrayList<>();

        for(int i = 0; i<misterios.length; i++){
            lista.add(new ItemListViewPrincipal(0,misterios[i],subMist[i]));
        }
        AdapterListMenuPrincipal adapter = new AdapterListMenuPrincipal(getContext(),lista);
        listView.setAdapter(adapter);


        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
