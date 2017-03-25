package com.alexandrelunkes.catolicapp.paginas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandrelunkes.catolicapp.R;


/**
 * Created by Alexandre Lunkes on 03/06/2016.
 */
public class ComoRezarRosarioFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_como_rezar_rosario,null);


        return v;
    }
}
