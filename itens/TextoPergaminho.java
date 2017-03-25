package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Alexandre Lunkes on 27/08/2016.
 */
public class TextoPergaminho extends TextView {
    public TextoPergaminho(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"vijaya.ttf");
        this.setTypeface(typeface);
    }

    public TextoPergaminho(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"vijaya.ttf");
        this.setTypeface(typeface);
    }

    public TextoPergaminho(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"vijaya.ttf");
        this.setTypeface(typeface);
    }

}
