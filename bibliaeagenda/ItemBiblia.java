package com.alexandrelunkes.catolicapp.bibliaeagenda;

/**
 * Created by Alexandre Lunkes on 06/09/2016.
 */
public class ItemBiblia {

    private int id;
    private int testamento;
    private int livro;
    private int capitulo;
    private int versiculo;
    private String texto;

    public static final String ID = "id";
    public static final String TESTAMENTO = "testamento";
    public static final String LIVRO = "livro";
    public static final String CAPITULO = "capitulo";
    public static final String VERSICULO = "versiculo";
    public static final String TEXTO = "texto";



    public ItemBiblia(int testamento, int livro, int capitulo, int versiculo, String texto) {
        this.testamento = testamento;
        this.livro = livro;
        this.capitulo = capitulo;
        this.versiculo = versiculo;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestamento() {
        return testamento;
    }

    public void setTestamento(int testamento) {
        this.testamento = testamento;
    }

    public int getLivro() {
        return livro;
    }

    public void setLivro(int livro) {
        this.livro = livro;
    }

    public int getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(int capitulo) {
        this.capitulo = capitulo;
    }

    public int getVersiculo() {
        return versiculo;
    }

    public void setVersiculo(int versiculo) {
        this.versiculo = versiculo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }



}
