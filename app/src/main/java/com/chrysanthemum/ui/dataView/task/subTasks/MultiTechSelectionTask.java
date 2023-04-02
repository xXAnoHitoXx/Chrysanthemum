package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.widget.Button;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.display.MultiTechnicianSelectorPanel;

import java.util.LinkedList;

public class MultiTechSelectionTask extends Task {

    private final DataRetriever<LinkedList<Technician>> retriever;
    private MultiTechnicianSelectorPanel panel;

    public MultiTechSelectionTask(TaskHostestActivity host, DataRetriever<LinkedList<Technician>> retriever) {
        super(host);

        this.retriever = retriever;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        host.clearForm();

        panel = host.createMultiTechPanel();

        Button button = host.getFormButton();
        button.setText("Select");

        button.setOnClickListener(v -> {

            LinkedList<Technician> data = new LinkedList<>(panel.getSelectedTechs().values());

            if(!data.isEmpty()){
                retriever.retrievedData(data);
            }
        });
    }
}
