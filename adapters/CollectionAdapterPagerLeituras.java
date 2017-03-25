package com.alexandrelunkes.catolicapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 14/05/2016.
 */
public class CollectionAdapterPagerLeituras extends FragmentStatePagerAdapter implements Serializable{

   ArrayList<ItemAdapterPagerLeituras> itens;


    public CollectionAdapterPagerLeituras(FragmentManager fm, ArrayList<ItemAdapterPagerLeituras> itens) {
        super(fm);
        this.itens = itens;
    }

    @Override
    public Fragment getItem(int position) {
        return itens.get(position);
    }

    @Override
    public int getCount() {
        return itens.size();
    }



}
