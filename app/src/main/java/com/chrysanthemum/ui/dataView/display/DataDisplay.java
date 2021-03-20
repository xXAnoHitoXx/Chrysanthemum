package com.chrysanthemum.ui.dataView.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * a slot on  the board that could be used to display a displayable
 */
public class DataDisplay extends androidx.appcompat.widget.AppCompatTextView {

    private final Displayable data;

    public DataDisplay(Context context, Displayable data) {
        super(context);
        this.data = data;
        updateOnclick();
    }

    public DataDisplay(Context context, AttributeSet set){
        super(context, set);
        data = null;
    }

    public boolean hasData(){
        return data != null;
    }

    protected void onDraw(Canvas canvas) {
        if(hasData()){
            Drawable bg = data.getBGDrawable(getRekt());

            if(bg != null){
                bg.draw(canvas);
                bg.invalidateSelf();
            }

            this.setText(data.getDisplayData());
        } else {
            this.setText("");
        }
        super.onDraw(canvas);
    }

    private Rect getRekt(){
        return new Rect(0, 0, getWidth(), getHeight());
    }

    private void updateOnclick() {
        if(data != null) {
            setOnClickListener(data.getOnclickListener());
            setOnLongClickListener(data.getOnLongClickListener());
        }
    }
}
