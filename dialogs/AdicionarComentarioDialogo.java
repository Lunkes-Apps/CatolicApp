package com.alexandrelunkes.catolicapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.favoritos.Favorito;
import com.alexandrelunkes.catolicapp.favoritos.RepositorioFavoritosDAO;
import com.alexandrelunkes.catolicapp.itens.BotaoCatolic;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;
import com.alexandrelunkes.catolicapp.paginas.ComentarioActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Alexandre Lunkes on 24/08/2016.
 */
public class AdicionarComentarioDialogo extends DialogFragment implements View.OnClickListener{

    private BotaoCatolic ok;
    private BotaoCatolic cancelar;
    private Favorito f;

    private TextoPergaminho titulo;

    private boolean pergunta;
    private String tituloPergunta;

    private OnReceiveAnswerListener myCallBack;

    public static AdicionarComentarioDialogo newInstance(Favorito f) {

        Bundle args = new Bundle();
        args.putSerializable("favorito",f);
        args.putBoolean("primeiraPergunta",false);

        AdicionarComentarioDialogo fragment = new AdicionarComentarioDialogo();
        fragment.setArguments(args);
        return fragment;
    }


    public static AdicionarComentarioDialogo newInstance() {

        Bundle args = new Bundle();
        args.putBoolean("primeiraPergunta",true);
        AdicionarComentarioDialogo fragment = new AdicionarComentarioDialogo();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_comentario, null);



        pergunta = getArguments().getBoolean("primeiraPergunta");
        if(pergunta){
            tituloPergunta = "Deseja adicionar os versículos aos favoritos?";
        }else {
            f = (Favorito) getArguments().getSerializable("favorito");
            RepositorioFavoritosDAO dao = RepositorioFavoritosDAO.getInstance(getActivity());
            f.setId(dao.salvarFavorito(f));
            tituloPergunta = "Desja adicionar um comentário?";
        }


        ok = (BotaoCatolic) v.findViewById(R.id.ok);
        cancelar = (BotaoCatolic) v.findViewById(R.id.cancelar);

        ok.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        titulo = (TextoPergaminho) v.findViewById(R.id.titulo);
        if(titulo == null){
            Log.i("teste_coment"," null titutlo");
        }
        titulo.setText(tituloPergunta);


        builder.setView(v);

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(10,0,0,0)));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

        getDialog().getWindow().setAttributes(p);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        if(pergunta){
            clickOkAddFav(v);
        }else {
            clickOkAddComment(v);
        }


    }


    void clickOkAddFav(View v){
        switch (v.getId()){
            case R.id.ok:
                myCallBack.onAnswer(1);
                dismiss();
                break;
            case R.id.cancelar:
                myCallBack.onAnswer(0);
                dismiss();
                break;
            default: dismiss();break;
        }
    }

    void clickOkAddComment(View v){
        switch (v.getId()){
            case R.id.ok:
                Intent i = new Intent(getActivity(), ComentarioActivity.class);
                i.putExtra("favorito",f);
                getActivity().startActivity(i);
                dismiss();
                break;
            case R.id.cancelar:
                dismiss();
                break;
            default: dismiss();break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myCallBack = (OnReceiveAnswerListener)activity;
    }

    public interface OnReceiveAnswerListener{
        void onAnswer(int answer);
    }

}
