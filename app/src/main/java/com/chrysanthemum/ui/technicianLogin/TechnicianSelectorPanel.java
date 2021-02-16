package com.chrysanthemum.ui.technicianLogin;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chrysanthemum.data.DataStorageModule;
import com.chrysanthemum.data.TechnicianIdentifier;

public class TechnicianSelectorPanel {

    private TechnicianSelectorButton selected;

    protected TechnicianSelectorPanel(Context context, LinearLayout layout){
        selected = null;

        for(TechnicianIdentifier tech :
                DataStorageModule.getFrontEnd().getTechList()){

            final TechnicianSelectorButton button =
                    new TechnicianSelectorButton(context, tech);
            /*
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSelectedTech(button);
                }
            });
            */

            button.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

            button.setMinimumHeight(100);

            layout.addView(button);
        }
    }

    /*
    public TechnicianIdentifier getSelectedTech(){
        return selected.getTech();
    }

    private void changeSelectedTech(TechnicianSelectorButton newTech){
        if(selected != null){
            selected.toggle();
        }

        selected = newTech;
        selected.toggle();
    }

    */
}
