package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

public class TechnicianSelectorButton extends View {

    private boolean selected;
    private int colour;

    public TechnicianSelectorButton(Context context, int colour){
        super(context);
        this.setMinimumHeight(100);
        selected = false;
        this.colour = colour;
    }

    public void toggle(){
        selected = !selected;
    }

    protected void onDraw(Canvas canvas) {
        ShapeDrawable drawable = new ShapeDrawable();

        if(isSelected()){
            drawable.setBounds(this.getClipBounds());
        } else {
            drawable.setBounds(15, 6, 220, 88);
        }

        drawable.getPaint().setColor(colour);
    }
}
