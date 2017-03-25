package com.alexandrelunkes.catolicapp.itens;

/**
 * Created by Alexandre Lunkes on 11/09/2016.
 */
public class ItemLinha {

    int testamento;
    int livro;
    int capitulo;
    int versiculo;

    boolean ateOFim;

    public ItemLinha(int testamento, int livro, int capitulo, int versiculo) {
        this.testamento = testamento;
        this.livro = livro;
        this.capitulo = capitulo;
        this.versiculo = versiculo;
        ateOFim = false;
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

    public boolean isAteOFim() {
        return ateOFim;
    }

    public void setAteOFim(boolean ateOFim) {
        this.ateOFim = ateOFim;
    }
}
