package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.widget.LinearLayout;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;

public class TechnicianSelectorPanel {

    private TechnicianSelectorButton selected;
    private boolean locked = false;

    protected TechnicianSelectorPanel(Context context, LinearLayout layout){
        selected = null;
        Iterable<Technician> it = DataStorageModule.getFrontEnd().getTechList();
        for(Technician tech : it){

            final TechnicianSelectorButton button =
                    new TechnicianSelectorButton(context, tech, this);

            button.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            100));

            layout.addView(button);

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
