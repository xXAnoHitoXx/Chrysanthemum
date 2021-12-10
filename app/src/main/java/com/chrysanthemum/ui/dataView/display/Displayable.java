package com.chrysanthemum.ui.dataView.display;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;

import com.chrysanthemum.appdata.dataType.subType.Colour;

/**
 * implements this interface to be displayed by the board
 */
public abstract class Displayable implements Comparable<Displayable> {

    private final long id;

    public Displayable(){
        id = createID();
    }

    @Nullable
    public abstract Drawable getBGDrawable(Rect boundingBox);
    public abstract Colour getBGColour();
    public abstract String getDisplayData();
    public abstract View.OnClickListener getOnclickListener();
    public abstract View.OnLongClickListener getOnLongClickListener();
    public abstract int getX();
    public abstract int getY();
    public abstract int getWidth();
    public abstract int getHeight();

    @Override
    public int compareTo(Displayable o) {
        long comp = this.id - o.id;
        if(comp != 0){
            comp = comp / (Math.abs(comp));
        }
        return (int) comp;
    }

    private static synchronized long createID(){
        return System.currentTimeMillis();
    }
}
