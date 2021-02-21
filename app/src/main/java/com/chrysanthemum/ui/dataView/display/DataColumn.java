package com.chrysanthemum.ui.dataView.display;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.Map;
import java.util.TreeMap;

/**
 * a collumn of slots on the board
 */
public class DataColumn {
    private final int DATA_WIDTH = 200;
    private final int DATA_HEIGHT = 100;

    private final Context c;
    private final LinearLayout col;
    private final ScrollView scroll;

    private int capacity;
    private final Map<Integer, DataDisplay> dataDisplays;

    public DataColumn(Context c, LinearLayout columns, View.OnScrollChangeListener synchronizer){
        this.c = c;
        scroll = new ScrollView(c);
        scroll.setOnScrollChangeListener(synchronizer);
        scroll.setLayoutParams(getColumnParam());

        col = new LinearLayout(c);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.START);

        col.setLayoutParams(getColumnParam());

        scroll.addView(col);
        columns.addView(scroll);

        capacity = 0;
        dataDisplays = new TreeMap<>();
    }

    public void display(int index, Displayable displayable){

        if(displayable != null) {
            DataDisplay display = getDisplayable(index);
            display.setData(displayable);
            display.invalidate();
        } else if(index < capacity){
            DataDisplay display = getDisplayable(index);
            display.setData(null);

            // if we remove the last slot containing actual data,
            // remove it and any paddings if exists
            if(index == capacity - 1){
                for(int i = capacity-1; i >= 0; i--){
                    if(!dataDisplays.get(i).hasData()){
                        col.removeView(dataDisplays.remove(i));
                        capacity--;
                    } else {
                        break;
                    }
                }
            }

            display.invalidate();
        }
    }

    public ScrollView getScroll(){
        return scroll;
    }

    private DataDisplay getDisplayable(int index){
        for (int i = capacity; i <= index; i++){
            addDataDisplay(new DataDisplay(c));
        }

        return dataDisplays.get(index);
    }

    private void addDataDisplay(DataDisplay data){
        dataDisplays.put(capacity++, data);
        data.setLayoutParams(getDataParam());
        col.addView(data);
    }


    private LinearLayout.LayoutParams getDataParam(){
        return new LinearLayout.LayoutParams(dpToPx(DATA_WIDTH), dpToPx(DATA_HEIGHT));
    }

    private LinearLayout.LayoutParams getColumnParam(){
        return new LinearLayout.LayoutParams(dpToPx(DATA_WIDTH), LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private int dpToPx(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }
}
