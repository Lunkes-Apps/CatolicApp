package com.alexandrelunkes.catolicapp.paginas;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDePassagens;
import com.alexandrelunkes.catolicapp.PassagemTexto;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.Tasks.AtualizadorLiturgiaDiaria;
import com.alexandrelunkes.catolicapp.adapters.CollectionAdapterPagerLeituras;
import com.alexandrelunkes.catolicapp.adapters.ItemAdapterPagerLeituras;
import com.alexandrelunkes.catolicapp.agenda.AgendaItem;
import com.alexandrelunkes.catolicapp.agenda.DialogCalendario;
import com.alexandrelunkes.catolicapp.bibliaeagenda.ItemAgenda;
import com.alexandrelunkes.catolicapp.bibliaeagenda.Livro;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioAgendaDAO;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;
import com.alexandrelunkes.catolicapp.dialogs.AlertasDialogPergaminho;
import com.alexandrelunkes.catolicapp.dialogs.ShareOptionsDialog;
import com.alexandrelunkes.catolicapp.itens.CheckNetWorkManager;
import com.alexandrelunkes.catolicapp.itens.ClipBoardCatolicApp;
import com.alexandrelunkes.catolicapp.itens.InterpretadorDePassagem;
import com.alexandrelunkes.catolicapp.itens.ItemLinha;
import com.alexandrelunkes.catolicapp.itens.PassagemBiblica;
import com.google.android.gms.internal.zzit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class LiturgiaDiariaActivity extends AppCompatActivity
        implements DialogCalendario.DataLiturgiaClickedListener,
        AtualizadorLiturgiaDiaria.OnExecutedAtualizadorLiturgia,
        ShareOptionsDialog.OnShareClickedListener {

    public static final String PREFERENCE = "agendaSettings";
    public static final int INICIO_TEXTO = 100;
    public static final int INICIO_PARAGRAFO = 50;
    public static final int TAM_LETRA = 15;
    public static final int TAM_LINHA = TAM_LETRA * 69;
    public static final int LARGURA_PAG = INICIO_PARAGRAFO + TAM_LINHA;
    public static final int ALTURA_LINHA = 35;

    private CheckNetWorkManager netWorkManager;
    private TextView textViewDataAtual;

    private CollectionAdapterPagerLeituras collection;


    private ArrayList<AgendaItem> agendaCompleta = new ArrayList<>();
    private ViewPager viewPager;

    private ProgressDialog progressDialog;

    private LeitorDeBiblia leitorDeBiblia;
    private PassagemTexto[] passagemTextos;
    private String[] itensToShareP;
    private String[] itensToShareSal;
    private String[] itensToShareS;
    private String[] itensToShareE;

    private String[] passagens;

    private String tituloP;
    private String tituloSal;
    private String tituloS;
    private String tituloE;

    private String formato1 = "dd 'de' MMMM 'de' yyyy";
    private String formato2 = "dd'-'MM'-'yyyy";
    private String dataDeHoje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liturgia_diaria_novo);

        SharedPreferences settings = getSharedPreferences(PREFERENCE, MODE_PRIVATE);

        if (settings.getBoolean("agenda_pronta", false)) {

        } else {
            prepararAgenda();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("agenda_pronta", true);
            Log.i("teste_carregamento", "agenda pronta");
            editor.commit();
        }


        setAppBar();
        viewPager = (ViewPager) findViewById(R.id.viewPager_liturgia);
        leitorDeBiblia = new LeitorDeBiblia(this);

        textViewDataAtual = (TextView) findViewById(R.id.textView_data_hoje_novo);

        DateFormat dateFormat = new SimpleDateFormat(formato1);
        Calendar hoje = Calendar.getInstance();
        dataDeHoje = dateFormat.format(hoje.getTime());

        textViewDataAtual.setText(dataDeHoje);
        String dataFormato2 = new SimpleDateFormat("dd'-'MM'-'yyyy").format(Calendar.getInstance().getTime());
        if (savedInstanceState == null) {
            carregarLeituras(dataFormato2);
            // new AtualizadorLiturgiaDiaria(this, dataFormato2).execute();


        } else {
            FragmentManager fm = getSupportFragmentManager();
            ArrayList<ItemAdapterPagerLeituras> itens = new ArrayList<>();
            ArrayList<PassagemTexto> auxPassagensTextos = new ArrayList<>();
            passagens = savedInstanceState.getStringArray("passagens");
            String[] textos = savedInstanceState.getStringArray("textos");
            String[] auxTitulos = {"Primeira Leitura\n", "Salmo\n", "Segunda Leitura\n", "Evangelho\n"};

            for (int i = 0; i < passagens.length; i++) {
                auxPassagensTextos.add(new PassagemTexto(passagens[i], textos[i]));
                if (passagens[i] != null)
                    itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[i] + passagens[i], textos[i], dataDeHoje));
            }

            passagemTextos = auxPassagensTextos.toArray(new PassagemTexto[auxPassagensTextos.size()]);

            viewPager.setAdapter(new CollectionAdapterPagerLeituras(fm, itens));
        }

    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            PassagemTexto[] itens = (PassagemTexto[]) msg.obj;
            if (itens != null) {
                atualizarDisplay(itens);
            }
        }
    };

    private void prepararAgenda() {
        RepositorioAgendaDAO agendaDAO = RepositorioAgendaDAO.getInstance(this);
        ProgressDialog progress = ProgressDialog.show(this, "Aguarde", "Carregando dados");
        agendaDAO.deletBanco();
        agendaDAO.carregarAgenda();
        progress.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.liturgia_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calendario:
                showCalendario();
                break;
            case R.id.share:

                netWorkManager = new CheckNetWorkManager(this);

                if (netWorkManager.isConected()) {
                    ShareOptionsDialog dialog = new ShareOptionsDialog();
                    dialog.setOnShareClickedListener(this);
                    dialog.show(getFragmentManager().beginTransaction(), "shareOptions");
                }else {
                    AlertasDialogPergaminho alerta = AlertasDialogPergaminho.newInstance("Alerta de Internet", "Sua conexão" +
                            " com a Internet falhou. \n Verifique seu dispositivo Wi-Fi ou Rede de Dados");
                     alerta.show(getFragmentManager().beginTransaction(),"alertaNet");
                }
                break;

            case R.id.menu_cpy:

                ClipBoardCatolicApp clip = new ClipBoardCatolicApp(this);

                int pag = 0;
                pag = viewPager.getCurrentItem();
                if (passagemTextos[pag].getTitulo() == null) {
                    pag++;
                    String textoToCopy = passagemTextos[pag].getTexto();
                    StringBuilder sb = new StringBuilder(passagemTextos[pag].getTitulo());
                    sb.append("\n");
                    sb.append("\n");
                    sb.append(textoToCopy);
                    clip.copiarTexto(sb.toString());

                } else {
                    pag = viewPager.getCurrentItem();
                    String textoToCopy = passagemTextos[pag].getTexto();
                    StringBuilder sb = new StringBuilder(passagemTextos[pag].getTitulo());
                    sb.append("\n");
                    sb.append("\n");
                    sb.append(textoToCopy);
                    clip.copiarTexto(sb.toString());
                }


                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showCalendario() {
        DialogCalendario calendario = new DialogCalendario();
        calendario.show(getFragmentManager(), null);
    }


    private String buscarLeituraDoDia(ArrayList<AgendaItem> agendaCompleta, String data) {

        for (AgendaItem item : agendaCompleta) {

            if (item.getData_inicio().equals(data)) {
                return item.getDescricao();
            }

        }
        return "não encontrado";
    }


    @Override
    public void onDataLiturgiaClicked(DatePicker date) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());

        //new AtualizadorLiturgiaDiaria(this, new SimpleDateFormat(formato2).format(calendar.getTime())).execute();

        carregarLeituras(new SimpleDateFormat(formato2).format(calendar.getTime()));
        dataDeHoje = new SimpleDateFormat(formato1).format(calendar.getTime());
        textViewDataAtual.setText(new SimpleDateFormat(formato1).format(calendar.getTime()));

    }


    @Override
    public void agendaCompletaAtualizada(ArrayList<AgendaItem> agendaCompleta) {
        this.agendaCompleta = agendaCompleta;

        String dataFormato2 = new SimpleDateFormat("dd'-'MM'-'yyyy").format(Calendar.getInstance().getTime());
        String leiturasDoDia = buscarLeituraDoDia(agendaCompleta, dataFormato2);

        // setLeiturasDoDia(leiturasDoDia);

    }

    @Override
    public void leiturasAtualizadas(PassagemTexto... leituras) {

        passagemTextos = leituras;

        ArrayList<ItemAdapterPagerLeituras> itens = new ArrayList<>();
        FragmentManager fm = this.getSupportFragmentManager();
        String[] auxTitulos = {"Primeira Leitura\n", "Salmo\n", "Segunda Leitura\n", "Evangelho\n"};
        int cont = 0;
        for (PassagemTexto p : leituras) {
            if (p.getTitulo() != null)
                itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[cont] + p.getTitulo(), p.getTexto(),
                        dataDeHoje));
            cont++;
        }
        collection = new CollectionAdapterPagerLeituras(fm, itens);
        viewPager.setAdapter(collection);
    }

    private void atualizarDisplay(PassagemTexto[] passagens) {

        passagemTextos = passagens;

        ArrayList<ItemAdapterPagerLeituras> itens = new ArrayList<>();
        FragmentManager fm = this.getSupportFragmentManager();
        String[] auxTitulos = {"Primeira Leitura\n", "Salmo\n", "Segunda Leitura\n", "Evangelho\n"};
        int cont = 0;

        if(passagens.length == 3){
            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[0] + passagens[0].getTitulo(), passagens[0].getTexto(),
                    dataDeHoje));
            tituloP = auxTitulos[0] + passagens[0].getTitulo();
            tituloSal = auxTitulos[1] + passagens[1].getTitulo();
            tituloE = auxTitulos[3] + passagens[2].getTitulo();
            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[1] + passagens[1].getTitulo(), passagens[1].getTexto(),
                     dataDeHoje));
            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[3] + passagens[2].getTitulo(), passagens[2].getTexto(),
                    dataDeHoje));
        }else {
            tituloP = auxTitulos[0] + passagens[0].getTitulo();
            tituloSal = auxTitulos[1] + passagens[1].getTitulo();
            tituloS = auxTitulos[2] + passagens[2].getTitulo();
            tituloE = auxTitulos[3] + passagens[3].getTitulo();
            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[0] + passagens[0].getTitulo(), passagens[0].getTexto(),
                    dataDeHoje));
            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[1] + passagens[1].getTitulo(), passagens[1].getTexto(),
                    dataDeHoje));

            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[2] + passagens[2].getTitulo(), passagens[2].getTexto(),
                    dataDeHoje));

            itens.add(ItemAdapterPagerLeituras.newInstance(auxTitulos[3] + passagens[3].getTitulo(), passagens[3].getTexto(),
                    dataDeHoje));

        }
        collection = new CollectionAdapterPagerLeituras(fm, itens);
        viewPager.setAdapter(collection);
    }


    private void carregarLeituras(final String data) {
        final ProgressDialog progressDialog1 = ProgressDialog.show(this, "Aguarde", "Carregando dados");

        final RepositorioAgendaDAO agendaDAO = RepositorioAgendaDAO.getInstance(this);


        new Thread() {
            @Override
            public void run() {
                ItemAgenda item = agendaDAO.leiturasPorData(data);
                PassagemTexto itens[] = setarPaginas(item);

                Message msg = new Message();
                msg.obj = itens;
                myHandler.sendMessage(msg);

                //  Log.i("teste_agenda", "inicio do teste");
                //  testarAgenda();
                //  Log.i("teste_agenda", "fim do teste");
                progressDialog1.dismiss();
            }
        }.start();
    }

    private void testarAgenda() {

        RepositorioAgendaDAO dao = RepositorioAgendaDAO.getInstance(this);

        ItemAgenda[] itens = dao.listarTodaAgenda();

        for (ItemAgenda i : itens) {
            setarPaginas(i);
        }

    }

    @Override
    public void setItensToShare(String[]... itens) {
        itensToShareP = itens[0];
        itensToShareSal = itens[1];
        itensToShareS = itens[2];
        itensToShareE = itens[3];
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String[] passagens;
        String[] textos;
        ArrayList<String> arrayPassagens = new ArrayList<>();
        ArrayList<String> arrayTextos = new ArrayList<>();

        for (PassagemTexto p : passagemTextos) {
            arrayPassagens.add(p.getTitulo());
            arrayTextos.add(p.getTexto());
        }

        passagens = arrayPassagens.toArray(new String[arrayPassagens.size()]);
        textos = arrayTextos.toArray(new String[arrayTextos.size()]);
        if (passagens == null) CatolicApp.logCatolicApp("passagens iniciou null");
        else CatolicApp.logCatolicApp("passagens ok");
        outState.putStringArray("passagens", passagens);
        outState.putStringArray("textos", textos);

    }


    @Override
    public Bitmap onShareClicked() {
        float maiorSize = 600;
        int x = (int) (maiorSize + 150);


        //determinar a leitura

        String[] itensToShare = determinateItens();

        if (itensToShare == null) {
            Log.i("teste_", "itens to share null");
        } else {
            Log.i("teste_", "itens to share " + itensToShare[1]);
        }


        int linha = 0;

        // String[] partesInicio = titulo.getText().toString().split("\n");

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(30);
        paintText.setTypeface(Typeface.createFromAsset(getAssets(), "vijaya.ttf"));
        paintText.setStyle(Paint.Style.FILL);

        List<String> linhas = new ArrayList<>();

        linhas.add("Liturgia de " + dataDeHoje);
        linhas.add(retornaProclamacao()[0]);
        linhas.add("");

        String[] proclamacao = {retornaProclamacao()[1]};
        for (String s : construirLinhas(proclamacao, maiorSize, paintText)) {
            linhas.add(s);
        }

        // linhas.add("");

        for (String s : construirLinhas(itensToShare, maiorSize, paintText)) {
            linhas.add(s);
        }


        int y = INICIO_TEXTO + (linhas.size() * ALTURA_LINHA) + (3 * ALTURA_LINHA);

        int id = R.drawable.pergaminhovertical;

        Bitmap bitmapPergaminho = BitmapFactory.decodeResource(this.getResources(), id);
        byte[] chunk = bitmapPergaminho.getNinePatchChunk();
        NinePatchDrawable npdPergaminho = new NinePatchDrawable(bitmapPergaminho, chunk, new Rect(), null);
        npdPergaminho.setBounds(0, 0, x, y);

        CatolicApp.logCatolicApp("X e Y" + x + " " + y);


        Bitmap outPut = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);


        Canvas canvasPergaminho = new Canvas(outPut);
        npdPergaminho.draw(canvasPergaminho);


        if (passagens == null) {
            Log.i("Teste_shared", "passagens null " + passagemTextos[0].getTitulo());
        }


