package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.widget.LinearLayout;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;

public class TechnicianSelectorPanel {

    private TechnicianSelectorButton selected;
    private boolean locked = false;

    public TechnicianSelectorPanel(Context context, LinearLayout layout, boolean vertical){
        selected = null;
        Iterable<Technician> it = DataStorageModule.getFrontEnd().getTechList();
        for(Technician tech : it){

            final TechnicianSelectorButton button =
                    new TechnicianSelectorButton(context, tech, this);

            button.setLayoutParams(getParam(vertical));

            layout.addView(button);

        }
    }

    private LinearLayout.LayoutParams getParam(boolean vertical){
        if(vertical){
            return new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    100);
        } else {
            return new LinearLayout.LayoutParams(
                    100,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    public Technician getSelectedTech(){
        return selected.getTech();
    }

    void changeSelectedTech(TechnicianSelectorButton newTech){
        if(!locked){
            if(selected != null){
                selected.toggle();
            }

            selected = newTech;
            selected.toggle();
        }
    }

    void lock(){
        locked = true;
    }

    void unlock(){
        locked = false;
    }
}
