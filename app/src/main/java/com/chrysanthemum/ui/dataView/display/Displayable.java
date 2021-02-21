package com.chrysanthemum.ui.dataView.display;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * implements this interface to be displayed by the board
 */
public interface Displayable {
    @Nullable
    Drawable getBGDrawable();
    String getDisplayData();
    View.OnClickListener getOnclickListener();
}
