package com.alexandrelunkes.catolicapp.paginas;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alexandrelunkes.catolicapp.Interfaces.OnAdReturnCommand;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.itens.AdControler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class RosarioActivity extends AppCompatActivity implements RosarioFragment.OnOracaoRosarioSelectedListener,
        OnAdReturnCommand{

    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rosario);

        setAppBar();

        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("0815A78C9B93FCF6A6888964A047C6E1")
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdControler(this,this));


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rosario_frame,new RosarioFragment());
        ft.commit();
    }

    @Override
    public void onOpenAd() {
        adView.destroy();
        adView.setVisibility(View.GONE);
    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void OnSelectOracaoRosario(String item, int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (position){
            case 0:
                ft.replace(R.id.rosario_frame,new ComoRezarRosarioFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 1:
                Intent i = new Intent(this, MisteriosContemplacaoActivity.class);
                startActivity(i);
            default: break;

        }



    }
}
