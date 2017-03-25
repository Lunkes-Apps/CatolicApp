package com.alexandrelunkes.catolicapp.paginas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.favoritos.Favorito;
import com.alexandrelunkes.catolicapp.favoritos.RepositorioFavoritosDAO;
import com.alexandrelunkes.catolicapp.itens.BotaoCatolic;
import com.alexandrelunkes.catolicapp.itens.EditTextPergaminho;

public class ComentarioActivity extends AppCompatActivity implements View.OnClickListener{

    private BotaoCatolic ok;
    private EditTextPergaminho editText;
    private Favorito f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);


        f =(Favorito) getIntent().getExtras().getSerializable("favorito");

        ok = (BotaoCatolic) findViewById(R.id.botao_comentario);
        editText = (EditTextPergaminho) findViewById(R.id.edit_text_comentario);


        if(f.getHasAnota()){
            editText.setText(f.getAnotacao());
        }

        ok.setOnClickListener(this);


        setAppBar();
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
        finish();
    }

    private void setAppBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setSubtitle("Comentário "+f.getPassagem());
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        String comentario = editText.getText().toString();

        if(editText.getText().toString().equals("")){
            f.setAnotacao(null);
            f.setHasAnota(false);
        }else {
            f.setAnotacao(comentario);
            f.setHasAnota(true);
        }

        RepositorioFavoritosDAO dao = RepositorioFavoritosDAO.getInstance(this);
        dao.atualizar(f);

        Intent i = new Intent();
        i.putExtra("favorito",f);
        setResult(1,i);
        super.onBackPressed();
    }
}
