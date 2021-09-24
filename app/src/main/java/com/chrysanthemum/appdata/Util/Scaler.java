package com.chrysanthemum.appdata.Util;

public class Scaler {
    private final float scaler;

    public Scaler(float scaler){
        this.scaler = scaler;
    }

    public int scaleValue(int x){
        return (int)(x * scaler);
    }

    public Scaler scale(float f){
        return new Scaler(scaler * f);
    }
}
