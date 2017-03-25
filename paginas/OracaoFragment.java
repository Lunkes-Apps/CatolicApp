package com.alexandrelunkes.catolicapp.paginas;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeTexto;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.dialogs.FiltrosDialog;
import com.alexandrelunkes.catolicapp.itens.ClipBoardCatolicApp;

import java.io.IOException;

/**
 * Created by Alexandre Lunkes on 02/06/2016.
 */
public class OracaoFragment extends Fragment {

    private String oracao;
private String textoOracao;

    public static OracaoFragment newInstance(String oracao) {

        Bundle args = new Bundle();
        args.putString("oracao",oracao);

        OracaoFragment fragment = new OracaoFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_oracao,null);
        TextView tvOracao = (TextView) v.findViewById(R.id.text_view_oracao);
        TextView tvTituloOracao = (TextView) v.findViewById(R.id.text_view_titulo_oracao);

        Bundle args = getArguments();
        oracao = args.getString("oracao");
        tvTituloOracao.setText(oracao);

        textoOracao = getOracaoTexto(oracao);

        tvOracao.setText(textoOracao);

        return v;
    }

    private String getOracaoTexto(String oracao) {

        LeitorDeTexto leitor = null;
        AssetManager asset = getContext().getAssets();
        try {
            leitor = new LeitorDeTexto(asset.open(LeitorDeTexto.simplicarTitulo(oracao)+".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(leitor == null){
            CatolicApp.logCatolicApp("leitor null");
        }

        return leitor.returnAllText().toString();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.oracoes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.copy:

                StringBuilder sb = new StringBuilder(oracao);
                sb.append("\n");
                sb.append("\n");
                sb.append(textoOracao);
                sb.append("\n");
                sb.append("\n");

                ClipBoardCatolicApp clipboard = new ClipBoardCatolicApp(getActivity());
                clipboard.copiarTexto(sb.toString());
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
