package com.example.tesisfirebasefinal;

public class PuntosMapa {
    String fecha,latitud,longitud;

    public PuntosMapa() {
    }

    public PuntosMapa(String fecha, String latitud, String longitud) {
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
