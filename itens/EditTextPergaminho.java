package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.alexandrelunkes.catolicapp.R;

/**
 * Created by Alexandre Lunkes on 27/08/2016.
 */
public class EditTextPergaminho extends EditText {
    public EditTextPergaminho(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"vijaya.ttf");
        this.setTypeface(typeface);
    }

    public EditTextPergaminho(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"vijaya.ttf");
        this.setTypeface(typeface);
    }

    public EditTextPergaminho(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"vijaya.ttf");
        this.setTypeface(typeface);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundResource(R.drawable.pergaminho_catolicapp);
        setTextColor(Color.BLACK);
        int paddingLeftAndRight = (int) (getResources().getDisplayMetrics().density * 15);
        int paddingTopAndBotton = (int) (getResources().getDisplayMetrics().density * 15);
        setPadding(paddingLeftAndRight,paddingTopAndBotton,paddingLeftAndRight,paddingTopAndBotton);

    }
}
