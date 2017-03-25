package com.alexandrelunkes.catolicapp.paginas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListMenuPrincipal;
import com.alexandrelunkes.catolicapp.bibliaeagenda.ItemBiblia;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;

import java.util.ArrayList;
import java.util.List;


public class ResultadoDaPesquisaActivity extends AppCompatActivity implements PaginaDePesquisaFragment.OnOkClicked,
        PaginaDeResultadoFragment.OnSelectPassagemListener{

    private int estado = 0;
    public static final int INICIO = 0;
    public static final int PAGINA_RESULTADOS = 1;
    public List<ItemBiblia> versiculosAchados;
    private ProgressDialog progress;

    private LeitorDeBiblia leitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_da_pesquisa);

        setAppBar();
        estado = INICIO;
        leitor = new LeitorDeBiblia(this);
        if(savedInstanceState==null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentPrincipalPesquisa,new PaginaDePesquisaFragment());
            ft.commit();
        }

    }

    private void setAppBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt("estado",estado);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null){

        }

    }



    @Override
    public void okClicked(final String query) {

//        Log.i("teste_keyboard","passou aqui");
//        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
//        estado = PAGINA_RESULTADOS;
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fragmentPrincipalPesquisa,PaginaDeResultadoFragment.newInstance(query));
//        ft.commit();


    }

    @Override
    public void onSelectedPassagem(View view) {
        AdapterListMenuPrincipal.ItemSuport item = (AdapterListMenuPrincipal.ItemSuport) view.getTag();

        String passagem = item.getSubtitulo().getText().toString();
        ItemSuportPassagem itemPass = getItemPassagem(passagem);

        Intent intent = new Intent(this,LivroCompletoActivity.class);
        intent.putExtra(LivroCompletoActivity.TESTAMENTO_ID,itemPass.getTestamento());
        intent.putExtra(LivroCompletoActivity.LIVRO_ID,itemPass.getLivro());
        intent.putExtra(LivroCompletoActivity.CAPITULO_ID,itemPass.getCapitulo());
        intent.putExtra(LivroCompletoActivity.VERSICULO_ID,itemPass.getVersiculo());

        startActivity(intent);

    }

    private ItemSuportPassagem getItemPassagem(String passagem) {

        int liv;
        int cap;
        int ver;
        int testamento;

        String[] palavras = passagem.split(" ");
        String capitulo;
        String livro;
        String versiculo = palavras[palavras.length-1];
        if(palavras.length > 3){
            capitulo = palavras[2];
            livro = palavras[0]+ " " + palavras[1];
        }else {
            capitulo = palavras[1];
            livro = palavras[0];
        }
        int tam = leitor.getLivrosAntigoTestamento().length;

        testamento = retornaTestamento(livro);

        capitulo = capitulo.replace(",","");

        liv = getLivroIndex(livro, testamento);

        cap = Integer.parseInt(capitulo);
        ver = Integer.parseInt(versiculo);

        return new ItemSuportPassagem(testamento,liv,cap-1,ver-1);
    }

    private int retornaTestamento(String livro) {

        for(String s: getResources().getStringArray(R.array.abrev_livros_novo_testam)){

            if(s.equals(livro)){
                return 1;
            }

        }
        return 0;
    }

    private int getLivroIndex(String livro, int testamento) {

        int i = 0;

        if(testamento == 1){
            for(String abr: getResources().getStringArray(R.array.abrev_livros_novo_testam)){
                if(abr.equals(livro)){
                    return i;
                }
                i++;
            }
        }else {
            for(String abr: getResources().getStringArray(R.array.abrev_livros_antigo_testam)){
                if(abr.equals(livro)){
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public class ItemSuportPassagem{
        private int livro;
        private int capitulo;
        private int versiculo;
        private int testamento;



        public ItemSuportPassagem(int testamento, int livro, int capitulo, int versiculo) {
            this.livro = livro;
            this.capitulo = capitulo;
            this.versiculo = versiculo;
            this.testamento = testamento;
        }

        public int getTestamento() {
            return testamento;
        }

        public void setTestamento(int testamento) {
            this.testamento = testamento;
        }


        public int getLivro() {
            return livro;
        }

        public void setLivro(int livro) {
            this.livro = livro;
        }

        public int getCapitulo() {
            return capitulo;
        }

        public void setCapitulo(int capitulo) {
            this.capitulo = capitulo;
        }

        public int getVersiculo() {
            return versiculo;
        }

        public void setVersiculo(int versiculo) {
            this.versiculo = versiculo;
        }
    }


}
