package com.alexandrelunkes.catolicapp.paginas;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListMenuPrincipal;
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 29/04/2016.
 */
public class PaginaDeResultadoFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ProgressBar progressBar;
    private LeitorDeBiblia leitorDeBiblia;

    private String query;
    private String[] palavrasDoQuery;
    private int cont = 0;
    private int totalBiblia = 35598;
    private int totalAntigo = 27641;
    private int totalNovo = 7957;
    private TextView textViewEncontrados;
    private TextView textViewLivroAtual;
    private ListView listViewResultados;
    private AdapterListMenuPrincipal adapter;
    private ArrayList<ItemListViewPrincipal> arrayList;
    private TextView textViewPorcentagem;
    private int tamAnt;

    private OnSelectPassagemListener myCallBack;
    private BuscadorAsync buscador;

    public static PaginaDeResultadoFragment newInstance(String query) {

        Bundle args = new Bundle();
        args.putString("query",query);
        PaginaDeResultadoFragment fragment = new PaginaDeResultadoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resultado_pesquisa,null);

        //testes de comparacao


        query = getArguments().getString("query");
        palavrasDoQuery = query.split(" ");


        textViewPorcentagem = (TextView) view.findViewById(R.id.textView_porcentagem);
        textViewEncontrados = (TextView) view.findViewById(R.id.textView_qty_resultado);
        textViewLivroAtual = (TextView) view.findViewById(R.id.textView_livro_atual);
        listViewResultados = (ListView) view.findViewById(R.id.listView_resultado_pesquisa);



        arrayList = new ArrayList<>();

        adapter = new AdapterListMenuPrincipal(getContext(),arrayList);
        listViewResultados.setAdapter(adapter);
        listViewResultados.setOnItemClickListener(this);


        TextView textViewQuery = (TextView) view.findViewById(R.id.textView_query_buscada);
        textViewQuery.setText(query);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_pesquisa);
        leitorDeBiblia = new LeitorDeBiblia(getContext());

        progressBar.setMax(35598);

        String[] livrosAntigo = leitorDeBiblia.getLivrosAntigoTestamento();
        String[] livrosNovo = leitorDeBiblia.getLivrosNovoTestamento();

        int antiLen = livrosAntigo.length;
        int novoLen = livrosNovo.length;
        tamAnt = antiLen;


        String[] todosOsLivros = new String[antiLen+novoLen];
        System.arraycopy(livrosAntigo,0,todosOsLivros,0,antiLen);
        System.arraycopy(livrosNovo,0,todosOsLivros,antiLen,novoLen);

        buscador = new BuscadorAsync();
        if(buscador.execute(todosOsLivros).getStatus()== AsyncTask.Status.RUNNING){
            buscador.cancel(true);
        }
        buscador = (BuscadorAsync) new BuscadorAsync().execute(todosOsLivros);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return view;
    }



    public int calcularQtyDeVers(){

        String[] livrosAntigo = leitorDeBiblia.getLivrosAntigoTestamento();
        String[] livrosNovo = leitorDeBiblia.getLivrosNovoTestamento();

        int total = 0;

//        for(String livro: livrosAntigo){
//            total+= leitorDeBiblia.quantosVersiculosNesteLivro(livro);
//        }
        for(String livro: livrosNovo){
            total+= leitorDeBiblia.quantosVersiculosNesteLivro(livro);
        }

        return total;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            myCallBack = (OnSelectPassagemListener) activity;
        }catch (InstantiationException e){

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(buscador.getStatus()== AsyncTask.Status.RUNNING){
            Toast.makeText(getContext(),"Espere finalizar a busca para selecionar",Toast.LENGTH_LONG).show();
        }else {
            myCallBack.onSelectedPassagem(view);
        }

    }

    private class BuscadorAsync extends AsyncTask<String,Integer,String>{

        int buscas = 0;
        int encontrado = 0;
        int capi = 0;
        int versiculo = 0;
        int sizeAnterior;


        StringBuilder versi = new StringBuilder();
        StringBuilder livroAtual = new StringBuilder();
        StringBuilder passagem = new StringBuilder();
        StringBuilder versiculoUP = new StringBuilder();
        StringBuilder queryUp = new StringBuilder();
        ItemListViewPrincipal item;
        ArrayList<ItemListViewPrincipal> arrayAux = new ArrayList<>();
        boolean livre = true;


        @Override
        protected String doInBackground(String... params) {
        CatolicApp.logCatolicApp("Iniciou a busca... ");
        queryUp.append(query.toUpperCase());
        for(int i = 0; i<params.length; i++) {

            livroAtual.replace(0,livroAtual.length(),params[i]);
            publishProgress(2);
            ArrayList<String[]> livro = leitorDeBiblia.getLivroDaBiblia(params[i]);
            capi = 0;
            for (String[]cap: livro){
                capi++;
                versiculo=0;

                for (String v: cap){
                   versiculo++;
                   versiculoUP.replace(0,versiculoUP.length(),v.toUpperCase());
                   if(versiculoUP.toString().contains(queryUp.toString())){
                      encontrado++;
                      versi.replace(0,versi.length(),v);
                      passagem.replace(0,passagem.length(),gerarPassagem(i,capi,versiculo));
                      item = new ItemListViewPrincipal(0, versi.toString(),passagem.toString());
                      arrayAux.add(item);
                      publishProgress(1);


                   }
                   buscas++;
                   publishProgress(0);
                }


            }

           //CatolicApp.logCatolicApp("buscando em "+ params[i]);
//            livroAtual.replace(0,livroAtual.length(),params[i]);
//            publishProgress(2);
//            ArrayList<String[]> livro = leitorDeBiblia.getLivroDaBiblia(params[i]);
//            capi = 0;
//            for (String[] cap : livro) {
//                capi++;
//                versiculo = 0;
//                for (String v : cap) {
//                    versiculo++;
//                    String[] palavras = v.split(" ");
//                    buscas++;
//                    for (String p : palavras) {
//                        if (p.equalsIgnoreCase(palavrasDoQuery[cont])){
//                            cont++;
//                        } else {
//                            cont = 0;
//                        }
//
//                        if (cont == palavrasDoQuery.length) {
//                            //CatolicApp.logCatolicApp("Encontrou um...");
//                            passagem.replace(0,passagem.length(),gerarPassagem(i,capi,versiculo));
//                            cont = 0;
//                            encontrado++;
//                            versi.replace(0,versi.length(),v);
//                            publishProgress(1);
//
//                        } else {
//                            publishProgress(0);
//                        }
//
//                    }
//
//                }
//            }
           if(this.isCancelled())i=params.length;
        }
         CatolicApp.logCatolicApp("Fim do processo");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            switch (values[0]){
                case 0: break;
                case 1:
                     sizeAnterior = arrayList.size();
                    for(int i = sizeAnterior; i<arrayAux.size(); i++){
                        arrayList.add(arrayAux.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    textViewEncontrados.setText(Integer.toString(encontrado));

                    break;
                case 2: textViewLivroAtual.setText("Buscando em "+livroAtual);
                    break;
            }
            progressBar.setProgress(buscas);

            float porcento = ((float)buscas/totalBiblia)*100;
            DecimalFormat format = new DecimalFormat("#.##");

            textViewPorcentagem.setText(format.format(porcento)+"%");

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getContext(),"Fim da busca",Toast.LENGTH_LONG).show();
            CatolicApp.logCatolicApp("Fim da busca.........."+ arrayList.size() + "encontrados");
        }
    }

     private String gerarPassagem(int liv, int cap, int ver){

         String livro;
         StringBuilder sb = new StringBuilder();

         if(liv < tamAnt){
            livro = leitorDeBiblia.getAbreviaturaAntigTestamen(liv);
         }else {
             livro = leitorDeBiblia.getAbreviaturaNovoTestamen(liv-tamAnt);
         }

         sb.append(livro+" "+cap+", "+ver);

         return sb.toString();
     }



     public interface OnSelectPassagemListener{
         public void onSelectedPassagem(View view);

     }

    @Override
    public void onDestroy() {
        super.onDestroy();



        if(buscador.getStatus() == AsyncTask.Status.RUNNING){
             buscador.cancel(true);
             CatolicApp.logCatolicApp("AsyncTask de busca cancelada");
        }
        CatolicApp.logCatolicApp("AsyncTask de busca em onDetroy");

    }
}
