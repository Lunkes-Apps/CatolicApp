package com.alexandrelunkes.catolicapp.LeitoresDeTexto;

import android.content.Context;
import android.util.Log;


import com.alexandrelunkes.catolicapp.CatolicApp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexandre Lunkes on 10/05/2016.
 */
public class LeitorDePassagens extends LeitorDeBiblia{

    final static int ENCOTROU_PRIMEIRA = 0;
    final static int ENCOTROU_SALMO = 1;
    final static int ENCOTROU_SEGUNDA = 2;
    final static int ENCOTROU_EVANGELHO = 3;
    final static int INICIOU = 4;
    final static int TERMINOU = 5;
    final static int ENCONTROU_CAP = 1;
    final static int ENCONTROU_INICIO_VERS = 2;
    final static int ENCONTROU_FIM_VERS = 3;
    final static int ENCONTROU_VERSICULO = 6;
    private HashMap map = generateMap();
    private String regexLivro = "\\d[^\\s],?-?;?[^a-z]?.?;?-?";
    private String regexApagaLivro ="\\d?\\s?[a-zA-Z]";
    private String regexCap = "\\d+-\\d+.";
    private String regexVer = "\\d+,";

    private  ArrayList<String> itens;

    public LeitorDePassagens(Context context) {
        super(context);
    }


    //testar como dia 12-05
    public String gerarTextoDaPassagem(Passagem passagem){


        StringBuilder sbTexto = new StringBuilder();
        int total = 0;
        int aux = 0;
        int inicio = 0;
        int fim = 0;
        int cont = 0;
        boolean ok = true;
        int capAtual = 0;
        int tamVer = 0;

        ArrayList<String[]> livroCompleto;
        itens = new ArrayList<>();

        if(passagem!=null){

            if(passagem.getLivro()!=null) {
                CatolicApp.logCatolicApp("gerarTextoDaPassagem "+ passagem.getLivro());
                livroCompleto = getLivroDaBiblia(
                        getNomeLivroByAbreviatura(passagem.getLivro())
                );
            }else {
                livroCompleto = getLivroDaBiblia("salmos");
            }

            for(int i = 0; i<passagem.getCap().size(); i++){
                capAtual = passagem.getCap().get(i)-1;
                for(int j = 0; j<passagem.getVer().get(i).size(); j++){
                    tamVer = passagem.getVer().get(i).size();
                    inicio = passagem.getVer().get(i).get(j);
                    CatolicApp.logCatolicApp("gerarTexto "+ passagem.getLivro()+" "+" capAtual "+capAtual+
                    " inicio "+inicio+ "livrocompleto "+livroCompleto.get(capAtual).length);
                    sbTexto.append(livroCompleto.get(capAtual)[inicio-1]+"\n\n");
                    itens.add(livroCompleto.get(capAtual)[inicio-1]);
                   if(j<tamVer-1){
                       String texto;
                       j++;
                       fim = passagem.getVer().get(i).get(j);
                       total = fim - inicio;
                       for (int w = 0; w < total; w++){
                           texto = livroCompleto.get(capAtual)[inicio+w];
                           CatolicApp.logCatolicApp("gerarTexto "+texto);
                           sbTexto.append(livroCompleto.get(capAtual)[inicio+w]+"\n\n");
                           itens.add(livroCompleto.get(capAtual)[inicio+w]);
                       }
                   }
               }
            }


        }else {
            return null;
        }

      return sbTexto.toString();
    }

