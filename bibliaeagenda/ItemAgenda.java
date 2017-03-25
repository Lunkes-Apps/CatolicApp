package com.alexandrelunkes.catolicapp.bibliaeagenda;

import com.alexandrelunkes.catolicapp.itens.PassagemBiblica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 06/09/2016.
 */
public class ItemAgenda implements Serializable{

    private int id;
    private String data;
    private String primeira;
    private String salmo;
    private String segunda;
    private String evangelho;

    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String PRIMEIRA = "primeira";
    public static final String SALMO = "salmo";
    public static final String SEGUNDA = "segunda";
    public static final String EVANGELHO = "evangelho";

    public ItemAgenda(String data, String primeira, String salmo, String segunda, String evangelho) {
        this.data = data;
        this.primeira = primeira;
        this.salmo = salmo;
        this.segunda = segunda;
        this.evangelho = evangelho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrimeira() {
        return primeira;
    }

    public void setPrimeira(String primeira) {
        this.primeira = primeira;
    }

    public String getSalmo() {
        return salmo;
    }

    public void setSalmo(String salmo) {
        this.salmo = salmo;
    }

    public String getSegunda() {
        return segunda;
    }

    public void setSegunda(String segunda) {
        this.segunda = segunda;
    }

    public String getEvangelho() {
        return evangelho;
    }

    public void setEvangelho(String evangelho) {
        this.evangelho = evangelho;
    }

    public PassagemBiblica parsePrimeira(){

        String p = getPrimeira();
        String livro;
        String capitulo;
        StringBuilder versiculos = new StringBuilder();
        String[] words = p.split("ou");
        words = words[0].split(",");



        if(words.length == 4){
            livro = words[1]+ " "+ words[2];
            capitulo = words[3];
            for(int i = 4; i<words.length; i++){
                versiculos.append(words[i]);
            }
        }else {
            livro = words[1];
            capitulo = words[2];
            for(int i = 3; i<words.length; i++){
                versiculos.append(words[i]);
            }
        }

        List<Integer> versList = new ArrayList<>();
        int aux = 0;
        int inicio = 0;
        boolean cont = false;
        StringBuilder auxB = new StringBuilder();
        for(char c: versiculos.toString().toCharArray()){

              if(c == '-'){
                   if(cont){

                   }
                   versList.add(Integer.parseInt(auxB.toString()));
                   inicio = versList.get(versiculos.length()-1);
                   cont = true;
              }
              if(Character.isDigit(c)){
                   auxB.append(c);
              }
              if(c == '.'){

              }
        }

        return null;

    }



}
