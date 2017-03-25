package com.alexandrelunkes.catolicapp.itens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.alexandrelunkes.catolicapp.R;

/**
 * Created by Alexandre Lunkes on 27/08/2016.
 */
public class BotaoCatolic extends TextoPergaminho {

    private AnimationDrawable animationDrawable;

    public BotaoCatolic(Context context) {
        super(context);
    }

    public BotaoCatolic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BotaoCatolic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundResource(R.drawable.animation_button);
        int paddingLeftAndRight = (int) (getResources().getDisplayMetrics().density * 15);
        int paddingTopAndBotton = (int) (getResources().getDisplayMetrics().density * 5);
        setPadding(paddingLeftAndRight,paddingTopAndBotton,paddingLeftAndRight,paddingTopAndBotton);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 1:
                animationDrawable = (AnimationDrawable) this.getBackground();
                animationDrawable.stop();
                animationDrawable.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("teste", "onTouch");
                break;
            default:break;

        }

        Log.i("teste euconsumi", String.valueOf(event.getAction()));

        return super.onTouchEvent(event);
    }


}
