package com.alexandrelunkes.catolicapp;

/**
 * Created by Alexandre Lunkes on 17/05/2016.
 */
public class PassagemTexto {

        private String titulo;
        private String texto;

        public PassagemTexto(String titulo, String texto) {
            this.titulo = titulo;
            this.texto = texto;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }
 }


