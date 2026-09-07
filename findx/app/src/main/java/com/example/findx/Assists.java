package com.example.findx;

public class Assists {

    String categoria, endereco, horario, latitude,
           longitude, nome, site, telefone;

    public Assists(String categoria, String endereco, String horario, String latitude, String longitude, String nome, String site, String telefone) {
        this.categoria = categoria;
        this.endereco = endereco;
        this.horario = horario;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
        this.site = site;
        this.telefone = telefone;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getHorario() {
        return horario;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getNome() {
        return nome;
    }

    public String getSite() {
        return site;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
