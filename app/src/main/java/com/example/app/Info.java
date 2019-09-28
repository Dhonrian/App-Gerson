package com.example.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {

    private String placa;
    private String cor;
    private boolean roubado;



    private String local;

    public Info(String placa, String cor, boolean roubado) {
        this.placa = placa;
        this.cor = cor;
        this.roubado = roubado;
        this.local = local;
    }

    protected Info(Parcel in) {
        placa = in.readString();
        cor = in.readString();
        roubado = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placa);
        dest.writeString(cor);
        dest.writeByte((byte) (roubado ? 1 : 0));
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

    public void setRoubado(boolean roubado) {
        this.roubado = roubado;
    }
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

}
