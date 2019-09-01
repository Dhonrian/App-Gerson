package com.example.app;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacaInfo implements Parcelable {

    private String placa;
    private String cor;
    private boolean roubado;

    public PlacaInfo(String placa, String cor, boolean roubado) {
        this.placa = placa;
        this.cor = cor;
        this.roubado = roubado;
    }

    protected PlacaInfo(Parcel in) {
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

    public static final Creator<PlacaInfo> CREATOR = new Creator<PlacaInfo>() {
        @Override
        public PlacaInfo createFromParcel(Parcel in) {
            return new PlacaInfo(in);
        }

        @Override
        public PlacaInfo[] newArray(int size) {
            return new PlacaInfo[size];
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
}
