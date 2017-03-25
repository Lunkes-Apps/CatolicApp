package com.alexandrelunkes.catolicapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Alexandre Lunkes on 24/05/2016.
 */
public class CustomView extends View {

    private Paint paint = new Paint();
    private String text;
    int sizeText;
    private Bitmap bitmap;


    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,0,0
        );

        try {
            text = (String) a.getText(R.styleable.CustomView_text);
            sizeText = (Integer) a.getInteger(R.styleable.CustomView_sizeText,20);
        }finally {
            a.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        bitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.RGB_565);
//        Canvas myCanvas = new Canvas(bitmap);
//
//        myCanvas.drawLine(0, 0, 20, 20, paint);
//        myCanvas.drawLine(20, 0, 0, 20, paint);
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        paint.setStyle(Paint.Style.FILL);
//        myCanvas.drawPaint(paint);
//
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(20);
//        myCanvas.drawText(text,10,20,paint);
//
//        Rect rectangle = new Rect(0,0,100,100);
//        canvas.drawBitmap(bitmap, null, rectangle, null);
//
//        CatolicApp.logCatolicApp("onDraw CustomView");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSizeText() {
        return sizeText;
    }

    public void setSizeText(int sizeText) {
        this.sizeText = sizeText;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

