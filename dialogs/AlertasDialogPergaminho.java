package com.alexandrelunkes.catolicapp.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.BotaoCatolic;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;

/**
 * Created by Alexandre Lunkes on 14/09/2016.
 */
public class AlertasDialogPergaminho extends DialogFragment implements View.OnClickListener {

    private TextoPergaminho titlo, msg;
    private BotaoCatolic bt;

    public static AlertasDialogPergaminho newInstance(String titulo, String msg) {

        Bundle args = new Bundle();

        args.putString("titulo",titulo);
        args.putString("msg",msg);

        AlertasDialogPergaminho fragment = new AlertasDialogPergaminho();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String t = getArguments().getString("titulo");
        String m = getArguments().getString("msg");


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_alertas,null);
        titlo = (TextoPergaminho) v.findViewById(R.id.titulo);
        msg = (TextoPergaminho) v.findViewById(R.id.msg);
        bt = (BotaoCatolic) v.findViewById(R.id.ok);
        bt.setOnClickListener(this);
        titlo.setText(t);
        msg.setText(m);

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
        dismiss();
    }


}
