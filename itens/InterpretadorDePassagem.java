package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.util.Log;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.ItemAdapterPagerLivro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 08/09/2016.
 */
public class InterpretadorDePassagem {


    private Context context;
    private HashMap map;

    public InterpretadorDePassagem(Context context) {
        this.context = context;
        map = generateMap();
    }


    public PassagemBiblica parseLeitura(String leitura) {

        String palavras[] = leitura.split(" ");

        PassagemBiblica passagemBiblica;

        if (palavras[0].contains("L1") || palavras[0].contains("L2")
                || palavras[0].contains("Ev")) {
            passagemBiblica = parsePassagem(leitura);
        } else {
            passagemBiblica = parseSalmo(leitura);
        }

        return passagemBiblica;
    }


    public PassagemBiblica parsePassagem(String passagem) {

        PassagemBiblica passagemBiblica = new PassagemBiblica();
        List<Capitulo> capitulos = new ArrayList<>();
        List<Integer> versiculos = new ArrayList<>();


        String palavras[] = passagem.split(" ");

        String passagemTitulo;

        int testamento = -1;
        int livro = -1;
        if (iscap(palavras[2])) {
            testamento = checarTestamento(palavras[1]);
            livro = checarLivroId(palavras[1], testamento);
        } else {
            testamento = checarTestamento(palavras[1] + " " + palavras[2]);
            livro = checarLivroId(palavras[1] + " " + palavras[2], testamento);
        }

        passagemTitulo = parseTitulo(testamento, livro, palavras);

        passagem = tirarLivro(passagem);

        palavras = passagem.split(" ");


        int cap = -1;
        List<ItemLinha> linhas = new ArrayList<>();

        Log.i("teste_agenda", passagemTitulo);

        for (String p : palavras) {
            Log.i("teste_agenda", p + " loop");

            if (iscap(p)) {
                Log.i("teste_agenda", p);
                cap = Integer.parseInt(p.replaceAll("\\D", ""));
            } else {
                if (p.contains("-")) {
                    if (hasDigit(p)) {
                        String aux[] = p.split("-");
                        int inicio = Integer.parseInt(aux[0].replaceAll("\\D", ""));
                        int fim = Integer.parseInt(aux[1].replaceAll("\\D", ""));
                        for (int i = inicio; i <= fim; i++) {
                            Log.i("teste_agenda", p+ " "+i);
                            linhas.add(new ItemLinha(testamento, livro, cap, i));
                        }
                    } else { // erro para I Jo 3, 22 – 4, 6;
                        // ultima linha seta "marcar ate o fim"
                        Log.i("teste_agenda", p);
                        linhas.get(linhas.size() - 1).setAteOFim(true);
                    }
                } else {
                    if (!p.replaceAll("\\D", "").equals("")) {
                        //inserir linha
                        Log.i("teste_agenda", p);
                        linhas.add(new ItemLinha(testamento, livro, cap,
                                Integer.parseInt(p.replaceAll("\\D", ""))));

                    }
                }
            }
        }
        passagemBiblica.setPassagem(passagemTitulo);
        passagemBiblica.setLinhas(linhas);
        passagemBiblica.setTestamento(testamento);
        passagemBiblica.setLivro(livro);
        return passagemBiblica;
    }

    private String tirarLivro(String passagem) {

        String palavras[] = passagem.split(" ");
        StringBuilder sb = new StringBuilder();

        int i = 0;
        if (iscap(palavras[2])) {
            i = 3;
            sb.append(palavras[2]);
        } else {
            sb.append(palavras[3]);
            i = 4;
        }


        for (; i < palavras.length; i++) {
            sb.append(" " + palavras[i]);
        }

        return sb.toString();
    }

    private String parseTitulo(int testamento, int livro, String[] palavras) {

        String abrs[];

        String liv;

        if (testamento == 0) {
            abrs = context.getResources().getStringArray(R.array.abrev_livros_antigo_testam);
        } else {
            abrs = context.getResources().getStringArray(R.array.abrev_livros_novo_testam);
        }

        if (map.containsKey(abrs[livro])) {
            liv = (String) map.get(abrs[livro]);
        } else {
            liv = abrs[livro];
        }

        StringBuilder aux = new StringBuilder();
        aux.append(liv);

        int i = 0;

        if (iscap(palavras[2])) {
            i = 2;
        } else {
            i = 3;
        }

        for (; i < palavras.length; i++) {
            aux.append(" " + palavras[i]);
        }

        return aux.toString();
    }

    private boolean hasDigit(String p) {

        char letras[] = p.toCharArray();

        for (char c : letras) {
            if (Character.isDigit(c)) {
                return true;
            }
        }

        return false;
    }

    private boolean iscap(String palavra) {
        return palavra.contains(",");
    }


