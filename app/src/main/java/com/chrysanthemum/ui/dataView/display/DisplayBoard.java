package com.chrysanthemum.ui.dataView.display;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import java.util.TreeMap;

import static com.chrysanthemum.appdata.Util.AppUtil.dpToPx;

/**
 * fixed column count board
 * create a new board for each task is recommended
 */
public class DisplayBoard {

    Context c;
    ConstraintLayout layout;
    TreeMap<Displayable, DataDisplay> subviews;

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

        subviews.put(d, display);
        layout.addView(display);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.connect(viewID, ConstraintSet.LEFT, layoutID, ConstraintSet.LEFT, dpToPx(c, d.getX()));
        constraintSet.connect(viewID, ConstraintSet.TOP, layoutID, ConstraintSet.TOP, dpToPx(c, d.getY()));
        constraintSet.constrainWidth(viewID, dpToPx(c, d.getWidth()));
        constraintSet.constrainHeight(viewID, dpToPx(c, d.getHeight()));

        constraintSet.applyTo(layout);
        display.invalidate();
    }

    public void remove(Displayable d){
        DataDisplay display = subviews.remove(d);
        layout.removeView(display);
        layout.invalidate();
    }

    public void clear(){
        subviews = new TreeMap<>();
        layout.removeAllViews();
    }
}
