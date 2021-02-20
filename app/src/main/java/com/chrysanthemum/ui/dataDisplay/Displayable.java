package com.chrysanthemum.ui.dataDisplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;

public class Displayable extends androidx.appcompat.widget.AppCompatTextView {

    private Transaction transaction;

    private Drawable bg;

    public Displayable(Context context) {
        super(context);
        transaction = new Transaction();
    }


    public Displayable(Context context, AttributeSet set){
        super(context, set);
        transaction = new Transaction();
    }

    public Displayable(Context context, Transaction transaction) {
        super(context);

        this.transaction = transaction;
    }

    protected void onDraw(Canvas canvas) {
        Drawable bg = getBGDrawable();

        bg.draw(canvas);
        bg.invalidateSelf();

        this.setText(transaction.getDisplayData());

        super.onDraw(canvas);
    }

    private Drawable getBGDrawable(){

        switch (transaction.getStatus()){
            //TODO
        }

        return null;
    }
}