    public Passagem gerarPassagemLivro (String passagem){
        CatolicApp.logCatolicApp("gerarPassagem "+passagem);

        Passagem p = new Passagem();

        passagem = passagem.split("ou")[0];

        //String livro = passagem.replaceAll(regexLivro,"");
        String livro = passagem.split("\\d+,")[0];

        CatolicApp.logCatolicApp("gerarPassagem "+livro);
        String passagemSemLivro = passagem.replaceAll(livro,"").replaceAll("[a-zA-Z]","");
        p.setPassagem(passagemSemLivro);
        CatolicApp.logCatolicApp("gerarPassagem "+passagemSemLivro);
        String[] splitVerByCap = passagemSemLivro.split(regexVer);
        String cap = passagemSemLivro;

        for(String v: splitVerByCap){
            if(!v.matches("\\s"))cap = cap.replaceAll(v,"");
        }
        String[] splitCap = cap.split(",");
        splitCap[0] = splitCap[0].replaceAll("\\s","");
        int cont = 1;

        p.setLivro(checarMap(livro.trim()));
        CatolicApp.logCatolicApp("trim "+livro.trim());
        for (String c: splitCap){
            CatolicApp.logCatolicApp("gerarPassagem c split "+c);
            Log.i("teste_liturgia",c);
            p.getCap().add(Integer.parseInt(c));
            p.getVer().add(new ArrayList<Integer>());
            String[] versiculos = splitVerByCap[cont].split("\\D");
            for(String v: versiculos){
                if(!v.matches(""))p.getVer().get(cont-1).add(Integer.parseInt(v));
            }
            cont++;
        }

        CatolicApp.logCatolicApp("gerarPassagem completo = "+ getPassagemFormato(p));

        return p;
    }

    public Passagem gerarPassagemSalmo(String passagem){

        Passagem p = new Passagem();
        p.setLivro(null);
        p.setPassagem(passagem);
        String[] parametros = passagem.split(",");
        CatolicApp.logCatolicApp("gerarPassagemSalmo "+parametros[0]);
        CatolicApp.logCatolicApp("gerarPassagemSalmo 2"+parametros[1]);
        parametros[1]=parametros[1].replaceAll("\\D","@");
        parametros[1]=parametros[1].replaceAll("@+"," ");
        CatolicApp.logCatolicApp("gerarPassagemSalmo 2"+parametros[1]);
        String cap = parametros[0].split("\\s")[0];
        p.getCap().add(Integer.parseInt(cap));

        String[] splitVer = parametros[1].split("\\s");
        p.getVer().add(new ArrayList<Integer>());
        for (String v: splitVer){
            CatolicApp.logCatolicApp("gerarPassagem completo salmo v = "+v );
          if(!v.equals(""))p.getVer().get(0).add(Integer.parseInt(v));
        }
        CatolicApp.logCatolicApp("gerarPassagem completo salmo = "+ getPassagemFormato(p));

        return p;
    }


    public String getPassagemFormato(Passagem passagem) {
        StringBuilder sbPassagem = new StringBuilder();
        if(passagem.getLivro()!=null){
            sbPassagem.append(passagem.getLivro()+" ");
            sbPassagem.append(passagem.getPassagem());
            CatolicApp.logCatolicApp("gerarPassagem getPassa"+passagem.getPassagem());
        }else {
            sbPassagem.append(passagem.getPassagem());
        }

        return sbPassagem.toString();
    }

    public String gerarTextoDoSalmo(String salmo){//testar 25 de maio

        String[] parametros = salmo.split("\\s");

        int verAnterior = -1;
        String salmoStr = parametros[0].replaceAll(",","");
        String[] salmos = getLivroDaBiblia("Salmos").get(Integer.parseInt(salmoStr)-1);
        CatolicApp.logCatolicApp("buscando salmo "+ parametros[0]);

        StringBuilder sbTexto = new StringBuilder();

        int estado = INICIOU;

        for(String s: parametros){

            if(estado == ENCONTROU_CAP &&
                    !(s = s.replaceAll("[a-zA-Z]","")).equals("")){
                CatolicApp.logCatolicApp("buscando salmo "+ s);
                String[] versParams = s.split("\\W");


                int verInicio = Integer.parseInt(versParams[0])-1;
                int verFinal;
                if(versParams.length!=1){
                    verFinal = Integer.parseInt(versParams[1])-1;
                }else {
                    verFinal = verInicio;
                }
                int verTotal = verFinal - verInicio;
                CatolicApp.logCatolicApp(verInicio+"="+verAnterior);
                if(verAnterior == verInicio){
                    for (int i = 1; i <= verTotal; i++){
                        sbTexto.append(salmos[verInicio+i]+"\n\n");
                    }
                    CatolicApp.logCatolicApp("ver salmo passou aqui anterior igual");
                }else{
                    for (int i = 0; i <= verTotal; i++){
                        sbTexto.append(salmos[verInicio+i]+"\n\n");
                    }
                }

                verAnterior = verFinal;

            }
            if(s.contains(",")){
                   estado = ENCONTROU_CAP;

               }


        }


        return  sbTexto.toString();
    }


