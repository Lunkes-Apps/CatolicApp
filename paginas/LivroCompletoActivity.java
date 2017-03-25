package com.alexandrelunkes.catolicapp.paginas;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.Interfaces.OnButtonClickedInPager;
import com.alexandrelunkes.catolicapp.Interfaces.OnSelectedToShare;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListSimplesSelector;
import com.alexandrelunkes.catolicapp.adapters.CollectionPagerLivros;
import com.alexandrelunkes.catolicapp.adapters.ItemAdapterPagerLivro;
import com.alexandrelunkes.catolicapp.adapters.ItemCollectionPagerTestamento;
import com.alexandrelunkes.catolicapp.bibliaeagenda.Livro;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;
import com.alexandrelunkes.catolicapp.dialogs.AdicionarComentarioDialogo;
import com.alexandrelunkes.catolicapp.dialogs.ShareOptionsDialog;
import com.alexandrelunkes.catolicapp.favoritos.Favorito;
import com.alexandrelunkes.catolicapp.favoritos.RepositorioFavoritosDAO;
import com.alexandrelunkes.catolicapp.itens.CompartilhadorDeBitmap;
import com.alexandrelunkes.catolicapp.itens.ToolbarNewFont;
import com.alexandrelunkes.catolicapp.itens.ViewPagerCustom;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LivroCompletoActivity extends AppCompatActivity implements
        OnButtonClickedInPager,
        ItemCollectionPagerTestamento.OnSelectedToShare,
        OnSelectedToShare,
        ShareOptionsDialog.OnShareClickedListener, AdicionarComentarioDialogo.OnReceiveAnswerListener {


    public static final int INICIO_TEXTO = 100;
    public static final int INICIO_PARAGRAFO = 50;
    public static final int TAM_LETRA = 15;
    public static final int TAM_LINHA = TAM_LETRA * 69;
    public static final int LARGURA_PAG = INICIO_PARAGRAFO + TAM_LINHA;
    public static final int ALTURA_LINHA = 35;

    private ViewPagerCustom viewPager;
    private LeitorDeBiblia leitorDeBiblia;
    private ArrayList<String[]> livroCompleto;
    private List<Integer> selecionados;
    private int capitulo = 0;
    private int versiculo = 0;
    private int pagAtual;
    private int pagFinal;
    private boolean longClicked = false;
    private int idShare = 0;
    private String livro;
    private int liv;
    private int testamento;
    private boolean isShared;
    private String[] itensToShare;

    public static final String LIVRO_ID = "livroId";
    public static final String TESTAMENTO_ID = "testamentoId";
    public static final String CAPITULO_ID = "capituloId";
    public static final String VERSICULO_ID = "versId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro_completo);

        if (savedInstanceState != null) {
            isShared = savedInstanceState.getBoolean("isShared");
            selecionados = savedInstanceState.getIntegerArrayList("selecionados");
            invalidateOptionsMenu();
            capitulo = savedInstanceState.getInt("capitulo");
        }


//      leitorDeBiblia = new LeitorDeBiblia(this);
        Bundle bundle = getIntent().getExtras();

        liv = (int) bundle.get(LIVRO_ID);
        testamento = (int) bundle.get(TESTAMENTO_ID);
        if (bundle.get(CAPITULO_ID) != null) {
            capitulo = (int) bundle.get(CAPITULO_ID);
        }
        pagAtual = capitulo;

        if (bundle.get(VERSICULO_ID) != null) {
            versiculo = (int) bundle.get(VERSICULO_ID);
        }


        RepositorioBibliaDAO bibliaDAO = RepositorioBibliaDAO.getInstance(this);
        Livro livroBanco = bibliaDAO.listarLivro(testamento, liv);

        if (testamento == 0) {
            livro = getResources().getStringArray(R.array.lista_antigo_testamento_livros_array)[liv];
        } else {
            livro = getResources().getStringArray(R.array.lista_novo_testamento_livros_array)[liv];
        }

        livroCompleto = livroBanco.getLivro();
        //livroCompleto = leitorDeBiblia.getLivroDaBiblia(livro);
        pagFinal = livroCompleto.size() - 1;

        setViewPagerAdapter(getCollection());

        setAppBar();

    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        toolbar.setSubtitle(livro);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isShared", isShared);
        outState.putIntegerArrayList("selecionados", (ArrayList<Integer>) selecionados);
        outState.putInt("capitulo", viewPager.getCurrentItem());
    }

    private void setViewPagerAdapter(CollectionPagerLivros collectionPagerAdapter) {
        viewPager = (ViewPagerCustom) findViewById(R.id.viewPager_livro);
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

        menuInflater.inflate(R.menu.menu_sharing_moment, menu);
        menu.findItem(R.id.limpar).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if (isShared) {
                    ShareOptionsDialog dialog = new ShareOptionsDialog();
                    dialog.setOnShareClickedListener(this);
                    dialog.show(getFragmentManager().beginTransaction(), "shareOptions");

                } else {
                    Toast.makeText(this, "Você não tem versículos selecionados", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.favoritos:
                if (isShared) {
                    AdicionarComentarioDialogo dialogo = AdicionarComentarioDialogo.newInstance();
                    dialogo.show(getFragmentManager().beginTransaction(), "adicionarFav");

                } else {
                    Intent i = new Intent(this, FavoritosActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.limpar:
                limparSelecoes();

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void limparSelecoes() {
        Log.i("teste_limpar", "limpar da activity");
        selecionados.clear();

        int pag = viewPager.getCurrentItem();
        setViewPagerAdapter(getCollection());
        isShared = false;
        invalidateOptionsMenu();
        viewPager.setSwipeEnabled(true);
        viewPager.setCurrentItem(pag);
    }


    private void listaToSelectMode() {
        invalidateOptionsMenu();
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

        View v = layoutInflater.inflate(R.layout.seletor_de_capitulo, null);
        final PopupWindow spinnerPopUp = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        ListView listView = (ListView) v.findViewById(R.id.lista_capitulos);
        ArrayList<String> listaArray = new ArrayList<>();
        int cont = 1;

        for (String[] pag : livroCompleto) {
            listaArray.add(new String("Capítulo " + cont));
            cont++;
        }

        listView.setAdapter(new AdapterListSimplesSelector(this, listaArray.toArray(new String[listaArray.size()])));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerPopUp.dismiss();
                viewPager.setCurrentItem(position);
            }
        });


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
        viewPager.setSwipeEnabled(!isShared);
        return isShared;
    }

    @Override
    public void setIsShared(boolean isShared) {

        Log.i("teste_size", "isShared " + isShared);
        this.isShared = isShared;
        viewPager.setSwipeEnabled(!isShared);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isShared) {
            menu.findItem(R.id.limpar).setVisible(true);
        } else {
            menu.findItem(R.id.limpar).setVisible(false);
        }

        if (isShared) {
            getSupportActionBar().setSubtitle("");
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setSubtitle(livro);
            getSupportActionBar().setTitle("CatolicApp");
        }

        Log.i("teste_toolbar", "fois");

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void cancelShare() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onSelectItemToShare(int position) {

    }

    @Override
    public void onGetVersiculosToShare(String[] versiculos, List<Integer> itens) {
        itensToShare = versiculos;
        Log.i("teste_share", "item 0 " + itensToShare[0]);
        selecionados = itens;
    }

    @Override
    public boolean clearItens() {
        return false;
    }

    @Override
    public Bitmap onShareClicked() {

        selecionados = ordenarSlecionados(selecionados, itensToShare);

        CompartilhadorDeBitmap compartilhador = new CompartilhadorDeBitmap(this, testamento, selecionados, liv, viewPager.getCurrentItem() + 1);

        return compartilhador.getBitMap(itensToShare);
    }

    @Override
    public String onCopy() {

        ordenarSlecionados(selecionados, itensToShare);

        StringBuilder sb = new StringBuilder();

        for(String s: itensToShare){
            sb.append(s);
            sb.append("\n");
        }

        sb.append("\n");
        return sb.toString();
    }

    private List<Integer> ordenarSlecionados(List<Integer> selecionados, String[] itensToShare) {

        boolean trocou = true;
        Integer[] aux = selecionados.toArray(new Integer[selecionados.size()]);
        Integer a;
        String s;
        while (trocou) {
            trocou = false;
            for (int i = 0; i < selecionados.size() - 1; i++) {
                if (aux[i] > aux[i + 1]) {
                    a = aux[i];
                    aux[i] = aux[i + 1];
                    aux[i + 1] = a;

                    s = itensToShare[i];
                    itensToShare[i] = itensToShare[i+1];
                    itensToShare[i+1] = s;
                    Log.i("teste_ordenar","trocou");
                    trocou = true;
                }
            }
        }

        selecionados = new ArrayList<>();

        for (Integer i : aux) {
            Log.i("teste_ordenar",i.toString());
            selecionados.add(i);
        }
        return selecionados;
    }

    private String contruirPassagem() {
        String abr;
        StringBuilder aux = new StringBuilder();

        selecionados = ordenarSlecionados(selecionados, itensToShare);

        List<Integer> itens = selecionados;

        if (testamento == 0) {
            abr = getResources().getStringArray(R.array.abrev_livros_antigo_testam)[liv];
        } else {
            abr = getResources().getStringArray(R.array.abrev_livros_novo_testam)[liv];
        }

        aux.append("(" + abr + " " + (viewPager.getCurrentItem() + 1) + ", ");
        List<String> itensAux = new ArrayList<>();
        itensAux.add(Integer.toString(itens.get(0).intValue() + 1));
        for (int i = 1; i < itens.size(); i++) {
            if ((itens.get(i) - itens.get(i - 1)) == 1) {
                if (itensAux.get(itensAux.size() - 1).contains("-")) {
                    itensAux.remove(itensAux.size() - 1);
                    itensAux.add(itensAux.size(), "-" + (itens.get(i) + 1));
                } else {
                    itensAux.add(itensAux.size(), "-" + (itens.get(i) + 1));
                }
            } else {
                itensAux.add(itensAux.size(), ";" + (itens.get(i) + 1));
            }
        }
        itensAux.add(")");

        for (String a : itensAux) {
            aux.append(a);
        }
        return aux.toString();
    }

    @Override
    public void onAnswer(int answer) {
        if (answer == 1) {
            Favorito f = new Favorito();
            f.setAnotacao(null);
            f.setHasAnota(false);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Log.i("teste_data", format.format(c.getTime()));
            f.setData(format.format(c.getTime()));
            f.setPassagem(contruirPassagem());
            AdicionarComentarioDialogo dialogo = AdicionarComentarioDialogo.newInstance(f);
            dialogo.show(getFragmentManager().beginTransaction(), "adicionarCom");

        }

        limparSelecoes();
    }
}
