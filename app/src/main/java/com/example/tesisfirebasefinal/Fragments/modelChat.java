package com.example.tesisfirebasefinal.Fragments;

public class modelChat {
    String estado,texto;

    public modelChat() {
    }

    public modelChat(String estado, String texto) {
        this.estado = estado;
        this.texto = texto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
