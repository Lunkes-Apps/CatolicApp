package com.alexandrelunkes.catolicapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Alexandre Lunkes on 16/06/2016.
 */
public class CollectionPagerMisterios extends FragmentStatePagerAdapter {


    ArrayList<ItemAdapterMisterios> itens = new ArrayList<>();

    public CollectionPagerMisterios(FragmentManager fm, ArrayList<ItemAdapterMisterios>itens) {
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
