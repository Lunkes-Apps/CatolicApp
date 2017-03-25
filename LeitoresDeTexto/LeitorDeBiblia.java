package com.alexandrelunkes.catolicapp.LeitoresDeTexto;


import android.content.Context;
import android.util.Log;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.R;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Alexandre Lunkes on 26/05/2015.
 */
public class LeitorDeBiblia extends LeitorDeTexto{



    private String[] livrosAntigoTestamento;
    private String[] abreviaturasAntigTestamen;
    private String[] livrosNovoTestamento;
    private String[] abreviaturasNovoTestamen;

    private HashMap<String,String> mapAntigo;
    private HashMap<String,String> mapNovo;

    Context context;

    public LeitorDeBiblia(Context context){
        super(null);
        this.context = context;
        mapAntigo = new HashMap<>();
        mapNovo = new HashMap<>();
        Log.v(CatolicApp.LOG_APP,String.valueOf(context));
        livrosAntigoTestamento = context.getResources().getStringArray(R.array.lista_antigo_testamento_livros_array);
        livrosNovoTestamento = context.getResources().getStringArray(R.array.lista_novo_testamento_livros_array);
        abreviaturasAntigTestamen = context.getResources().getStringArray(R.array.abrev_livros_antigo_testam);
        abreviaturasNovoTestamen = context.getResources().getStringArray(R.array.abrev_livros_novo_testam);
        gerarMaps();

    }

    private void gerarMaps() {
        int cont=0;
        for(String s: abreviaturasAntigTestamen){
            mapAntigo.put(s,livrosAntigoTestamento[cont]);
            cont++;
        }
        cont = 0;
        for(String s: abreviaturasNovoTestamen){
            mapAntigo.put(s,livrosNovoTestamento[cont]);
            cont++;
        }
    }


    public String removeAccent(String s){
        // transforma a string na forma canonica em decomposi�ao depois aplica replacceAll para retirar os acentos
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");
    }

    public String simplificarTitulo(String s){

        String aux = s.toLowerCase();
        aux = aux.replaceAll(" ", "_");
        aux = removeAccent(aux);

        return aux;
    }


    public StringBuilder getLivro(String livro){
        setLivroInputStream(livro);
        StringBuilder sb = returnAllText();
        return sb;
    }


    public int quantosVersiculosNesteLivro(String livro){

        ArrayList<String[]> book = getLivroDaBiblia(livro);

        int total = 0;

        for(String[] cap: book){
            for(String vers: cap){
                total++;
            }
        }
        return total;
    }


    public String getNomeLivroByAbreviatura(String abreviatura){

        if(mapAntigo.containsKey(abreviatura)){
            CatolicApp.logCatolicApp("getNomeLivroByAbreviatura "+ mapAntigo.get(abreviatura));
            return mapAntigo.get(abreviatura);
        }
        if(mapNovo.containsKey(abreviatura))
            return mapNovo.get(abreviatura);
        return null;
    }

