package com.alexandrelunkes.catolicapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.Tasks.AnimationTexts;
import com.alexandrelunkes.catolicapp.dialogs.ShareOptionsDialog;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 14/05/2016.
 */

public class ItemAdapterPagerLeituras extends Fragment implements AnimationTexts.OnExecuteAnimationText{

    private TextoPergaminho titulo;
    private TextoPergaminho texto;
    public static final String TITULO = "titulo";
    public static final String TEXTO = "texto";
    public static final String LOG = "ItemAdapterLeituras ";
    public static final int INICIO_TEXTO = 100;
    public static final int INICIO_PARAGRAFO = 50;
    public static final int TAM_LETRA = 15;
    public static final int TAM_LINHA = TAM_LETRA*69;
    public static final int LARGURA_PAG = INICIO_PARAGRAFO+TAM_LINHA;
    public static final int ALTURA_LINHA = 35;

    private ShareButton shareButton;
    private String text;
    private String data;
    private int linhaDoMaior = 0;

    private ViewGroup.LayoutParams params;
    public static ItemAdapterPagerLeituras newInstance(String titulo, String texto, String data) {

        Bundle args = new Bundle();
        args.putString(TITULO, titulo);
        args.putString(TEXTO, texto);
        args.putString("data",data);
        ItemAdapterPagerLeituras fragment = new ItemAdapterPagerLeituras();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_collection_pager_liturgia,null);

        titulo = (TextoPergaminho) v.findViewById(R.id.textView_titulo_page);
        texto = (TextoPergaminho) v.findViewById(R.id.textView_leitura_page);
        final Bundle args = getArguments();

        titulo.setText(args.getString(TITULO));
        text = args.getString(TEXTO);
        data = args.getString("data");
        texto.setText(text);
      //  shareButton = (ShareButton) v.findViewById(R.id.button_share_leitura);

        texto.post(new Runnable() {
            @Override
            public void run() {

                executarAnimation();

             //   setShareButton();


            }


        });



         return v;
    }

    private void setShareButton() {
//
//        SharePhoto textoPhoto = new SharePhoto.Builder()
//                .setBitmap(getBitmapNinePatch(R.drawable.pergaminhovertical,
//                        LARGURA_PAG,500,getContext(),text))
//                .build();
//
//        SharePhotoContent content = new SharePhotoContent.Builder()
//                .addPhoto(textoPhoto)
//                .build();
//
//        shareButton.setShareContent(content);

    }

    private Bitmap getBitmapNinePatch(int id, int x, int y, Context context, String texto){

        String[] linhas = getTextLinhas(texto);
        String[] partesInicio = titulo.getText().toString().split("\n");


        int positionLinha = 0;
        int l= linhas.length;
        int tamanhoTotal = partesInicio.length+l+2;
        y = (2*INICIO_TEXTO)+(tamanhoTotal*ALTURA_LINHA);
        CatolicApp.logCatolicApp("linhaDoMaior = "+linhaDoMaior);
        x = (linhas[linhaDoMaior].length()*TAM_LETRA);



        Bitmap bitmapPergaminho = BitmapFactory.decodeResource(context.getResources(),id);
        byte[] chunk = bitmapPergaminho.getNinePatchChunk();
        NinePatchDrawable npdPergaminho = new NinePatchDrawable(bitmapPergaminho,chunk,new Rect(),null);
        npdPergaminho.setBounds(0,0,x,y);

        CatolicApp.logCatolicApp("X e Y"+ x+" "+y);

        Bitmap outPut = Bitmap.createBitmap(x,y,Bitmap.Config.ARGB_8888);
        Canvas canvasPergaminho = new Canvas(outPut);
        npdPergaminho.draw(canvasPergaminho);

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "vijaya.ttf"));
        paintText.setTextSize(25);
        paintText.setStyle(Paint.Style.FILL);

        canvasPergaminho.drawText("Liturgia de "+data,INICIO_PARAGRAFO,INICIO_TEXTO,paintText);
        positionLinha++;
        positionLinha++;
        canvasPergaminho.drawText(partesInicio[0],INICIO_PARAGRAFO,INICIO_TEXTO+(positionLinha*ALTURA_LINHA),paintText);
        positionLinha++;

        canvasPergaminho.drawText(getTituloAnuncioLivro(partesInicio[1]),
                INICIO_PARAGRAFO,INICIO_TEXTO+(positionLinha*ALTURA_LINHA),paintText);
        positionLinha++;
        positionLinha++;


       for(int i = 0; i<l; i++){

         if(linhas[i]!=null){
             CatolicApp.logCatolicApp(" tamanho "+linhas[i].length() +" linha n "+i);
             canvasPergaminho.drawText(linhas[i],INICIO_PARAGRAFO,INICIO_TEXTO+(positionLinha*ALTURA_LINHA),paintText);
             positionLinha++;
         }
         else CatolicApp.logCatolicApp("Titulo 'e "+titulo.getText().toString() + " "+i+" "+l);
       }

        return outPut;
    }

    private String[] getTextLinhas(String texto) {

        StringBuilder sb = new StringBuilder();
        ArrayList <String> outPut= new ArrayList<>();
        String[] palavras = texto.split("\\s");

        int maior = 0;
        int contlinha = 0;
        linhaDoMaior = 0;
        int contLetras = 0;

        for(String s: palavras){
            contLetras += s.length() + 1;

            if(s.length()!=0) {
                if (contLetras > 70 || Character.isDigit(s.charAt(0))) {
                    if(sb.toString().length()>maior){
                        maior = sb.toString().length();
                        linhaDoMaior = contlinha;
                    }
                    outPut.add(sb.toString());
                    contlinha++;
                    contLetras = s.length();
                    if (sb.length() > 0) sb.delete(0, sb.length() - 1);
                }
                if (s != null){
                    sb.append(s + " ");
                }
            }
        }

        outPut.add(sb.toString());
        return outPut.toArray(new String[outPut.size()]);
    }

    private String getTituloAnuncioLivro(String passagem){

        String outPut;
        String[] partes = passagem.split(",");
        String salmo = partes[0].split("\\s")[0];
        String livroAbr = partes[0].replaceAll("\\d","");

        livroAbr = livroAbr.trim();

        String[] abrs = getResources().getStringArray(R.array.abrev_livros_novo_testam);
        String[] titulos = getResources().getStringArray(R.array.lista_titulos_leitura);
        LeitorDeBiblia leitorDeBiblia = new LeitorDeBiblia(getContext());
        String nomeDoLivro = leitorDeBiblia.getNomeLivroByAbreviatura(livroAbr);

        //Chegar o livro;

        CatolicApp.logCatolicApp("Livro Abr "+livroAbr+ " nome "+nomeDoLivro);

        int cont = 0;
        for(String s: abrs){
            CatolicApp.logCatolicApp("Livro Abr "+livroAbr+" s "+s+" fora do if");
            if(livroAbr.equals(s)){
               outPut = titulos[cont];
               CatolicApp.logCatolicApp("Livro Abr "+livroAbr+" s "+s);
               return outPut;
            }
            cont++;
        }

        if(nomeDoLivro!=null){
            outPut = "Leitura de "+ nomeDoLivro;
        }else {
            outPut = "Leitura do Salmo "+ salmo;
        }


        return outPut;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onIncrementaTamanho(int unidade) {
         params = texto.getLayoutParams();
         params.height += unidade;
         texto.setLayoutParams(params);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    public void executarAnimation(){
        new AnimationTexts(this,texto).execute();
    }


    @Override
    public boolean getFragmentIsVisible() {

        return true;
    }




}