    public class Passagem{

        ArrayList<Integer> cap;
        ArrayList<ArrayList<Integer>> ver;
        String livro;
        String passagem;

        Passagem(){
           cap = new ArrayList<Integer>();
           ver = new ArrayList<ArrayList<Integer>>();
        }

        public String getPassagem() {
            return passagem;
        }

        public void setPassagem(String passagem) {
            this.passagem = passagem;
        }

        public ArrayList<Integer> getCap() {
            return cap;
        }

        public void setCap(ArrayList<Integer> cap) {
            this.cap = cap;
        }

        public ArrayList<ArrayList<Integer>> getVer() {
            return ver;
        }

        public void setVer(ArrayList<ArrayList<Integer>> ver) {
            this.ver = ver;
        }

        public String getLivro() {
            return livro;
        }

        public void setLivro(String livro) {
            this.livro = livro;
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


        m.put("Gen","Gên");
        m.put("Ex","Êx");
        m.put("Num" , "Núm");
        m.put("1 Sam" , "I Sam");
        m.put("2 Sam" , "II Sam");
        m.put("1 Reis" , "I Reis");
        m.put("2 Reis", "II Reis");
        m.put("Cr","I Crôn");
        m.put("2 Cr" ,"II Crôn");
        m.put("Jz","Juí");
        m.put("Job","Jó");
        m.put("Sal","Sl");
        m.put("Co" , "Ecle");
        m.put("Jonas" , "Jon");
        m.put("Naum","Na");
        m.put("Act","At");
        m.put("1 Cor","I Cor");
        m.put("2 Cor","II Cor");
        m.put("Gal","Gál");
        m.put("Filip","Flp");
        m.put("1 Tes" ,"I Tes");
        m.put("2 Tes" ,"II Tes");
        m.put("1 Tim","1 Tim");
        m.put("2 Tim","II Tim");
        m.put("Hebr","Heb");
        m.put("1 Pedro","I Pe");
        m.put("2 Pedro","II Pe");
        m.put("Judas", "Jud");
        m.put("Ap","Apoc");

        return m;
    }

    public String gerarFormatoPassagem(String passagem){
        if (passagem.equals("")){
            return null;
        }

        String[] parametros = passagem.split("\\s");
        StringBuilder sbLivro = new StringBuilder();
        StringBuilder sbPassagem = new StringBuilder();

        int cont = 0;
        if(parametros[0].matches("[a-zA-Z]{1,}")) {
            CatolicApp.logCatolicApp("sim eh um livro");
            sbLivro.append(parametros[0]);
            sbLivro.deleteCharAt(sbLivro.length() - 1);
            String livro = sbLivro.toString();
            livro = checarMap(livro);
            sbPassagem.append("(" + livro + " ");
            cont = 1;
        }

        CatolicApp.logCatolicApp("Verificar formato passagem salmo "+passagem);
        while (cont < parametros.length){
            sbPassagem.append(parametros[cont++]+" ");
        }
        sbPassagem.deleteCharAt(sbPassagem.length()-1);
        if(sbPassagem.charAt(sbPassagem.length()-1) == ';'){
            sbPassagem.deleteCharAt(sbPassagem.length()-1);
        }

        sbPassagem.append(")");

        return sbPassagem.toString();
    }

    public String checarMap(String key){

        if(map.containsKey(key)){
            return (String) generateMap().get(key);
        }else {
            return key;
        }

    }

    public ArrayList<String> getItens() {
        return itens;
    }

    public void setItens(ArrayList<String> itens) {
        this.itens = itens;
    }
}
