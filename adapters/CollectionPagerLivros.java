package com.alexandrelunkes.catolicapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 25/05/2016.
 */
public class CollectionPagerLivros extends FragmentStatePagerAdapter{

    ArrayList<ItemAdapterPagerLivro> itens = new ArrayList<>();


    public CollectionPagerLivros(FragmentManager fm, ArrayList<ItemAdapterPagerLivro> itens) {
        super(fm);
        this.itens = itens;
    }

    @Override
    public Fragment getItem(int position){
        return itens.get(position);
    }

    @Override
    public int getCount() {
        return itens.size();
    }
}
