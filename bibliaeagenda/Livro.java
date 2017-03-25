package com.alexandrelunkes.catolicapp.bibliaeagenda;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 06/09/2016.
 */
public class Livro {

    private String nome;
    private List<List<String>> capitulos;
    private ArrayList<String[]> livro;

    public Livro(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<List<String>> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<List<String>> capitulos) {
        this.capitulos = capitulos;
    }

    public ArrayList<String[]> getLivro(){

         livro = new ArrayList<>();
         List<String> aux = new ArrayList<>();

         for(List<String> a: capitulos){
              for(String b: a){
                  aux.add(b);
              }
             livro.add(aux.toArray(new String[aux.size()]));
             aux = new ArrayList<>();
         }


        return livro;
    }


}
