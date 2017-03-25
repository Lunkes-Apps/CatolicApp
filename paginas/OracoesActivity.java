package com.alexandrelunkes.catolicapp.paginas;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.Interfaces.OnAdReturnCommand;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.AdControler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class OracoesActivity extends AppCompatActivity implements ListaOracoesFragment.OnOracaoSelectedListener,
        OnAdReturnCommand{
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oracoes);

        setAppBar();

        adView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("0815A78C9B93FCF6A6888964A047C6E1")  // All emulators
                .build();

        adView.loadAd(request);

        adView.setAdListener(new AdControler(this,this));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.oracoes_frame,new ListaOracoesFragment());
        ft.commit();
    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onOpenAd() {
        adView.destroy();
        adView.setVisibility(View.GONE);
    }

    @Override
    public void onSelectedOracao(String oracao) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.oracoes_frame,OracaoFragment.newInstance(oracao));
        ft.addToBackStack(null);
        ft.commit();
    }
}
