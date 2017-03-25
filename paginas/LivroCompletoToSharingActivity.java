package com.alexandrelunkes.catolicapp.paginas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.Interfaces.OnButtonClickedInPager;
import com.alexandrelunkes.catolicapp.Interfaces.OnSelectedToShare;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.CollectionPagerLivros;
import com.alexandrelunkes.catolicapp.adapters.ItemAdapterPagerLivro;
import com.alexandrelunkes.catolicapp.adapters.ItemCollectionPagerTestamento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LivroCompletoToSharingActivity extends AppCompatActivity implements
        OnButtonClickedInPager,
        ItemCollectionPagerTestamento.OnSelectedToShare,
        OnSelectedToShare {

    private ViewPager viewPager;
    private LeitorDeBiblia leitorDeBiblia;
    private ArrayList<String[]> livroCompleto;
    private ArrayList<Integer> selecionados;
    private int capitulo = 0;
    private int versiculo = 0;
    private int pagAtual;
    private int pagFinal;
    private boolean longClicked = false;
    private boolean isShare = false;
    private int idShare = 0;
    private String livro;
    private SuportShareItem suport = new SuportShareItem();


    public static final String LIVRO_ID = "livroId";
    public static final String TESTAMENTO_ID = "testamentoId";
    public static final String CAPITULO_ID = "capituloId";
    public static final String VERSICULO_ID = "versId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro_completo);



        Toast.makeText(this, "Selecione os Versículos", Toast.LENGTH_LONG).show();



        leitorDeBiblia = new LeitorDeBiblia(this);
        Bundle bundle = getIntent().getExtras();


        int liv = (int) bundle.get(LIVRO_ID);
        int testamento = (int) bundle.get(TESTAMENTO_ID);
        if (bundle.get(CAPITULO_ID) != null) {
            capitulo = (int) bundle.get(CAPITULO_ID);
        }
        pagAtual = capitulo;

        if (bundle.get(VERSICULO_ID) != null) {
            versiculo = (int) bundle.get(VERSICULO_ID);
        }

        if (testamento == 0) {
            livro = leitorDeBiblia.getLivrosAntigoTestamento()[liv];
        } else {
            livro = leitorDeBiblia.getLivrosNovoTestamento()[liv];
        }


        livroCompleto = leitorDeBiblia.getLivroDaBiblia(livro);
        pagFinal = livroCompleto.size() - 1;

        setViewPagerAdapter(getCollection());

        if (savedInstanceState != null) {
            isShare = savedInstanceState.getBoolean("isShare");
            idShare = savedInstanceState.getInt("idShare");
            suport = (SuportShareItem)savedInstanceState.getSerializable("suport");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idShare", idShare);
        outState.putBoolean("isShare", isShare);
        outState.putSerializable("suport",suport);

    }

    private void setViewPagerAdapter(CollectionPagerLivros collectionPagerAdapter) {
        viewPager = (ViewPager) findViewById(R.id.viewPager_livro);
        viewPager.setAdapter(collectionPagerAdapter);
        viewPager.setCurrentItem(capitulo);
    }


    public CollectionPagerLivros getCollection() {

        ArrayList<ItemAdapterPagerLivro> itens = new ArrayList<>();
        FragmentManager fm = this.getSupportFragmentManager();
        int i = 1;

        for (String[] pag : livroCompleto) {
            if (i == capitulo + 1) {
                itens.add(ItemAdapterPagerLivro.newInstance(pag, "Capítulo " + i, versiculo));
            } else {
                itens.add(ItemAdapterPagerLivro.newInstance(pag, "Capítulo " + i, 0));
            }
            i++;
        }
        return new CollectionPagerLivros(fm, itens);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);

        MenuItem share = menu.add(0, 0, 0, "item novo");
        share.setIcon(R.drawable.ic_share_variant);
        share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 0:
                Intent i = new Intent(this, CompartilharComFaceBook.class);
                i.putExtra("suport",suport);
                startActivity(i);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void listaToSelectMode() {
//        invalidateOptionsMenu();
//        if(isShare){
//            isShare = false;
//        }else {
//            isShare = true;
//        }
    }


    @Override
    public void onButtonClicked(View view) {

        if (!longClicked) {
            switch (view.getId()) {
                case R.id.button_anterior:
                    executarAnterior();
                    break;
                case R.id.button_proximo:
                    executarProximo();
                    break;
                default:
                    break;
            }
        } else {
            longClicked = false;
        }

    }

    private void executarProximo() {
        pagAtual = viewPager.getCurrentItem();
        CatolicApp.logCatolicApp("pagina atual " + pagAtual);
        if (pagAtual == pagFinal) {
            Toast.makeText(this, "Última Página", Toast.LENGTH_LONG).show();
        } else {
            pagAtual++;
            viewPager.setCurrentItem(pagAtual);
        }

    }

    private void executarAnterior() {

        pagAtual = viewPager.getCurrentItem();

        CatolicApp.logCatolicApp("pagina atual " + pagAtual);
        if (pagAtual == 0) {
            Toast.makeText(this, "Primeira Página", Toast.LENGTH_LONG).show();
        } else {
            pagAtual--;
            viewPager.setCurrentItem(pagAtual);
        }

    }


    @Override
    public void onLongButtonClicked(View view) {
        longClicked = true;

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);

        View v = layoutInflater.inflate(R.layout.layout_spinner_popup, null);
        final PopupWindow spinnerPopUp = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner_caps);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.isSelected()) {
                    spinnerPopUp.dismiss();
                }
                spinner.setSelected(true);
                viewPager.setCurrentItem(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> listaArray = new ArrayList<>();
        int cont = 1;

        for (String[] pag : livroCompleto) {
            listaArray.add(new String("Capítulo " + cont));
            cont++;
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                listaArray.toArray(new String[listaArray.size()]));
        CatolicApp.logCatolicApp("aqui " + spinner.toString());

        spinner.setAdapter(adapterSpinner);
        spinnerPopUp.showAsDropDown(view);
    }

    @Override
    public void onItemSelectedToShare(int position) {

    }

    @Override
    public void onSelected(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isSharedMode() {
        return true;
    }

    @Override
    public void setIsShared(boolean isShared) {
        this.isShare = isShared;
        invalidateOptionsMenu();
    }

    @Override
    public void cancelShare() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onSelectItemToShare(int position) {

        suport.getItemPosition().add(position);
        suport.getPagPosition().add(viewPager.getCurrentItem());

    }

    @Override
    public void onGetVersiculosToShare(String[] versiculos, List<Integer> itens) {

    }

    @Override
    public boolean clearItens() {
        return false;
    }

    public class SuportShareItem implements Serializable{
        private ArrayList<Integer> pagPosition = new ArrayList<>();
        private ArrayList<Integer> itemPosition = new ArrayList<>();
        private int liv;

        public ArrayList<Integer> getPagPosition() {
            return pagPosition;
        }

        public void setPagPosition(ArrayList<Integer> pagPosition) {
            this.pagPosition = pagPosition;
        }

        public ArrayList<Integer> getItemPosition() {
            return itemPosition;
        }

        public void setItemPosition(ArrayList<Integer> itemPosition) {
            this.itemPosition = itemPosition;
        }
    }




}
