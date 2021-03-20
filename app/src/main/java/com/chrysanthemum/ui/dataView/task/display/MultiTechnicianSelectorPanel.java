package com.chrysanthemum.ui.dataView.task.display;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorButton;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

import java.util.Map;
import java.util.TreeMap;

public class MultiTechnicianSelectorPanel extends TechnicianSelectorPanel {

    private static final long SELECT_ALL = -1;

    private Map<Long, Technician> selected = new TreeMap<>();

    private boolean locked = false;

    public MultiTechnicianSelectorPanel(Context context, LinearLayout layout){
        super(context, layout, false);

        TechnicianSelectorButton button =
                new TechnicianSelectorButton(context, new Technician("", "", Color.BLACK, SELECT_ALL), this);

        button.setLayoutParams(getParam(false));

        layout.addView(button);
    }

    public void changeSelectedTech(TechnicianSelectorButton newTech){
        if(!locked){
            newTech.toggle();

            if(selected.containsKey(newTech.getTech().getID())){
                selected.remove(newTech.getTech().getID());
            } else {
                selected.put(newTech.getTech().getID(), newTech.getTech());
            }
        }
    }

    public Map<Long, Technician> getSelectedTechs(){

        if(selected.containsKey(SELECT_ALL)){
            selected.remove(SELECT_ALL);

            for(Technician tech : DataStorageModule.getFrontEnd().getTechList()){
                selected.put(tech.getID(), tech);
            }
        }

        return selected;
    }
}
