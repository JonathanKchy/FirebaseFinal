package com.example.tesisfirebasefinal.Fragments;

public class model {
    String fecha,titulo,transcripcion;

    public model() {
    }

    public model(String fecha, String titulo, String transcripcion) {
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
