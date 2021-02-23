package com.chrysanthemum.ui.dataView.display;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * implements this interface to be displayed by the board
 */
public interface Displayable {
    @Nullable
    Drawable getBGDrawable(Rect boundingBox);
    String getDisplayData();
    View.OnClickListener getOnclickListener();
    View.OnLongClickListener getOnLongClickListener();
}
