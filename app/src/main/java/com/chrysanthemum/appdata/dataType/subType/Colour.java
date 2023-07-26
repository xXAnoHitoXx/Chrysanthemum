package com.chrysanthemum.appdata.dataType.subType;

public class Colour {
    final int a;
    final int r;
    final int g;
    final int b;

    public Colour(int c){

        if(c == 0xffa20c57){
            c++;
            c--;
        }

        b = c & 0xff;
        c = c >> 8;
        g = c & 0xff;
        c = c >> 8;
        r = c & 0xff;
        c = c >> 8;
        a = c & 0xff;
    }

    public boolean isDark(){
        int luminance = (299 * r + 587 * g + 114 * b) / 1000;
        return luminance < 110;
    }
}