    public PassagemBiblica parseSalmo(String salmo) {

        PassagemBiblica passagemBiblica = new PassagemBiblica();

        if (isSalmo(salmo)) {

            List<Capitulo> capitulos = new ArrayList<>();
            List<Integer> versiculos = new ArrayList<>();

            int testamento = 0;
            int livro = 22;

            String palavras[] = salmo.split(" ");
            int cap = -1;

            Capitulo c = new Capitulo();
            for (String s : palavras) {
                if (!s.equals("")) {
                    Log.i("teste_agenda", s);
                    cap = Integer.parseInt(s.replaceAll("\\D", ""));
                    break;
                }
            }

            capitulos.add(c);

            palavras = salmo.split(",");

            palavras = palavras[1].split(" ");

            List<ItemLinha> linhas = new ArrayList<>();

            for (String s : palavras) {
                Log.i("teste_agenda",s + " loop");
                if (!s.contains("(")) {
                    if (s.contains("-")) {
                        String aux[] = s.split("-");

                        int inicio = Integer.parseInt(aux[0].replaceAll("\\D", ""));
                        int fim = Integer.parseInt(aux[1].replaceAll("\\D", ""));

                        for (int i = inicio; i <= fim; i++) {
                            Log.i("teste_agenda",s + " "+i);
                            linhas.add(new ItemLinha(testamento, livro, cap, i));
                        }

                    } else {
                        if (!s.replaceAll("\\D", "").equals("")){
                            Log.i("teste_agenda",s);
                            linhas.add(new ItemLinha(testamento, livro, cap,
                                    Integer.parseInt(s.replaceAll("\\D", ""))));
                        }
                    }
                }

            }


            passagemBiblica.setPassagem(salmo);
            passagemBiblica.setLinhas(linhas);
            passagemBiblica.setTestamento(testamento);
            passagemBiblica.setLivro(livro);

            return passagemBiblica;
        } else {
            return parsePassagem(salmo);
        }
    }

    private boolean isSalmo(String salmo) {
        return Character.isDigit(salmo.replaceAll(" ", "").charAt(0));
    }

    private int checarLivroId(String livro, int testamento) {


        String[] livs;

        if (map.containsKey(livro)) {
            livro = (String) map.get(livro);
        }

        if (testamento == 0) {
            livs = context.getResources().getStringArray(R.array.abrev_livros_antigo_testam);
        } else {
            livs = context.getResources().getStringArray(R.array.abrev_livros_novo_testam);
        }

        for (int i = 0; i < livs.length; i++) {
            if (livro.equals(livs[i])) {
                return i;
            }
        }


        try {
            throw new ExceptionLivro(livro);
        } catch (ExceptionLivro e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int checarTestamento(String livro) {

        String livs[] = context.getResources().getStringArray(R.array.abrev_livros_antigo_testam);

        if (map.containsKey(livro)) {
            livro = (String) map.get(livro);
        }

        for (String i : livs) {
            if (livro.equals(i)) {
                return 0;
            }
        }

        return 1;
    }


    public class Capitulo {

        private int cap;
        private List<Integer> versiculos;

        public int getCap() {
            return cap;
        }

        public void setCap(int cap) {
            this.cap = cap;
        }

        public List<Integer> getVersiculos() {
            return versiculos;
        }

        public void setVersiculos(List<Integer> versiculos) {
            this.versiculos = versiculos;
        }
    }


    private HashMap generateMap() {

        HashMap m = new HashMap();

         /*

          Gen = Gên
          Ex = Êx
          Num = Núm
          1 Sam = I Sam // checar os salmos
          2 Sam = II Sam
          1 Reis = I Re
          2 Reis = II Reis
          Cr = I Crôn
          2 Cr = II Crôn
          Jz = Juí
          Job = Jó
          Sal = Sl
          Co = Ecle
          Jonas = Jon
          Naum = Na
          Act = At
          1 Cor = I Cor
          2 Cor = II Cor
          Gal = Gál
          Filip = Flp
          1 Tes = I Tes
          2 Tes = II Tes
          1 Tim = 1 Tim
          2 Tim = II Tim
          Hebr = Heb
          1 Pedro = I Pe
          2 Pedro = II Pe
          Judas = Jud
          Ap = Apoc


         */
        m.put("1 Cr","I Crôn");
        m.put("Tito", "Tt");
        m.put("Tit", "II Tim");
        m.put("2 Mac", "II Mac");
        m.put("Joel", "Jl");
        m.put("1 Re", "I Reis");
        m.put("1 Jo", "I Jo");
        m.put("2 Jo", "II Jo");
        m.put("3 Jo", "III Jo");
        m.put("Gen", "Gên");
        m.put("Ex", "Êx");
        m.put("Num", "Núm");
        m.put("1 Sam", "I Sam");
        m.put("2 Sam", "II Sam");
        m.put("1 Reis", "I Reis");
        m.put("2 Reis", "II Reis");
        m.put("Cr", "I Crôn");
        m.put("2 Cr", "II Crôn");
        m.put("Jz", "Juí");
        m.put("Job", "Jó");
        m.put("Sal", "Sl");
        m.put("Co", "Ecle");
        m.put("Jonas", "Jon");
        m.put("Naum", "Na");
        m.put("Act", "At");
        m.put("1 Cor", "I Cor");
        m.put("2 Cor", "II Cor");
        m.put("Gal", "Gál");
        m.put("Filip", "Flp");
        m.put("1 Tes", "I Tes");
        m.put("2 Tes", "II Tes");
        m.put("1 Tim", "I Tim");
        m.put("2 Tim", "II Tim");
        m.put("Hebr", "Heb");
        m.put("1 Pedro", "I Pe");
        m.put("2 Pedro", "II Pe");
        m.put("Judas", "Jud");
        m.put("Ap", "Apoc");

        return m;
    }


}
