package com.alexandrelunkes.catolicapp.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 28/04/2016.
 */
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ItemCollectionPagerTestamento> itens;
    private String[] titulos;


    public CollectionPagerAdapter(FragmentManager fm, ArrayList<ItemCollectionPagerTestamento> itens){
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


    @Override
    public CharSequence getPageTitle(int position) {
        return titulos[position];
    }
}
