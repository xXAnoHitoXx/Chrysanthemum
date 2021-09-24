package com.chrysanthemum.appdata.Util;

public class DynamicLCSTable {

    private final String a;
    private final String b;
    private final int[][] table;


    public DynamicLCSTable(String a, String b) {
        this.a = a;
        this.b = b;
        table = new int[a.length()][b.length()];

        for(int i = 0; i < a.length(); i++){
            for(int j = 0; j < b.length(); j++){
                if(a.charAt(i) == b.charAt(j)){
                    table[i][j] = 1 + retrieveFromDynamicTable(i - 1, j - 1);
                } else {
                    table[i][j] = Math.max(retrieveFromDynamicTable(i - 1, j),
                            retrieveFromDynamicTable(i, j - 1));
                }
            }
        }
    }

    public String getLCS(){
        StringBuilder builder = new StringBuilder();

        int i = a.length() - 1;
        int j = b.length() - 1;

        while (retrieveFromDynamicTable(i, j) > 0){
            if( retrieveFromDynamicTable(i - 1, j) == retrieveFromDynamicTable(i , j)){
                i--;
            } else if(retrieveFromDynamicTable(i, j - 1) == retrieveFromDynamicTable(i, j)) {
                j--;
            } else {
                builder.insert(0, a.charAt(i));
                i--;
                j--;
            }
        }

        return builder.toString();
    }

    public String getShortestCommonSuperString(){
        StringBuilder builder = new StringBuilder();

        int i = a.length() - 1;
        int j = b.length() - 1;

        while (retrieveFromDynamicTable(i, j) > 0){
            if( retrieveFromDynamicTable(i - 1, j) == retrieveFromDynamicTable(i , j)){
                builder.insert(0, a.charAt(i));
                i--;
            } else if(retrieveFromDynamicTable(i, j - 1) == retrieveFromDynamicTable(i, j)) {
                builder.insert(0, b.charAt(j));
                j--;
            } else {
                builder.insert(0, a.charAt(i));
                i--;
                j--;
            }
        }

        i++;
        j++;
        builder.insert(0, a.substring(0, i));
        builder.insert(0, b.substring(0, j));

        return builder.toString();
    }

    private int retrieveFromDynamicTable(int i, int j){
        if(i < 0 || j < 0){
            return 0;
        } else {
            return table[i][j];
        }
    }
}
