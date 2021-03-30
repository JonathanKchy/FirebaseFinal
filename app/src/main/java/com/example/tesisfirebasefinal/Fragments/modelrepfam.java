package com.example.tesisfirebasefinal.Fragments;

public class modelrepfam {
    String fecha,titulo,transcripcion;

    public modelrepfam() {
    }

    public modelrepfam(String fecha, String titulo, String transcripcion) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.transcripcion = transcripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTranscripcion() {
        return transcripcion;
    }

    public void setTranscripcion(String transcripcion) {
        this.transcripcion = transcripcion;
    }
}
