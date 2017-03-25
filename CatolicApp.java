package com.alexandrelunkes.catolicapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alexandrelunkes.catolicapp.Interfaces.OnAdReturnCommand;
import com.alexandrelunkes.catolicapp.adapters.AdapterListMenuPrincipal;
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioAgendaDAO;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;
import com.alexandrelunkes.catolicapp.itens.AdControler;
import com.alexandrelunkes.catolicapp.paginas.BibliaCompletaActivity;
import com.alexandrelunkes.catolicapp.paginas.FavoritosActivity;
import com.alexandrelunkes.catolicapp.paginas.LiturgiaDiariaActivity;
import com.alexandrelunkes.catolicapp.paginas.OracoesActivity;
import com.alexandrelunkes.catolicapp.paginas.ResultadoDaPesquisaActivity;
import com.alexandrelunkes.catolicapp.paginas.RosarioActivity;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;


public class CatolicApp extends AppCompatActivity implements AdapterView.OnItemClickListener,
        LocationListener, OnAdReturnCommand{

    private static final String PREFERENCES = "preferences_dados";
    public static final String LOG_APP = "Log CatolicApp ";
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "teste.lunkes.teste@gmail.com";
    private static final String TWITTER_SECRET = "twitterTesteAndroid";

    private ListView listViewPrincipal;
    private String[] titulosArray;
    private String[] subTitulosArray;
    private ArrayList<ItemListViewPrincipal> listItens;
    private AdView adView;
    private Location myLocation;
    private ProgressDialog progressDialog;
    private boolean adOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catolic_app);

        SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        if (settings.getBoolean("ja_carregou", false)) {

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            carregarDados();
            Log.i("teste_carregamento", "dados da biblia carregado");
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("ja_carregou", true);
            editor.commit();
        }


        //Iniciando Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Inicinaod Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Iniciando Admob


        MobileAds.initialize(getApplicationContext(), "...........................");
        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("..............................")
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdControler(this,this));


        if (myLocation != null) {

        }

        configurarViews();

        setAppBar();
    }


    private void carregarDados() {

        ProgressDialog progress = ProgressDialog.show(this, "Aguarde", "Carregando dados da Bíblia");
        RepositorioBibliaDAO dao = RepositorioBibliaDAO.getInstance(this);
        dao.recriar();
        dao.carregarBiblia();

        progress.dismiss();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    }


    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_pagina_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorito:
                Intent i = new Intent(this, FavoritosActivity.class);
                startActivity(i);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void configurarViews() {
        listViewPrincipal = (ListView) findViewById(R.id.listView_principal);
        titulosArray = getResources().getStringArray(R.array.titulos);
        subTitulosArray = getResources().getStringArray(R.array.SubTitulos);
        listItens = new ArrayList<ItemListViewPrincipal>();
        int[] idsIcon = {R.drawable.biblia_icone, R.drawable.eucaristia_icone,
                R.drawable.espirito_santo_icone, R.drawable.crucifixo_icone, R.mipmap.ic_launcher};

        int i = 0;
        for (String s : titulosArray) {
            listItens.add(new ItemListViewPrincipal(idsIcon[i], s, subTitulosArray[i]));
            i++;
        }


        Log.v(LOG_APP, listItens.get(1).getTitulo());

        listViewPrincipal.setAdapter(new AdapterListMenuPrincipal(this, listItens));
        listViewPrincipal.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        switch (position) {
            case 0:
                Intent intent0 = new Intent(this, BibliaCompletaActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(this, LiturgiaDiariaActivity.class);
                startActivity(intent1);
                break;

            case 2:
                Intent intent2 = new Intent(this, ResultadoDaPesquisaActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, OracoesActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, RosarioActivity.class);
                startActivity(intent4);
                break;

            default:
                break;
        }

    }


    static public void logCatolicApp(String s) {
        Log.v(LOG_APP, s);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onOpenAd() {
        adView.destroy();
        adView.setVisibility(View.GONE);
    }




}



