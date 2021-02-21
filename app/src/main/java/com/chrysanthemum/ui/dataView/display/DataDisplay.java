package com.chrysanthemum.ui.dataView.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * a slot on  the board that could be used to display a displayable
 */
public class DataDisplay extends androidx.appcompat.widget.AppCompatTextView {

    private Displayable data;

    public DataDisplay(Context context) {
        super(context);
        data = null;
    }

    public DataDisplay(Context context, AttributeSet set){
        super(context, set);
        data = null;
    }

    public void setData(Displayable data){
        this.data = data;
        updateOnclick();
    }

    public boolean hasData(){
        return data != null;
    }

    protected void onDraw(Canvas canvas) {
        if(hasData()){
            Drawable bg = data.getBGDrawable();

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

    private void updateOnclick(){
        if(data == null){
            setOnClickListener(null);
        } else {
            setOnClickListener(data.getOnclickListener());
        }
    }
}
