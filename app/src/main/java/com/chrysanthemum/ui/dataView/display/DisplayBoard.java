package com.chrysanthemum.ui.dataView.display;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;

import com.chrysanthemum.appdata.Util.Scaler;

import java.util.TreeMap;

import static com.chrysanthemum.appdata.Util.AppUtil.dpToPx;

/**
 * fixed column count board
 * create a new board for each task is recommended
 */
public class DisplayBoard {

    private Scaler scaler = new Scaler(1);

    private final Context c;
    private final ConstraintLayout layout;
    private TreeMap<Displayable, DataDisplay> subviews;

    public DisplayBoard(Context c, ConstraintLayout layout){
        this.c = c;
        this.layout = layout;
        subviews = new TreeMap<>();
    }

    public void displayData(Displayable d){
        int viewID = ViewCompat.generateViewId();
        int layoutID = layout.getId();

        DataDisplay display = new DataDisplay(c, d);
        display.setId(viewID);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(display, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        subviews.put(d, display);
        layout.addView(display);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.connect(viewID, ConstraintSet.LEFT, layoutID, ConstraintSet.LEFT, dpToPx(c, scaler.scaleValue(d.getX())));
        constraintSet.connect(viewID, ConstraintSet.TOP, layoutID, ConstraintSet.TOP, dpToPx(c, scaler.scaleValue(d.getY())));
        constraintSet.constrainWidth(viewID, dpToPx(c, scaler.scaleValue(d.getWidth())));
        constraintSet.constrainHeight(viewID, dpToPx(c, scaler.scaleValue(d.getHeight())));

        constraintSet.applyTo(layout);
        display.invalidate();
    }

    public void invalidateData(Displayable d){
        DataDisplay display = subviews.get(d);

        if(display != null){
            display.invalidate();
        }
    }

    public void remove(Displayable d){
        DataDisplay display = subviews.remove(d);
        layout.removeView(display);
        layout.invalidate();
    }

    public void setDisplayScale(Scaler scale){
        scaler = scale;
        layout.invalidate();
    }

    public void clear(Scaler scale){
        subviews = new TreeMap<>();
        layout.removeAllViews();
        setDisplayScale(scale);
    }
}
