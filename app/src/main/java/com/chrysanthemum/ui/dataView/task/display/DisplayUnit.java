package com.chrysanthemum.ui.dataView.task.display;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import androidx.annotation.Nullable;

import com.chrysanthemum.ui.dataView.display.Displayable;

public class DisplayUnit extends Displayable {
    private static final int BLOCK_HEIGHT = 20;
    private static final int BLOCK_WIDTH = 140;
    private  static final int MARGIN = 2;

    private int colour;
    private String data;
    private Rect bounds;
    private View.OnLongClickListener listener = null;

    public DisplayUnit(String data, int colour, int col, int row, int width){
        this.colour = colour;
        this.data = data;
        this.bounds = allocateSpace(col, row, width);
    }

    public void setOnLongCLickListener(View.OnLongClickListener listener){
        this.listener = listener;
    }

    private Rect allocateSpace(int col, int row, int width){
        int left = col * (BLOCK_WIDTH + MARGIN);
        int right = left + BLOCK_WIDTH + (width - 1) * (BLOCK_WIDTH + MARGIN);
        int top = row * (BLOCK_HEIGHT + MARGIN);
        int bot = top + BLOCK_HEIGHT;

        return new Rect(left, top, right, bot);
    }

    @Nullable
    @Override
    public Drawable getBGDrawable(Rect boundingBox) {
        ShapeDrawable bgDrawable =  new ShapeDrawable(new RectShape());

        bgDrawable.getPaint().setColor(colour);
        bgDrawable.setBounds(boundingBox);

        return bgDrawable;
    }

    @Override
    public String getDisplayData() {
        return data;
    }

    @Override
    public View.OnClickListener getOnclickListener() {
        return null;
    }

    @Override
    public View.OnLongClickListener getOnLongClickListener() {
        return listener;
    }

    @Override
    public int getX() {
        return bounds.left;
    }

    @Override
    public int getY() {
        return bounds.top;
    }

    @Override
    public int getWidth() {
        return bounds.width();
    }

    @Override
    public int getHeight() {
        return bounds.height();
    }
}