//        canvasPergaminho.drawText(,
//                INICIO_PARAGRAFO,
//                INICIO_TEXTO + (linha * ALTURA_LINHA), paintText);
//        linha++;

        for (int i = 0; i < linhas.size(); i++) {
            canvasPergaminho.drawText(linhas.get(i), INICIO_PARAGRAFO,
                    INICIO_TEXTO + (i * ALTURA_LINHA), paintText);
        }

        paintText.setTypeface(Typeface.createFromAsset(getAssets(), "itcedscr.ttf"));
        paintText.setTextSize(45);

        canvasPergaminho.drawText(constuirAssinatura(maiorSize, paintText), INICIO_PARAGRAFO,
                INICIO_TEXTO + ((linhas.size() + 1) * ALTURA_LINHA), paintText);


        return outPut;
    }

    @Override
    public String onCopy() {
        int pag = viewPager.getCurrentItem();
        int qty = viewPager.getAdapter().getCount();

        String[] itens = null;

        StringBuilder sb = new  StringBuilder();

        if(qty == 3){
            switch (pag){
                case 0:
                    itens = itensToShareP;
                    sb.append(tituloP);
                    break;
                case 1:
                    itens = itensToShareSal;
                    sb.append(tituloSal);
                    break;
                case 2:
                    itens = itensToShareE;
                    sb.append(tituloE);
                    break;
                 default:break;
            }
        }else {
            switch (pag){
                case 0:
                    itens = itensToShareP;
                    sb.append(tituloP);
                    break;
                case 1:
                    itens = itensToShareSal;
                    sb.append(tituloSal);
                    break;
                case 2:
                    itens = itensToShareS;
                    sb.append(tituloS);
                    break;
                case 3:
                    itens = itensToShareE;
                    sb.append(tituloE);
                    break;
                default:break;
            }
        }



        sb.append("\n");
        sb.append("\n");

        for (String s: itens){
            sb.append(s);

        }

        sb.append("\n");

        return sb.toString();
    }


    private String[] retornaProclamacao() {

        List<String> itens = new ArrayList<>();

        for (PassagemTexto p : passagemTextos) {
            if (p.getTitulo() != null) {
                itens.add(p.getTitulo());
            }
        }

        // itens.


        String output = "output";

        String[] itensOutput = new String[2];

        if (viewPager.getAdapter().getCount() == 4) {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    itensOutput[0] = "Primeira Leitura: " + itens.get(viewPager.getCurrentItem());
                    break;
                case 1:
                    itensOutput[0] = "Salmo: " + itens.get(viewPager.getCurrentItem());
                    break;
                case 2:
                    itensOutput[0] = "Segunda Leitura: " + itens.get(viewPager.getCurrentItem());
                    break;
                case 3:
                    itensOutput[0] = "Evangelho: " + itens.get(viewPager.getCurrentItem());
                    break;
                default:
                    break;
            }
        } else {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    itensOutput[0] = "Primeira Leitura: " + itens.get(viewPager.getCurrentItem());
                    break;
                case 1:
                    itensOutput[0] = "Salmo: " + itens.get(viewPager.getCurrentItem());
                    break;
                case 2:
                    itensOutput[0] = "Evangelho: " + itens.get(viewPager.getCurrentItem());
                    break;
                default:
                    break;
            }
        }

        itensOutput[1] = getTituloAnuncioLivro(itens.get(viewPager.getCurrentItem()));

        return itensOutput;
    }

    private String getTituloAnuncioLivro(String passagem) {

        String outPut;
        String[] partes = passagem.split(",");
        String salmo = partes[0].split("\\s")[0];
        String livroAbr = partes[0].replaceAll("\\d", "");

        livroAbr = livroAbr.trim();

        String[] abrs = getResources().getStringArray(R.array.abrev_livros_novo_testam);
        String[] titulos = getResources().getStringArray(R.array.lista_titulos_leitura);
        LeitorDeBiblia leitorDeBiblia = new LeitorDeBiblia(this);
        String nomeDoLivro = leitorDeBiblia.getNomeLivroByAbreviatura(livroAbr);

        //Chegar o livro;

        CatolicApp.logCatolicApp("Livro Abr " + livroAbr + " nome " + nomeDoLivro);

        int cont = 0;
        for (String s : abrs) {
            CatolicApp.logCatolicApp("Livro Abr " + livroAbr + " s " + s + " fora do if");
            if (livroAbr.equals(s)) {
                outPut = titulos[cont];
                CatolicApp.logCatolicApp("Livro Abr " + livroAbr + " s " + s);
                return outPut;
            }
            cont++;
        }

        if (nomeDoLivro != null) {
            outPut = "Leitura de " + nomeDoLivro;
        } else {
            outPut = "Leitura do Salmo " + salmo;
        }

        return outPut;
    }


    private String[] determinateItens() {
        int page = viewPager.getCurrentItem();
        int qty = viewPager.getAdapter().getCount();

        if (page == 0) {
            return itensToShareP;
        } else {
            if (qty == 3) {
                switch (page) {
                    case 1:
                        return itensToShareSal;
                    case 2:
                        return itensToShareE;
                    default:
                        return null;
                }
            } else {
                switch (page) {
                    case 1:
                        return itensToShareSal;
                    case 2:
                        return itensToShareS;
                    case 3:
                        return itensToShareE;
                    default:
                        return null;
                }
            }
        }
    }

    private String saveImage(Bitmap bitmap) throws IOException {

        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File arquivo = new File(diretorio, "teste.jpg");

        Log.i("teste_share", diretorio.getAbsolutePath());

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(arquivo);
            if (fos == null) {
                Log.i("teste_share", "fos = null");
            } else {
                Log.i("teste_share", "fos != null");
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return diretorio.getAbsolutePath();
    }


    private List<String> construirLinhas(String[] itensToShare, float maiorSize, Paint paint) {

        String[] palavras;

        List<String> lista = new ArrayList<>();
        List<Integer> itens = new ArrayList<>();

        StringBuilder aux = new StringBuilder();
        float cont = 0;

        for (String vers : itensToShare) {

            if (vers != null) {

                Log.i("teste_", "vers " + vers);
                palavras = vers.split(" ");

                for (int i = 0; i < palavras.length; i++) {
                    if (i == 0 && Character.isDigit(palavras[0].charAt(0))) {
                        aux.append("      ");
                        cont += paint.measureText("      ");
                        itens.add(new Integer(Integer.parseInt(palavras[0].replace(".", ""))));
                    } else {
                        cont += paint.measureText(palavras[i] + " ");
                        if (cont < maiorSize) {
                            aux.append(palavras[i]);
                            aux.append(" ");
                        } else {
                            lista.add(aux.toString());
                            aux.delete(0, aux.length() - 1);
                            aux.append(palavras[i]);
                            aux.append(" ");
                            if (i == palavras.length - 1) {
                                cont = 0;
                            } else {
                                cont = paint.measureText(palavras[i] + " ");
                            }
                        }
                    }
                }

                lista.add(aux.toString());
                aux.delete(0, aux.length() - 1);
                cont = 0;
            } else {
                Log.i("teste_", "vers null");
            }


        }

        lista.add(" ");

        return lista;
    }

    private String constuirAssinatura(float maiorSize, Paint paint) {

        //   paint.setTypeface(Typeface.createFromAsset(getAssets(),"itcedscr.ttf"));
        String assinatura = "CatolicApp";
        StringBuilder sb = new StringBuilder(" ");
        float tam = paint.measureText(assinatura);
        float total = 0;

        while (total < maiorSize) {
            sb.append(" ");
            total = paint.measureText(sb.toString()) + tam;
        }
        sb.append(assinatura);

        return sb.toString();
    }


    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    public PassagemTexto[] setarPaginas(ItemAgenda itemAgenda) {

        Log.i("teste_agenda", "testando data " + itemAgenda.getData());


        String p = itemAgenda.getPrimeira();
        String sal = itemAgenda.getSalmo();
        String s = itemAgenda.getSegunda();
        String ev = itemAgenda.getEvangelho();

        p = p.split("ou")[0];
        sal = sal.split("ou")[0];
        s = s.split("ou")[0];
        ev = ev.split("ou")[0];

        InterpretadorDePassagem interpretador = new InterpretadorDePassagem(this);
        PassagemBiblica primeira = interpretador.parseLeitura(p);
        Log.i("teste_passagens", "passou primeira");
        PassagemBiblica salmo = interpretador.parseLeitura(sal);
        Log.i("teste_passagens", "passou salmo");
        PassagemBiblica segunda = null;
        if (!s.contains("null")) {
            segunda = interpretador.parseLeitura(s);
            Log.i("teste_passagens", "passou segunda");
        }
        PassagemBiblica evangelho = interpretador.parseLeitura(ev);
        Log.i("teste_passagens", "passou evangelho");

        List<PassagemTexto> lista = new ArrayList<>();
        if (primeira != null) {
            itensToShareP = pegarItensToShare(primeira);
            lista.add(new PassagemTexto(primeira.getPassagem(), pegarTexto(primeira)));
        }
        if (salmo != null) {
            itensToShareSal = pegarItensToShare(salmo);
            lista.add(new PassagemTexto(salmo.getPassagem(), pegarTexto(salmo)));
        }
        if (segunda != null) {
            itensToShareS = pegarItensToShare(segunda);
            lista.add(new PassagemTexto(segunda.getPassagem(), pegarTexto(segunda)));
        }
        if (evangelho != null) {
            itensToShareE = pegarItensToShare(evangelho);
            lista.add(new PassagemTexto(evangelho.getPassagem(), pegarTexto(evangelho)));
        }

        return lista.toArray(new PassagemTexto[lista.size()]);
    }

    @NonNull
    private String pegarTexto(PassagemBiblica passagem) {

        int testamento = passagem.getTestamento();
        int livro = passagem.getLivro();

        Livro livroCompleto = RepositorioBibliaDAO.getInstance(this).listarLivro(testamento, livro);

        List<ItemLinha> linhas = passagem.getLinhas();

        StringBuilder sb = new StringBuilder();

        int auxInicio;
        int auxFim;
        boolean ateoFim = false;

        for (ItemLinha item : linhas) {
            if (item.isAteOFim()) {
                ateoFim = true;
                auxInicio = item.getVersiculo() - 1;
                Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                auxFim = livroCompleto.getCapitulos().get(item.getCapitulo() - 1).size();
                Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                for (int i = auxInicio; i < auxFim; i++) {
                    Log.i("teste_agenda", "testamento " + Integer.toString(passagem.getTestamento()));
                    Log.i("teste_agenda", "livro " + Integer.toString(passagem.getLivro()));
                    Log.i("teste_agenda", "cap " + Integer.toString(item.getCapitulo()));
                    Log.i("teste_agenda", "ver " + livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(item.getVersiculo() - 1));
                    sb.append(livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(i));
                    sb.append("\n");
                    sb.append("\n");
                }

            } else {
//                Log.i("teste_agenda","testamento "+Integer.toString(passagem.getTestamento()));
//                Log.i("teste_agenda","livro "+Integer.toString(passagem.getLivro()));
//                Log.i("teste_agenda","cap "+Integer.toString(item.getCapitulo()));
//                Log.i("teste_agenda","ver "+livroCompleto.getCapitulos().get(item.getCapitulo()-1).get(item.getVersiculo()-1));

                if (ateoFim) {
                    ateoFim = false;
                    auxInicio = 0;
                    Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                    auxFim = item.getVersiculo() - 1;

                    Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                    for (int i = auxInicio; i <= auxFim; i++) {
//                        Log.i("teste_agenda","testamento "+Integer.toString(passagem.getTestamento()));
//                        Log.i("teste_agenda","livro "+Integer.toString(passagem.getLivro()));
//                        Log.i("teste_agenda","cap "+Integer.toString(item.getCapitulo()));
//                        Log.i("teste_agenda","ver "+livroCompleto.getCapitulos().get(item.getCapitulo()-1).get(item.getVersiculo()-1));
                        sb.append(livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(i));
                        sb.append("\n");
                        sb.append("\n");
                    }

                } else {
                    livroCompleto.getCapitulos().get(item.getCapitulo() - 1);
                    livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(item.getVersiculo() - 1);
                    sb.append(livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(item.getVersiculo() - 1));
                    sb.append("\n");
                    sb.append("\n");
                }


            }
        }

        return sb.toString();
    }


    private String[] pegarItensToShare(PassagemBiblica passagem) {

        int testamento = passagem.getTestamento();
        int livro = passagem.getLivro();

        Livro livroCompleto = RepositorioBibliaDAO.getInstance(this).listarLivro(testamento, livro);

        List<ItemLinha> linhas = passagem.getLinhas();

        List<String> itensToShare = new ArrayList<>();

        int auxInicio;
        int auxFim;
        boolean ateoFim = false;

        for (ItemLinha item : linhas) {
            if (item.isAteOFim()) {
                ateoFim = true;
                auxInicio = item.getVersiculo() - 1;
                Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                auxFim = livroCompleto.getCapitulos().get(item.getCapitulo() - 1).size();
                Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                for (int i = auxInicio; i < auxFim; i++) {
                    Log.i("teste_agenda", "testamento " + Integer.toString(passagem.getTestamento()));
                    Log.i("teste_agenda", "livro " + Integer.toString(passagem.getLivro()));
                    Log.i("teste_agenda", "cap " + Integer.toString(item.getCapitulo()));
                    Log.i("teste_agenda", "ver " + livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(item.getVersiculo() - 1));
                    itensToShare.add(livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(i));
                    itensToShare.add("\n");
                    itensToShare.add("\n");
                }

            } else {
//                Log.i("teste_agenda","testamento "+Integer.toString(passagem.getTestamento()));
//                Log.i("teste_agenda","livro "+Integer.toString(passagem.getLivro()));
//                Log.i("teste_agenda","cap "+Integer.toString(item.getCapitulo()));
//                Log.i("teste_agenda","ver "+livroCompleto.getCapitulos().get(item.getCapitulo()-1).get(item.getVersiculo()-1));

                if (ateoFim) {
                    ateoFim = false;
                    auxInicio = 0;
                    Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                    auxFim = item.getVersiculo() - 1;

                    Log.i("teste_agenda", " inicio " + (item.getVersiculo() - 1));
                    for (int i = auxInicio; i <= auxFim; i++) {
//                        Log.i("teste_agenda","testamento "+Integer.toString(passagem.getTestamento()));
//                        Log.i("teste_agenda","livro "+Integer.toString(passagem.getLivro()));
//                        Log.i("teste_agenda","cap "+Integer.toString(item.getCapitulo()));
//                        Log.i("teste_agenda","ver "+livroCompleto.getCapitulos().get(item.getCapitulo()-1).get(item.getVersiculo()-1));
                        itensToShare.add(livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(i));
                        itensToShare.add("\n");
                        itensToShare.add("\n");
                    }

                } else {
                    livroCompleto.getCapitulos().get(item.getCapitulo() - 1);
                    livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(item.getVersiculo() - 1);
                    itensToShare.add(livroCompleto.getCapitulos().get(item.getCapitulo() - 1).get(item.getVersiculo() - 1));
                    itensToShare.add("\n");
                    itensToShare.add("\n");
                }


            }
        }

        return itensToShare.toArray(new String[itensToShare.size()]);
    }

}




