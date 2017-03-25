package com.alexandrelunkes.catolicapp.itens;

/**
 * Created by Alexandre Lunkes on 09/09/2016.
 */
public class ExceptionLivro extends Exception {

    public ExceptionLivro(String livro) {
        super("Livro "+livro+" nao encontrado .... ..... ");
    }
}
