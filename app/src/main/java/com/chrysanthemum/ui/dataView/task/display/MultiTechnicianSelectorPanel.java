package com.chrysanthemum.ui.dataView.task.display;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorButton;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

import java.util.Map;
import java.util.TreeMap;

public class MultiTechnicianSelectorPanel extends TechnicianSelectorPanel {

    private static final long SELECT_ALL = -1;

    private final Map<Long, Technician> selected = new TreeMap<>();

    public MultiTechnicianSelectorPanel(Context context, LinearLayout layout){
        super(context, layout, false);

        TechnicianSelectorButton salesButton =
                new TechnicianSelectorButton(context, DatabaseStructure.Accounting.SALE_TECH, this);
        TechnicianSelectorButton selectALLButton =
                new TechnicianSelectorButton(context, new Technician("", "", Color.BLACK, SELECT_ALL), this);

        salesButton.setLayoutParams(getParam(false));
        selectALLButton.setLayoutParams(getParam(false));

        layout.addView(salesButton);
        layout.addView(selectALLButton);
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

            selected.put(DatabaseStructure.Accounting.SALE_TECH.getID(), DatabaseStructure.Accounting.SALE_TECH);
        }

        return selected;
    }
}
