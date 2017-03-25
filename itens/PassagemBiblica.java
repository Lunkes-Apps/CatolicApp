package com.alexandrelunkes.catolicapp.itens;

import java.util.List;

/**
 * Created by Alexandre Lunkes on 08/09/2016.
 */
public class PassagemBiblica {

    private List<ItemLinha> linhas;
    private String passagem;
    private int testamento;
    private int livro;

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

    public List<ItemLinha> getLinhas() {
        return linhas;
    }

    public void setLinhas(List<ItemLinha> linhas) {
        this.linhas = linhas;
    }

    public String getPassagem() {
        return passagem;
    }

    public void setPassagem(String passagem) {
        this.passagem = passagem;
    }
}
