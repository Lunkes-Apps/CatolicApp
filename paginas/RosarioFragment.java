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
 * Created by Alexandre Lunkes on 03/06/2016.
 */
public class RosarioFragment extends Fragment implements AdapterView.OnItemClickListener {

     OnOracaoRosarioSelectedListener myCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rosario,null);

        ListView listView = (ListView) v.findViewById(R.id.list_rosario);
        String[] lista = getResources().getStringArray(R.array.lista_rosario);
        ArrayList<String> listaArray = new ArrayList<>();
        for(String s: lista){
            listaArray.add(s);
        }

        AdapterListSimplesPergaminho adapter = new AdapterListSimplesPergaminho(getContext(),listaArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myCallBack = (OnOracaoRosarioSelectedListener) activity;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String item = (String) parent.getAdapter().getItem(position);

        myCallBack.OnSelectOracaoRosario(item,position);

    }

    public interface OnOracaoRosarioSelectedListener{

        void OnSelectOracaoRosario(String item, int position);

    }

}
