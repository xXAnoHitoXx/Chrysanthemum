package com.chrysanthemum.ui.dataView.task;

import com.chrysanthemum.ui.dataView.display.DisplayBoard;

public class CustomerFinderTask extends Task {

    private DisplayBoard board;

    public CustomerFinderTask(TaskHostestActivity host) {
        super(host);
        board = host.createBoard(4);

    }

    private void setupFindMode(){

    }
}
