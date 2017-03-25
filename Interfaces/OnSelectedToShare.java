package com.alexandrelunkes.catolicapp.Interfaces;

import java.util.List;

/**
 * Created by Alexandre Lunkes on 25/05/2016.
 */
public interface OnSelectedToShare {
    boolean isSharedMode();
    void setIsShared(boolean isShared);
    void cancelShare();
    void onSelectItemToShare(int position);
    void onGetVersiculosToShare(String[] versiculos, List<Integer> itens);
    boolean clearItens();
}
