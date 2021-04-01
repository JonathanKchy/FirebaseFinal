package com.example.tesisfirebasefinal.Fragments;

public class modelAdmin {
    String Apodo,Clave,Correo,Sincronismo,idPropio;

    public modelAdmin() {
    }

    public modelAdmin(String apodo, String clave, String correo, String sincronismo, String idPropio) {
        Apodo = apodo;
        Clave = clave;
        Correo = correo;
        Sincronismo = sincronismo;
        this.idPropio = idPropio;
    }

    public String getApodo() {
        return Apodo;
    }

    public void setApodo(String apodo) {
        Apodo = apodo;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getSincronismo() {
        return Sincronismo;
    }

    public void setSincronismo(String sincronismo) {
        Sincronismo = sincronismo;
    }

    public String getIdPropio() {
        return idPropio;
    }

    public void setIdPropio(String idPropio) {
        this.idPropio = idPropio;
    }
}
