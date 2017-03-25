package com.alexandrelunkes.catolicapp.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.Interfaces.OnButtonClickedInPager;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.ClipBoardCatolicApp;


/**
 * Created by Alexandre Lunkes on 16/06/2016.
 */
public class ItemAdapterMisterios extends Fragment implements View.OnClickListener {


    private String titulo;
    private String texto;
    private TextView tvTitulo;
    private TextView tvTexto;

    private OnButtonClickedInPager myCallBack;
    private ImageView btProx;
    private ImageView btAnt;


    public static ItemAdapterMisterios newInstance(String titulo, String texto) {

        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("texto", texto);
        ItemAdapterMisterios fragment = new ItemAdapterMisterios();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_misterio_fragment, null);

        texto = getArguments().getString("texto");
        titulo = getArguments().getString("titulo");

        btAnt = (ImageView) v.findViewById(R.id.button_anterior);
        btProx = (ImageView) v.findViewById(R.id.button_proximo);

        btAnt.setOnClickListener(this);
        btProx.setOnClickListener(this);

        tvTexto = (TextView) v.findViewById(R.id.text_view_misterio);
        tvTitulo = (TextView) v.findViewById(R.id.text_view_titulo_misterio);

        tvTexto.setText(texto);
        tvTitulo.setText(titulo);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myCallBack = (OnButtonClickedInPager) activity;
    }

    @Override
    public void onClick(View v) {
        myCallBack.onButtonClicked(v);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.oracoes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.copy:

                StringBuilder sb = new StringBuilder(titulo);
                sb.append("\n");
                sb.append("\n");
                sb.append(texto);
                sb.append("\n");
                sb.append("\n");
                ClipBoardCatolicApp clipboard = new ClipBoardCatolicApp(getActivity());
                clipboard.copiarTexto(sb.toString());
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
