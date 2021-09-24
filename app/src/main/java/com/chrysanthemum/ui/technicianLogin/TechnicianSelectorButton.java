package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

import com.chrysanthemum.R;
import com.chrysanthemum.appdata.dataType.Technician;

import androidx.core.content.res.ResourcesCompat;

public class TechnicianSelectorButton extends View implements View.OnClickListener {

    private static final int OFFSET_PERCENT = 7;

    private boolean selected;
    private Technician tech;
    private Resources res;
    private TechnicianSelectorPanel panel;

    public TechnicianSelectorButton(Context context){
        super(context);
        initButton(context);
    }

    public TechnicianSelectorButton(Context context, AttributeSet set){
        super(context, set);
        initButton(context);
    }

    public TechnicianSelectorButton(Context context, Technician tech, TechnicianSelectorPanel panel){
        super(context);
        initButton(context);
        setTech(tech);
        this.panel = panel;
        this.setOnClickListener(this);
    }

    private void initButton(Context context){
        selected = false;
        res = context.getResources();
    }

    public void toggle(){
        selected = !selected;
        this.invalidate();
    }

    public void setTech(Technician tech){
        this.tech = tech;
    }

    public Technician getTech(){
        return tech;
    }

    protected void onDraw(Canvas canvas) {
        Drawable button = getButtonDrawable();
        button.draw(canvas);
        button.invalidateSelf();
    }

    private Drawable getButtonDrawable(){
        LayerDrawable buttonDrawable = new LayerDrawable(new Drawable[]{
                getButtonBackgroundDrawable()
        });

        if(selected){
            Drawable drawable = getCheckMarkDrawable();

            if(drawable != null){
                buttonDrawable.addLayer(getCheckMarkDrawable());
            }
        }

        return buttonDrawable;
    }

    private Drawable getButtonBackgroundDrawable() {
        ShapeDrawable drawable =  new ShapeDrawable(new RectShape());
        drawable.getPaint().setColor(tech.getColour());
        drawable.setBounds(getBoundingBox());

        return drawable;
    }


    private Drawable getCheckMarkDrawable() {
        Drawable checkMark = ResourcesCompat.getDrawable(res, R.drawable.checkmark, null);

        if(checkMark == null){
            return null;
        }

        checkMark.setBounds(this.getWidth() - checkMark.getIntrinsicWidth(),
                this.getHeight() - checkMark.getIntrinsicHeight(),
                this.getWidth(), this.getHeight());

        return checkMark;
    }

    private Rect getBoundingBox(){
        return (selected)? getOffsetBox() : getFullBox();
    }

    private Rect getFullBox(){
        return new Rect(0, 0, getWidth(), getHeight());
    }

    private Rect getOffsetBox(){
        int xMargin = (getWidth() * OFFSET_PERCENT) / 100;
        int yMargin = (getHeight() * OFFSET_PERCENT) / 100;

        return new Rect(xMargin, yMargin,
                getWidth() - xMargin, getHeight() - yMargin);
    }

    @Override
    public void onClick(View v) {
        panel.changeSelectedTech(this);
    }
}
