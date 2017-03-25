package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 29/08/2016.
 */
public class CompartilhadorDeBitmap {

    public static final int INICIO_TEXTO = 100;
    public static final int INICIO_PARAGRAFO = 50;
    public static final int TAM_LETRA = 15;
    public static final int TAM_LINHA = TAM_LETRA * 69;
    public static final int LARGURA_PAG = INICIO_PARAGRAFO + TAM_LINHA;
    public static final int ALTURA_LINHA = 35;

    private Context context;
    private int testamento;
    private List<Integer> selecionados;
    private int liv;
    private int cap;

    public CompartilhadorDeBitmap(Context context) {
        this.context = context;
    }

    public CompartilhadorDeBitmap(Context context, int testamento, List<Integer> selecionados, int liv, int cap) {
        this.context = context;
        this.testamento = testamento;
        this.selecionados = selecionados;
        this.liv = liv;
        this.cap = cap;
    }

    private String contruirPassagem(int testamento, List<Integer> selecionados, int liv, int cap) {
        String abr;
        StringBuilder aux = new StringBuilder();
        List<Integer> itens = selecionados;

        if (testamento == 0) {
            abr = context.getResources().getStringArray(R.array.abrev_livros_antigo_testam)[liv];
        } else {
            abr = context.getResources().getStringArray(R.array.abrev_livros_novo_testam)[liv];
        }

        aux.append("(" + abr + " " + cap + ", ");
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

    private List<String> construirLinhas(String[] itensToShare, float maiorSize, Paint paint) {

        String[] palavras;


        List<String> lista = new ArrayList<>();
        List<Integer> itens = new ArrayList<>();

        lista.add("CatolicApp");
        lista.add("");

        StringBuilder aux = new StringBuilder();
        float cont = 0;

        for (String vers : itensToShare) {
            palavras = vers.split(" ");
            for (int i = 0; i < palavras.length; i++) {
                if (i == 0) {
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

        }

        lista.add(" ");

        if (aux.length() > 0) aux.delete(0, aux.length() - 1);
        String abr;

        lista.add(contruirPassagem(getTestamento(),getSelecionados(),getLiv(),getCap()));

        return lista;
    }

    public Bitmap getBitMap(String[] itensToShare){
        float maiorSize = 400;
        int x = (int) (maiorSize + 150);


        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);

        paintText.setTypeface(Typeface.createFromAsset(context.getAssets(), "vijaya.ttf"));
        paintText.setTextSize(30);


        List<String> linhas = construirLinhas(itensToShare, maiorSize, paintText);
        int y = INICIO_TEXTO + (linhas.size() * ALTURA_LINHA) + (2 * ALTURA_LINHA);

        int id = R.drawable.pergaminhovertical;

        Bitmap bitmapPergaminho = BitmapFactory.decodeResource(context.getResources(), id);
        byte[] chunk = bitmapPergaminho.getNinePatchChunk();
        NinePatchDrawable npdPergaminho = new NinePatchDrawable(bitmapPergaminho, chunk, new Rect(), null);
        npdPergaminho.setBounds(0, 0, x, y);

        CatolicApp.logCatolicApp("X e Y" + x + " " + y);

        Bitmap outPut = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);

        Canvas canvasPergaminho = new Canvas(outPut);
        npdPergaminho.draw(canvasPergaminho);

        paintText.setTextSize(45);
        paintText.setTypeface(Typeface.createFromAsset(context.getAssets(), "itcedscr.ttf"));

        canvasPergaminho.drawText(linhas.get(0), INICIO_PARAGRAFO, INICIO_TEXTO + (0 * ALTURA_LINHA), paintText);

        paintText.setTypeface(Typeface.createFromAsset(context.getAssets(), "vijaya.ttf"));
        paintText.setTextSize(30);


        for (int i = 1; i < linhas.size(); i++) {

           canvasPergaminho.drawText(linhas.get(i), INICIO_PARAGRAFO, INICIO_TEXTO + (i * ALTURA_LINHA), paintText);

        }


//        try {
//            Log.i("teste_salvar", "salvando em " + saveImage(outPut));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return outPut;
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getTestamento() {
        return testamento;
    }

    public void setTestamento(int testamento) {
        this.testamento = testamento;
    }

    public List<Integer> getSelecionados() {
        return selecionados;
    }

    public void setSelecionados(List<Integer> selecionados) {
        this.selecionados = selecionados;
    }

    public int getLiv() {
        return liv;
    }

    public void setLiv(int liv) {
        this.liv = liv;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }





}
