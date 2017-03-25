package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Alexandre Lunkes on 15/08/2016.
 */
public class ToolbarNewFont extends Toolbar {

    private Context context;

    public ToolbarNewFont(Context context) {
        this(context,null);
    }

    public ToolbarNewFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public ToolbarNewFont(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Class objeto = this.getClass();

        try {
           Field field = objeto.getDeclaredField("mTitleTextView");
           TextView textView = (TextView) field.get(new TextView(context));
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "itcedscr.ttf"));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
