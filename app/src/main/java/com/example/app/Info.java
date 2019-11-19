package com.example.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {

    private String placa;
    private String cor;
    private boolean roubado;
    private String categoria;
    private String nome_possuidor;
    private String doc_possuidor;
    private String UF_possuidor;
    private double lat;
    private double lon;

    public Info(String placa, String cor, boolean roubado, String categoria, String nome_possuidor, String doc_possuidor, String UF_possuidor) {
        this.placa = placa;
        this.cor = cor;
        this.roubado = roubado;
        this.categoria = categoria;
        this.nome_possuidor = nome_possuidor;
        this.doc_possuidor = doc_possuidor;
        this.UF_possuidor = UF_possuidor;
    }

    protected Info(Parcel in) {
        placa = in.readString();
        cor = in.readString();
        roubado = in.readByte() != 0;
        categoria = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placa);
        dest.writeString(cor);
        dest.writeString(categoria);
        dest.writeString(nome_possuidor);
        dest.writeString(doc_possuidor);
        dest.writeString(UF_possuidor);
        dest.writeByte((byte) (roubado ? 1 : 0));
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public boolean isRoubado() {
        return roubado;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCategoria() {return categoria;}

    public void setCategoria(String categoria){ this.categoria = categoria;}

    public void setRoubado(boolean roubado) {
        this.roubado = roubado;
    }

    public String getNome_possuidor() {
        return nome_possuidor;
    }

    public void setNome_possuidor(String nome_possuidor) {
        this.nome_possuidor = nome_possuidor;
    }

    public String getDoc_possuidor() {
        return doc_possuidor;
    }

    public void setDoc_possuidor(String doc_possuidor) {
        this.doc_possuidor = doc_possuidor;
    }

    public String getUF_possuidor() {
        return UF_possuidor;
    }

    public void setUF_possuidor(String UF_possuidor) {
        this.UF_possuidor = UF_possuidor;
    }
}
