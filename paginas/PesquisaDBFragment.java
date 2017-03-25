package com.alexandrelunkes.catolicapp.paginas;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Alexandre Lunkes on 13/09/2016.
 */
public class PesquisaDBFragment extends Fragment {


    public static PesquisaDBFragment newInstance() {

        Bundle args = new Bundle();

        PesquisaDBFragment fragment = new PesquisaDBFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
