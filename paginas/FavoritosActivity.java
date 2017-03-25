package com.alexandrelunkes.catolicapp.paginas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.dialogs.ShareOptionsDialog;
import com.alexandrelunkes.catolicapp.favoritos.AdapterListFavoritos;
import com.alexandrelunkes.catolicapp.favoritos.Favorito;
import com.alexandrelunkes.catolicapp.favoritos.FavoritoScreenActivity;
import com.alexandrelunkes.catolicapp.favoritos.RepositorioFavoritosDAO;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Favorito> list;
    private ListView listView;
    private boolean editando = false;
    private List<Integer> itensSelecionados;
    private AdapterListFavoritos adapter;
    private TextoPergaminho textoPergaminho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        itensSelecionados = new ArrayList<>();

        setAppBar();

        if(savedInstanceState != null){
            itensSelecionados = savedInstanceState.getIntegerArrayList("itensSelecionados");
            editando = savedInstanceState.getBoolean("editando");
        }

    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setSubtitle("Leituras favoritas");

        setSupportActionBar(toolbar);
    }

    private void initializeData() {

        RepositorioFavoritosDAO dao = RepositorioFavoritosDAO.getInstance(this);

        List<Favorito> newList;

        newList = dao.listarTodoHistorico();

        for (int i = newList.size(); i < 0; i--) {
            newList.add(newList.get(i - 1));

        }
        list = newList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if (editando) {

            Favorito favorito = (Favorito) parent.getAdapter().getItem(position);
            if (favorito.getIsSelected()) {
                view.setAlpha(1F);
                favorito.setSelected(false);
                itensSelecionados.remove(new Integer(position));
                getSupportActionBar().setSubtitle(itensSelecionados.size() + " Selecionados");
            } else {
                view.setAlpha(0.5F);
                favorito.setSelected(true);
                itensSelecionados.add(new Integer(position));
                getSupportActionBar().setSubtitle(itensSelecionados.size() + " Selecionados");
            }

        } else {
            Favorito f = list.get(position);

            Intent i = new Intent(this, FavoritoScreenActivity.class);
            i.putExtra("favorito", f);

            startActivity(i);
        }

    }

    private void deletarItens() {

        RepositorioFavoritosDAO dao = RepositorioFavoritosDAO.getInstance(this);
        List<Favorito> favoritos = new ArrayList<>();

        for (Integer i : itensSelecionados) {
            Log.i("teste_deletar", "deletando item position" + i.intValue());
            dao.deletarItem(list.get(i.intValue()).getId());
        }

        for (Integer i : itensSelecionados) {
            favoritos.add(list.get(i.intValue()));
        }

        for (Favorito f : favoritos) {
            list.remove(f);
        }

        itensSelecionados.clear();

        if (list.size() == 0) {
            textoPergaminho.setVisibility(View.VISIBLE);
        } else {
            textoPergaminho.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorito_list, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listView = (ListView) findViewById(R.id.list_favoritos);
        textoPergaminho = (TextoPergaminho) findViewById(R.id.aviso);



        initializeData();

        if(editando){
            for(int i = 0; i<list.size(); i++){
               if(itensSelecionados.contains(new Integer(i))){
                   list.get(i).setSelected(true);
               }
            }
        }

        if (list.size() == 0) {
            textoPergaminho.setVisibility(View.VISIBLE);
        } else {
            textoPergaminho.setVisibility(View.INVISIBLE);
        }

        adapter = new AdapterListFavoritos(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setFastScrollEnabled(true);




    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (editando) {
            menu.findItem(R.id.editar).setVisible(false);
            menu.findItem(R.id.deletar).setVisible(true);
            getSupportActionBar().setSubtitle(itensSelecionados.size() + " Selecionados");
            getSupportActionBar().setTitle("Deletar Favoritos");

            if(itensSelecionados.size() == list.size()){
                menu.findItem(R.id.marc_tudo).setVisible(false);
            }else {
                menu.findItem(R.id.marc_tudo).setVisible(true);
            }

            if(itensSelecionados.size() == 0){
                menu.findItem(R.id.desmarc_tudo).setVisible(false);
            }else {
                menu.findItem(R.id.desmarc_tudo).setVisible(true);
            }


        } else {

            menu.findItem(R.id.editar).setVisible(true);
            menu.findItem(R.id.deletar).setVisible(false);
            menu.findItem(R.id.desmarc_tudo).setVisible(false);
            menu.findItem(R.id.marc_tudo).setVisible(false);
            setAppBar();
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.editar:
                editando = true;
                invalidateOptionsMenu();
                break;
            case R.id.deletar:
                editando = false;
                deletarItens();
                adapter.notifyDataSetChanged();
                invalidateOptionsMenu();
                break;
            case R.id.marc_tudo:
                marcarTudo();
                break;
            case R.id.desmarc_tudo:
                desmarcarTudo();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }


    private void desmarcarTudo(){
        itensSelecionados.clear();

        for(Favorito f: list){
            f.setSelected(false);
        }
        getSupportActionBar().setSubtitle(itensSelecionados.size() + " Selecionados");
        adapter.notifyDataSetChanged();
    }

    private void marcarTudo(){

        int i =0;

        for(Favorito f: list){

            f.setSelected(true);
            if(!itensSelecionados.contains(new Integer(i))){
                itensSelecionados.add(new Integer(i));
            }
            i++;
        }
        getSupportActionBar().setSubtitle(itensSelecionados.size() + " Selecionados");
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        if(editando){
            editando = false;
            desmarcarTudo();
            invalidateOptionsMenu();
        }else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("editando",editando);
        outState.putIntegerArrayList("itensSelecionados", (ArrayList<Integer>) itensSelecionados);


    }
}
