package com.alexandrelunkes.catolicapp.favoritos;

import com.alexandrelunkes.catolicapp.PassagemTexto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alexandre Lunkes on 21/08/2016.
 */
public class Favorito implements Serializable{

    public static final String PASSAGEM = "PASSAGEM";
    public static final String _ID = "_id";
    public static final String DATA = "data";
    public static final String ANOTACAO = "anotacao";

    private int id;
    private String passagem;
    private String anotacao;
    private String data;
    private Boolean hasAnota = false;
    private float alfa;
    private boolean isSelected = false;




    public Boolean getHasAnota() {
        return hasAnota;
    }

    public void setHasAnota(Boolean hasAnota) {
        this.hasAnota = hasAnota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassagem() {
        return passagem;
    }

    public void setPassagem(String passagem) {
        this.passagem = passagem;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {

        if(anotacao == null){
            setHasAnota(false);
        }else {
            setHasAnota(true);
        }

        this.anotacao = anotacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getAlfa(){
        if(getHasAnota()){
            return 1F;
        }else {
            return 0.3F;
        }
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }

    public boolean getIsSelected(){
        return this.isSelected;
    }
}
