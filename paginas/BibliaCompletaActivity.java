package com.alexandrelunkes.catolicapp.paginas;



import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.CollectionPagerAdapter;
import com.alexandrelunkes.catolicapp.adapters.ItemCollectionPagerTestamento;

import java.util.ArrayList;

public class BibliaCompletaActivity extends AppCompatActivity
        implements ItemCollectionPagerTestamento.OnButtonButtonClickedInPager,
        ItemCollectionPagerTestamento.OnSelectedToShare{

    private CollectionPagerAdapter collectionPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblia_completa);
        setAppBar();

        collectionPagerAdapter = getCollectionTestamentos();

        setViewPagerAdapter(collectionPagerAdapter);
    }



    private void setViewPagerAdapter(CollectionPagerAdapter collectionPagerAdapter){

        viewPager = (ViewPager) findViewById(R.id.viewPager_biblia_testamentos);
        viewPager.setAdapter(collectionPagerAdapter);
       // viewPager.setCurrentItem(1);

    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);

    }



    public CollectionPagerAdapter getCollectionTestamentos() {

        ArrayList<ItemCollectionPagerTestamento> itens = new ArrayList<>();
        FragmentManager fm = this.getSupportFragmentManager();

        String[] novo = getResources().getStringArray(R.array.lista_novo_testamento_livros_array);
        String[] antigo = getResources().getStringArray(R.array.lista_antigo_testamento_livros_array);

        itens.add(ItemCollectionPagerTestamento.newInstance(antigo,"Antigo Testamento",0));
        itens.add(ItemCollectionPagerTestamento.newInstance(novo,"Novo Testamento",0));

        return new CollectionPagerAdapter(fm, itens);
    }


    @Override
    public void onButtonClicked(View view) {
        switch (view.getId()){

            case R.id.button_anterior: actionButtonAnterior();break;
            case R.id.button_proximo: actionButtonProximo();break;

            default:break;
        }



         Log.v(CatolicApp.LOG_APP,view.toString());
    }

    @Override
    public void onLivroClicked(int position) {
        Intent intent = new Intent(this,LivroCompletoActivity.class);
        intent.putExtra(LivroCompletoActivity.LIVRO_ID,position);

        int testamento = viewPager.getCurrentItem();
        intent.putExtra(LivroCompletoActivity.TESTAMENTO_ID,testamento);

        startActivity(intent);
    }

    @Override
    public void onLongButtonClicked(View view) {

    }

    private void actionButtonProximo() {
        if(viewPager.getCurrentItem()==0){
            viewPager.setCurrentItem(1);
        }else{
            Toast.makeText(this,"Última Página",Toast.LENGTH_LONG).show();
        }
    }

    private void actionButtonAnterior() {
        if(viewPager.getCurrentItem()==1){
            viewPager.setCurrentItem(0);
        }else{
            Toast.makeText(this,"Primeira Página",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSelected(String text) {

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

}
