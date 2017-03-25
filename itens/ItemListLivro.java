package com.alexandrelunkes.catolicapp.itens;

/**
 * Created by Alexandre Lunkes on 25/05/2016.
 */
public class ItemListLivro {

    private String texto;
    private boolean selecionado;

    public ItemListLivro(String texto, boolean selecionado) {
        this.texto = texto;
        this.selecionado = selecionado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}
