package com.chrysanthemum.ui.dataView.display;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * fixed column count board
 * create a new board for each task is recommended
 */
public class DisplayBoard {

    Context c;
    LinearLayout columnLayout;
    DataColumn[] columns;
    MutableLiveData<Integer> scrollSynchronizer;

    public DisplayBoard(Context c, LifecycleOwner o, LinearLayout board, int columnCount){
        columnLayout = board;
        columnLayout.removeAllViewsInLayout();
        this.c = c;
        scrollSynchronizer = new MutableLiveData<Integer>();
        scrollSynchronizer.observe(o, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                for(DataColumn d : columns){
                    d.getScroll().smoothScrollTo(0, integer);
                }
            }
        });
        createColumns(columnCount);
    }

    public void displayData(int col, int row, Displayable data){
        if(col < columns.length){
            columns[col].display(row, data);
        }
    }

    private void createColumns(int columnCount){
        columns = new DataColumn[columnCount];

        for(int i = 0; i < columnCount; i++){
            columns[i] = new DataColumn(c, columnLayout, new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    scrollSynchronizer.setValue(scrollY);
                }
            });
        }
    }

}
