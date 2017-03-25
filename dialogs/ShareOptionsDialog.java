package com.alexandrelunkes.catolicapp.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.CheckNetWorkManager;
import com.alexandrelunkes.catolicapp.itens.ClipBoardCatolicApp;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alexandre Lunkes on 13/08/2016.
 */
public class ShareOptionsDialog extends DialogFragment implements View.OnClickListener {


    private OnShareClickedListener myCallBack;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private CheckNetWorkManager netWorkManager;
    private ImageView imageFace;
    private ImageView imageInsta;
    private ImageView imageTwitter;
    private ImageView imageCopy;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.share_options_dialog_layout, null);

        imageFace = (ImageView) v.findViewById(R.id.facebook_icon);
        imageTwitter = (ImageView) v.findViewById(R.id.twitter_icon);
        imageCopy = (ImageView) v.findViewById(R.id.copy_icon);

        imageFace.setOnClickListener(this);
        imageTwitter.setOnClickListener(this);
        imageCopy.setOnClickListener(this);

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


    public void setOnShareClickedListener(OnShareClickedListener listener) {

        myCallBack = listener;

    }

    @Override
    public void onClick(View v) {

        if (myCallBack != null) {
            switch (v.getId()) {
                case R.id.facebook_icon:
                    shareFacebook(myCallBack.onShareClicked());
                    Log.i("teste_salvar_imagem", "clicou no face");
                    dismiss();
                    break;
                case R.id.twitter_icon:
                    shareTwitter(myCallBack.onShareClicked());
                    dismiss();
                    break;
                case  R.id.copy_icon:
                    ClipBoardCatolicApp clipBoard = new ClipBoardCatolicApp(getActivity());
                    clipBoard.copiarTexto(myCallBack.onCopy());
                    dismiss();
                default:
                    break;
            }
        }
    }

    private void shareFacebook(Bitmap bitmap) {

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap).build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo).build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

    }

    private void shareTwitter(Bitmap bitmap) {

        String path = null;

        try {
            path = saveImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(path!=null){
            File myImageFile = new File(path + "/twitterSharedImage.jpg");
            Uri myImageUri = FileProvider.getUriForFile(getActivity(), "com.alexandrelunkes.catolicapp", myImageFile);
            Log.i("teste_twitter", "salvo em uri " + myImageUri.getPath());

            TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                    .image(myImageUri)
                    .text("Compartilhado pelo Aplicativo CatolicApp");

            builder.show();
        }

    }

    private String saveImage(Bitmap bitmap) throws IOException {


        if (Build.VERSION.SDK_INT >= 23) {

            int hasWriteImageExternalStoragePermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteImageExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                return null;
            } else {
                return executeSave(bitmap);
            }
        } else {
            return executeSave(bitmap);
        }

        // Log.i("teste_twitter", "salvo em " + diretorio.getAbsolutePath());

    }

    private String executeSave(Bitmap bitmap) throws IOException {
        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File arquivo = new File(diretorio, "twitterSharedImage.jpg");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(arquivo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return diretorio.getAbsolutePath();
    }

    public interface OnShareClickedListener {

        static final int FACEBOOK = 0;
        static final int INSTAGRAM = 1;
        static final int TWITTER = 2;

        Bitmap onShareClicked();
        String onCopy();
    }


}