    public ArrayList<String[]> getLivroDaBiblia(String livro){
        resetReader();
        CatolicApp.logCatolicApp(livro+ " getLivroDaBiblia");
        setLivroInputStream(livro);

        String comparador = context.getString(R.string.capitulo_teste);
        String compSalmo = "Salmo";
        ArrayList<String[]> livroCompleto = null;
        ArrayList<String> capitulo = new ArrayList<String>();
        String linha = "";
        lerLinha(); // Para pular o titulo
        StringBuilder sbVersiculos = new StringBuilder();
        int capIndex = 0;
        String aux;

        while(linha != null){
            if(!linha.matches("[0-9]+")) {
                if (!linha.contains(comparador) && !linha.matches("")&&!linha.matches("^Salmo\\s[0-9]+")) {

                    //Log.v(SouCatolico.LOG, "Estou adicionando -> "+ linha);
                    if (Character.isDigit(linha.charAt(0))) {
                        capitulo.add(linha);
                    } else {
                        if (!linha.matches("") && capitulo.size() != 0) {
                            aux = capitulo.get(capitulo.size() - 1);
                            capitulo.set(capitulo.size() - 1, aux + " " + linha);
                        }

                    }


                } else {
                    if (livroCompleto == null) {
                        livroCompleto = new ArrayList<String[]>();
                    }
                    if (capitulo.size() > 0)
                        livroCompleto.add(capitulo.toArray(new String[capitulo.size()]));
                    capitulo = new ArrayList<String>();
                }
            }
            linha = lerLinha();
        }


//        while(linha != null){
//            //  Log.v(SouCatolico.LOG,"Entrou no while");
//            if(!linha.matches("[0-9]+")&&!linha.contains(comparador)
//                    &&!linha.matches("")&&!linha.contains(compSalmo)){
//
//                if(Character.isDigit(linha.charAt(0))){
//
//                    if(sbVersiculos == null){
//                        sbVersiculos = new StringBuilder();
//                        sbVersiculos.append(linha + " ");
//                    }
//                    else{
//                        if(sbVersiculos.length()!=0)linhasLista.add(sbVersiculos.toString());
//                        sbVersiculos.setLength(0);
//                        sbVersiculos.append(linha + " ");
//                    }
//                }else{
//                    //Log.v(SouCatolico.LOG,"linha= "+linha);
//                    if(sbVersiculos!= null)sbVersiculos.append(linha + " ");// coloquei condicao para fazer duas buscas
//                }
//            }
//            if(linha.contains(comparador)||linha.contains(compSalmo)){
//               if(sbVersiculos!=null){
//                   linhasLista.add(sbVersiculos.toString());
//                   sbVersiculos.setLength(0);
//               }
//
//                //Log.v(SouCatolico.LOG,"Encontrou Capitulo");
//            if(livroCompleto == null) livroCompleto = new ArrayList<String[]>();
//                else{
//                    livroCompleto.add(linhasLista.toArray(new String[linhasLista.size()]));
//                    linhasLista = new ArrayList<String>();
//                }
//
//            }
//            linha = lerLinha();
//            //  Log.v(SouCatolico.LOG,linha);
//        }
//        livroCompleto.add(linhasLista.toArray(new String[linhasLista.size()]));//Carrega o ultimo capitulo
//
//
//        if(livroCompleto == null)Log.v(SouCatolico.LOG, "est� null");
//

        livroCompleto.add(capitulo.toArray(new String[capitulo.size()]));
        resetReader();
        return livroCompleto;
    }

    private void setLivroInputStream(String livro){
        // Using reflaction to get the id from R.raw

        livro = simplificarTitulo(livro);

      //  Log.v(SouCatolico.LOG, "setLivro = " + livro);
        try {
            Class rawClass = R.raw.class;
          //  Log.v(SouCatolico.LOG,livro);
            Field field = rawClass.getField(livro);
            R.raw rawObject = new R.raw();
            Object id = field.get(rawObject);
            setInputStream(context.getResources().openRawResource((Integer)id));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getNomePeloAbreviado(String abrev){



        return  null;
    }

    public static String getPassagem(String abreviatura, String capitulo, String versiculo){

        StringBuilder resposta = new StringBuilder();
        resposta.append(abreviatura+" "+capitulo+", "+versiculo);

        return resposta.toString();
    }


    public String getLivroAntigoTestamento(int p) {
        return livrosAntigoTestamento[p];
    }

    public String getAbreviaturaAntigTestamen(int p) {
        return abreviaturasAntigTestamen[p];
    }

    public String getLivrosNovoTestamento(int p) {
        return livrosNovoTestamento[p];
    }

    public String getAbreviaturaNovoTestamen(int p) {
        return abreviaturasNovoTestamen[p];
    }

    public String[] getLivrosAntigoTestamento() {
        return livrosAntigoTestamento;
    }

    public String[] getLivrosNovoTestamento() {
        return livrosNovoTestamento;
    }


}
