package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;

import com.chrysanthemum.R;
import com.chrysanthemum.data.TechnicianIdentifier;

import androidx.core.content.res.ResourcesCompat;

public class TechnicianSelectorButton extends androidx.appcompat.widget.AppCompatButton {

    private boolean selected;
    private TechnicianIdentifier tech;
    private Resources res;

    public TechnicianSelectorButton(Context context){
        super(context);
        initButton(context);
    }

    public TechnicianSelectorButton(Context context, TechnicianIdentifier tech){
        super(context);
        initButton(context);
        setTech(tech);
    }

    public TechnicianSelectorButton(Context context, AttributeSet set){
        super(context, set);
        initButton(context);
    }

    private void initButton(Context context){
        selected = false;
        this.setMinimumHeight(100);
        res = context.getResources();
    }

    public void toggle(){
        selected = !selected;
    }

    public void setTech(TechnicianIdentifier tech){
        this.tech = tech;
    }

    public TechnicianIdentifier getTech(){
        return tech;
    }

    protected void onDraw(Canvas canvas) {
        this.getButtonBackgroundDrawable().draw(canvas);

        /*
        if(selected){
            getCheckmarkDrawable().draw(canvas);
        }
         */
    }

    private Drawable getButtonBackgroundDrawable() {
        ShapeDrawable drawable =  new ShapeDrawable(new RectShape());

        drawable.getPaint().setColor(tech.getColour());

        if(selected){
            drawable.setBounds(this.getClipBounds());
        } else {
            int xmargin = 15;
            int ymargin = 6;

            drawable.setBounds(xmargin, ymargin,
                    this.getWidth() - xmargin,
                    this.getHeight() - ymargin);
        }

        return drawable;
    }

    private Drawable getCheckmarkDrawable() {
        Drawable checkmark = ResourcesCompat.getDrawable(res, R.drawable.checkmark, null);

        checkmark.setBounds(this.getWidth() - checkmark.getIntrinsicWidth(),
                this.getHeight() - checkmark.getIntrinsicHeight(),
                this.getWidth(), this.getHeight());

        return checkmark;
    }
}
