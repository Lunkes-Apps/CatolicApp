package com.alexandrelunkes.catolicapp.Tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDePassagens;
import com.alexandrelunkes.catolicapp.PassagemTexto;
import com.alexandrelunkes.catolicapp.agenda.AgendaItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 11/05/2016.
 */
public class AtualizadorLiturgiaDiaria extends AsyncTask <String,Integer,Integer> {

    private ArrayList<AgendaItem> agendaCompleta = new ArrayList<>();
    private Context context;
    private OnExecutedAtualizadorLiturgia myCallBack;
    Dialog dialog;
    private String data;
    private LeitorDePassagens leitorDePassagens;

    private String textoPrimeira;
    private String textoSalmo;
    private String textoSegunda;
    private String textoEvangelho;
    private String passagemPrimeira;
    private String passagemSalmo;
    private String passagemSegunda;
    private String passagemEvangelho;



    final static int ENCOTROU_PRIMEIRA = 0;
    final static int ENCOTROU_SALMO = 1;
    final static int ENCOTROU_SEGUNDA = 2;
    final static int ENCOTROU_EVANGELHO = 3;
    final static int INICIOU = 4;
    final static int TERMINOU = 5;

    private StringBuilder primeira = new StringBuilder();
    private StringBuilder segunda = new StringBuilder();
    private StringBuilder salmo = new StringBuilder();
    private StringBuilder evangelho = new StringBuilder();

    private List<String> itensPrimeira;
    private List<String> itensSalmo;
    private List<String> itensSegunda;
    private List<String> itensEvangelho;


