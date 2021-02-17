package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.widget.LinearLayout;

import com.chrysanthemum.data.DataStorageModule;
import com.chrysanthemum.data.TechnicianIdentifier;

public class TechnicianSelectorPanel {

    private TechnicianSelectorButton selected;

    protected TechnicianSelectorPanel(Context context, LinearLayout layout){
        selected = null;
        Iterable<TechnicianIdentifier> it = DataStorageModule.getFrontEnd().getTechList();
        for(TechnicianIdentifier tech : it){

            final TechnicianSelectorButton button =
                    new TechnicianSelectorButton(context, tech, this);

            button.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            100));

            layout.addView(button);

        }
    }

    public TechnicianIdentifier getSelectedTech(){
        return selected.getTech();
    }

    void changeSelectedTech(TechnicianSelectorButton newTech){
        if(selected != null){
            selected.toggle();
        }

        selected = newTech;
        selected.toggle();
    }

}
