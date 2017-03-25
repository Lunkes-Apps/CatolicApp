package com.alexandrelunkes.catolicapp.Interfaces;

import android.view.View;

/**
 * Created by Alexandre Lunkes on 25/05/2016.
 */
public interface OnButtonClickedInPager {
        public void onButtonClicked(View view);
        public void onLongButtonClicked(View view);
        public void onItemSelectedToShare(int position);

}