    public AtualizadorLiturgiaDiaria(Context context, String data) {
          this.data = data;
          this.context = context;
          leitorDePassagens = new LeitorDePassagens(context);
        try {
           myCallBack = (OnExecutedAtualizadorLiturgia) context;
        }catch (ClassCastException e){
            CatolicApp.logCatolicApp(context.toString()+ " Precisa implemntar " +
                    "funcoes de OnExecutedAtualizadorLiturgia");
        }
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context,"Aguarde","Carregando ...");
    }

    @Override
    protected Integer doInBackground(String... params) {


        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            InputStream inputStream = context.getAssets().open("agenda.xml");
            parser.setInput(inputStream,null);
            parseXml(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        publishProgress(1);
        ArrayList<LeitorDePassagens.Passagem> passagens = atualizarLeituras(buscarLeituraDoDia(agendaCompleta,data));
        CatolicApp.logCatolicApp("doInBackground Atualizador "+ passagens.get(0).getLivro());
        textoPrimeira = leitorDePassagens.gerarTextoDaPassagem(passagens.get(0));
        itensPrimeira = leitorDePassagens.getItens();

        textoSegunda = leitorDePassagens.gerarTextoDaPassagem(passagens.get(2));
        itensSegunda = leitorDePassagens.getItens();

        CatolicApp.logCatolicApp("Salmo gerarTexto "+passagens.get(1).getCap().get(0));
        textoSalmo = leitorDePassagens.gerarTextoDaPassagem(passagens.get(1));
        itensSalmo = leitorDePassagens.getItens();

        textoEvangelho = leitorDePassagens.gerarTextoDaPassagem(passagens.get(3));
        itensEvangelho = leitorDePassagens.getItens();

        passagemPrimeira = leitorDePassagens.getPassagemFormato(passagens.get(0));
        passagemSalmo = leitorDePassagens.getPassagemFormato(passagens.get(1));
        CatolicApp.logCatolicApp("segunda atualizador "+ segunda.toString());
        if(passagens.get(2)!=null)passagemSegunda = leitorDePassagens.getPassagemFormato(passagens.get(2));
        passagemEvangelho = leitorDePassagens.getPassagemFormato(passagens.get(3));

        publishProgress(2);

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        myCallBack.agendaCompletaAtualizada(agendaCompleta);
        myCallBack.leiturasAtualizadas(
                new PassagemTexto(passagemPrimeira,textoPrimeira),
                new PassagemTexto(passagemSalmo,textoSalmo),
                new PassagemTexto(passagemSegunda,textoSegunda),
                new PassagemTexto(passagemEvangelho,textoEvangelho)
        );
        myCallBack.setItensToShare(itensPrimeira.toArray(new String[itensPrimeira.size()])
        ,itensSalmo.toArray(new String[itensPrimeira.size()])
        ,itensSegunda.toArray(new String[itensPrimeira.size()])
        ,itensEvangelho.toArray(new String[itensPrimeira.size()]));
        dialog.dismiss();
    }

    /*
      Inteface de comunicacao com a classe LiturgiaDiariaActivity
     */

    public interface OnExecutedAtualizadorLiturgia{
        public void agendaCompletaAtualizada(ArrayList<AgendaItem> agendaCompleta);
        public void leiturasAtualizadas(PassagemTexto... leituras);

        void setItensToShare(String[]... itens);
    }

    private void parseXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType= parser.getEventType();
        String name;

        AgendaItem agenda = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            eventType= parser.getEventType();
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    // CatolicApp.logCatolicApp("iniciou XmlPullParser");
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    // CatolicApp.logCatolicApp("iniciou XmlPullParser start tag - "+name);
                    if(name.equals("AgendaLitur2016")){
                        agenda = new AgendaItem();
                    }else if (agenda!=null){
                        if(name.equals("assunto")){
                            //       CatolicApp.logCatolicApp("iniciou XmlPullParser encontou - "+name);
                            agenda.setAssunto(parser.nextText());
                        }else if (name.equals("descricao")){
                            agenda.setDescricao(parser.nextText());
                        }else if(name.equals("data_inicio")){
                            agenda.setData_inicio(parser.nextText());
                        }
                    }break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("AgendaLitur2016") && agenda !=null){
                        agendaCompleta.add(agenda);
                    }
            }

            parser.next();
        }

    }

    private String buscarLeituraDoDia(ArrayList<AgendaItem> agendaCompleta, String data) {

        for(AgendaItem item: agendaCompleta){

            if(item.getData_inicio().equals(data)){
                return item.getDescricao();
            }

        }
        return "não encontrado";
    }

    private ArrayList<LeitorDePassagens.Passagem> atualizarLeituras(String leiturasDoDia){

        String[] linhas = leiturasDoDia.split("\n");

        primeira = new StringBuilder();
        segunda = new StringBuilder();
        salmo = new StringBuilder();
        evangelho = new StringBuilder();

        StringBuilder leituras = new StringBuilder();
        int estado = -1;

        for(String linha: linhas){
            if(linha.contains("L1 ") && estado!=TERMINOU){
               estado = INICIOU;
            }
            if (linha.contains("Ev ")&& estado!=TERMINOU){
                estado = ENCOTROU_EVANGELHO;
            }

            switch (estado){
                case INICIOU: leituras.append(linha+ " ");break;
                case ENCOTROU_EVANGELHO: leituras.append(linha+" ");
                    estado = TERMINOU;
                    break;
                default:break;
            }
        }

        CatolicApp.logCatolicApp("atualizarLeitura "+leituras.toString());

        estado = INICIOU;

        for(String s: leituras.toString().split("\\s")){

            if(s.contains("L1")){
                estado = ENCOTROU_PRIMEIRA;
            }
            if(s.contains("Sal")){
                estado = ENCOTROU_SALMO;
            }
            if(s.contains("L2")){
                estado = ENCOTROU_SEGUNDA;
            }
            if(s.contains("Ev")){
                estado = ENCOTROU_EVANGELHO;
            }

            switch (estado){
                case INICIOU: break;
                case ENCOTROU_PRIMEIRA:
                    primeira.append(s+" ");
                    break;
                case ENCOTROU_SALMO:
                    salmo.append(s+" ");
                    break;
                case  ENCOTROU_SEGUNDA:
                    segunda.append(s+ " ");
                    break;
                case ENCOTROU_EVANGELHO:
                    evangelho.append(s+" ");
                    break;
                default:break;
            }
        }

        String l1 = primeira.toString().replaceAll("L1 ","");
        String sal = salmo.toString().replaceAll("Sal ","");
        String l2 = segunda.toString().replaceAll("L2 ","");
        String ev = evangelho.toString().replaceAll("Ev ","");

        CatolicApp.logCatolicApp("leituras primeira "+l1);
        CatolicApp.logCatolicApp("leituras salmo "+sal);
        CatolicApp.logCatolicApp("leituras segunda "+l2);
        CatolicApp.logCatolicApp("leituras evangelho "+ev);


        ArrayList<LeitorDePassagens.Passagem> passagens = new ArrayList<>();
   //   String regexLivro = "\\d[^\\s],?-?;?[^a-z]?.?;?-?";
        passagens.add(leitorDePassagens.gerarPassagemLivro(l1));
        if(String.valueOf(sal.charAt(0)).matches("\\d")){
            passagens.add(leitorDePassagens.gerarPassagemSalmo(sal));
            CatolicApp.logCatolicApp("Salmo passou");
        }
        else {
            passagens.add(leitorDePassagens.gerarPassagemLivro(sal));
            CatolicApp.logCatolicApp("Salmo é um livro");
        }
        if(!segunda.toString().equals(""))passagens.add(leitorDePassagens.gerarPassagemLivro(l2));
        else passagens.add(null);
        passagens.add(leitorDePassagens.gerarPassagemLivro(ev));

        return passagens;
    }




}
