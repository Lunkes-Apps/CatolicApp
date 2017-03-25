package com.alexandrelunkes.catolicapp.paginas;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.alexandrelunkes.catolicapp.Interfaces.OnAdReturnCommand;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListMenuPrincipal;
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;
import com.alexandrelunkes.catolicapp.itens.AdControler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class MisteriosContemplacaoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        OnAdReturnCommand{

    private ListView listView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misterios_contemplacao);

        setAppBar();

        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("0815A78C9B93FCF6A6888964A047C6E1")
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdControler(this,this));

        listView = (ListView) findViewById(R.id.list_misterios);

        String[] misterios = getResources().getStringArray(R.array.lista_misterios);
        String[] subMist = getResources().getStringArray(R.array.lista_sub_misterios);

        ArrayList<ItemListViewPrincipal> lista = new ArrayList<>();

        for (int i = 0; i < misterios.length; i++) {
            lista.add(new ItemListViewPrincipal(0, misterios[i], subMist[i]));
        }
        AdapterListMenuPrincipal adapter = new AdapterListMenuPrincipal(this, lista);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onOpenAd() {
        adView.destroy();
        adView.setVisibility(View.GONE);
    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, MisterioActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }
}
