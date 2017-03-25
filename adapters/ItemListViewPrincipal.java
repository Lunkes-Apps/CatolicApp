package com.alexandrelunkes.catolicapp.adapters;

/**
 * Created by Alexandre Lunkes on 28/04/2016.
 */
public class ItemListViewPrincipal {

    private int idImage;
    private String titulo;
    private String subTitulo;

    public ItemListViewPrincipal(int idImage, String titulo, String subTitulo) {
        this.idImage = idImage;
        this.titulo = titulo;
        this.subTitulo = subTitulo;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }
}
