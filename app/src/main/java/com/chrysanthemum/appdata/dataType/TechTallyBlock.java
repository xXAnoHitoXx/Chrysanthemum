package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.ui.dataView.task.accounting.Cal.Amount;

import java.util.Set;
import java.util.TreeMap;

public class TechTallyBlock {
     private final Amount total = new Amount();
     private final int techColour;
     private final TreeMap<Integer, Amount> data = new TreeMap<>();

    public TechTallyBlock(int techColour) {
        this.techColour = techColour;
    }

    public void add(Integer index, Amount a){
        data.put(index, a);
        total.add(a);
     }

     public Set<Integer> Keys(){
         return data.keySet();
     }

     public Amount get(int index){
         return data.get(index);
     }

     public Amount getTotal(){
         return total;
     }

     public int getTechColour(){
        return techColour;
     }

}
