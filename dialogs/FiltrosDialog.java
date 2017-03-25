package com.alexandrelunkes.catolicapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.BotaoCatolic;

/**
 * Created by Alexandre Lunkes on 13/09/2016.
 */
public class FiltrosDialog extends DialogFragment implements View.OnClickListener {


    private RadioButton t;
    private RadioButton antigo;
    private RadioButton novo;
    private BotaoCatolic ok;
    private OnSelectFiltroListener myCallBack;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_filtros_layout, null);

        t = (RadioButton) v.findViewById(R.id.rad1);
        t.setOnClickListener(this);
        antigo = (RadioButton) v.findViewById(R.id.rad2);
        antigo.setOnClickListener(this);
        novo = (RadioButton) v.findViewById(R.id.rad3);
        novo.setOnClickListener(this);
        ok = (BotaoCatolic) v.findViewById(R.id.ok);
        ok.setOnClickListener(this);

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

        switch (v.getId()) {

            case R.id.rad1:
                antigo.setChecked(false);
                novo.setChecked(false);
                break;
            case R.id.rad2:
                t.setChecked(false);
                novo.setChecked(false);
                break;
            case R.id.rad3:
                t.setChecked(false);
                antigo.setChecked(false);
                break;
            case R.id.ok:
                if(myCallBack!=null) {
                    int selected = 0;
                    if(t.isChecked())selected=1;
                    if(antigo.isChecked())selected=2;
                    if(novo.isChecked())selected=3;
                    myCallBack.filtroSelecionado(selected);
                }
                dismiss();
                break;
            default:
                break;
        }

    }

    public interface OnSelectFiltroListener {

        void filtroSelecionado(int i);

    }

    public void setOnSelectFiltroListener(OnSelectFiltroListener selectFiltroListener){
        myCallBack = selectFiltroListener;
    }


}
