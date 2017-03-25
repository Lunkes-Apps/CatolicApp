package com.alexandrelunkes.catolicapp.itens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Alexandre Lunkes on 05/09/2016.
 */
public class ClipBoardCatolicApp {

    private String texto;
    private ClipboardManager clipboardManager;
    private Context context;

    public ClipBoardCatolicApp(Context context) {
        this.context = context;

        clipboardManager = (ClipboardManager)
              context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void copiarTexto(String texto){

        StringBuilder sb = new StringBuilder(texto);

        sb.append("CatolicApp");

        ClipData clipData = ClipData.newPlainText("copiadoDeCatolicApp",sb.toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "Texto foi copiado", Toast.LENGTH_LONG).show();
    }



}
