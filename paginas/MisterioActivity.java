package com.alexandrelunkes.catolicapp.paginas;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.alexandrelunkes.catolicapp.Interfaces.OnButtonClickedInPager;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.CollectionPagerMisterios;
import com.alexandrelunkes.catolicapp.adapters.ItemAdapterMisterios;

import java.util.ArrayList;

public class MisterioActivity extends AppCompatActivity implements OnButtonClickedInPager {

    private ViewPager viewPager;
    private int position;
    private ArrayList<ItemAdapterMisterios> lista;
    private CollectionPagerMisterios collection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misterio);

        setAppBar();

        viewPager = (ViewPager) findViewById(R.id.viewPager_misterio);

        lista = new ArrayList<>();

        Bundle args = getIntent().getExtras();
        position = args.getInt("position");

        String[] aux = {"Primeiro Mistério", "Segundo Mistério", "Terceiro Mistério", "Quarto Mistério", "Quinto Mistério"};
        String[] auxMist = {"Gozoso", "Luminoso", "Doloroso", "Glorioso"};

        String[] textos = null;
        switch (position) {
            case 0:
                textos = getResources().getStringArray(R.array.lista_textos_misterios_gozosos);
                break;
            case 1:
                textos = getResources().getStringArray(R.array.lista_textos_misterios_luminosos);
                break;
            case 2:
                textos = getResources().getStringArray(R.array.lista_textos_misterios_dolorosos);
                break;
            case 3:
                textos = getResources().getStringArray(R.array.lista_textos_misterios_gloriosos);
                break;
            default:
                break;
        }

        for (int i = 0; i < aux.length; i++) {
            lista.add(ItemAdapterMisterios.newInstance(aux[i] + " " + auxMist[position], textos[i]));
        }

        collection = new CollectionPagerMisterios(getSupportFragmentManager(), lista);
        viewPager.setAdapter(collection);

    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.button_anterior:
                executaAnterior();
                break;
            case R.id.button_proximo:
                executaProximo();
        }

    }

    @Override
    public void onLongButtonClicked(View view) {

    }

    @Override
    public void onItemSelectedToShare(int position) {

    }


    private void executaProximo() {

        if (viewPager.getCurrentItem() == (viewPager.getAdapter().getCount() - 1)) {
            Toast.makeText(this, "Última página", Toast.LENGTH_LONG).show();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }

    }

    private void executaAnterior() {

        if (viewPager.getCurrentItem() == 0) {
            Toast.makeText(this, "Primeira página", Toast.LENGTH_LONG).show();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }

    }

}
