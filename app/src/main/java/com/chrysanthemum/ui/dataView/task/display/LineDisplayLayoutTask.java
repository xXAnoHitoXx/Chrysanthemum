package com.chrysanthemum.ui.dataView.task.display;

import android.view.View;

import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;

public abstract class LineDisplayLayoutTask extends Task {

    public LineDisplayLayoutTask(TaskHostestActivity host) {
        super(host);
    }


    protected void displayLine(String[] data, int colour, int row, View.OnLongClickListener listener){
        int col = 0;

        for(; col < (data.length - 1); col++){

            DisplayUnit ad = new DisplayUnit(data[col], colour, col, row, 1);
            ad.setOnLongCLickListener(listener);

            host.getBoard().displayData(ad);
        }

        host.getBoard().displayData(new DisplayUnit(data[col], colour, col, row, 2));
    }


    protected void displayLine(String[] data, int[] colour, int row){
        int col = 0;

        for(; col < (data.length - 1); col++){
            host.getBoard().displayData(new DisplayUnit(data[col], colour[col], col, row, 1));
        }

        host.getBoard().displayData(new DisplayUnit(data[col], colour[col], col, row, 2));
    }
}
