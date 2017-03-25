package com.alexandrelunkes.catolicapp.paginas;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.CatolicApp;
import com.alexandrelunkes.catolicapp.CustomView;

import com.alexandrelunkes.catolicapp.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class CompartilharComFaceBook extends AppCompatActivity implements View.OnClickListener{

    private Button btLogin;
    private View imageView;
    private TextView textView;
    private Bitmap imagemTeste;
    private CustomView customView;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ShareButton shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_compartilhar_com_face_book);


        setFacebookConfigs();

        textView = (TextView) findViewById(R.id.texto_to_share);

        Bundle args = getIntent().getExtras();




    }

    private void setFacebookConfigs() {

        shareButton = (ShareButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        Bitmap imagem = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagem)
                .build();

        SharePhotoContent contentPhoto = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareButton.setShareContent(contentPhoto);


    }

   class MyDrawView extends View{

        Paint paint = new Paint();


        public MyDrawView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawLine(0, 0, 20, 20, paint);
            canvas.drawLine(20, 0, 0, 20, paint);
        }
    }




    private Bitmap loadBitmapFromView(View v) {
        if(v.getLayoutParams() == null){
            CatolicApp.logCatolicApp("Compartilhar null");
        }

        ViewGroup.LayoutParams params = v.getLayoutParams();

        params.height = 100;
        params.width = 100;

        Bitmap b = Bitmap.createBitmap( params.width, params.height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }


    public void fazerLogin(){



    }


    @Override
    public void onClick(View v) {

        //https://padrepauloricardo.org/

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://padrepauloricardo.org/"))
                .build();


        Bitmap imagem = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(customView.getBitmap())
                .build();

        SharePhotoContent contentPhoto = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();


        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(contentPhoto, ShareDialog.Mode.AUTOMATIC);

    }


}
