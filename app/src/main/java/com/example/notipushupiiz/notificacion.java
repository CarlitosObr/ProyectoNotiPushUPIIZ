package com.example.notipushupiiz;

public class notificacion {
    private String idUsuario;
    private String titulo;
    private String descripcion;

    public notificacion(){

    }

    public notificacion(String idUsuario, String titulo, String descripcion) {
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
