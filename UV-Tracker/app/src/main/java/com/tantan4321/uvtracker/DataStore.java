package com.tantan4321.uvtracker;

import java.util.ArrayList;

public class DataStore {
    ArrayList<Double>[] data = new ArrayList[3];

    private static DataStore m_pInstance;
    public static DataStore GetInstance() {
        if (m_pInstance == null) m_pInstance = new DataStore();
        return m_pInstance;
    }

    public DataStore(){
        for(int i = 0; i < 3; i++){
            data[i] = new ArrayList<Double>();
        }
    }

    public void addVal(double uva, double uvb, double uv){
        data[0].add(uva);
        data[1].add(uvb);
        data[2].add(uv);
    }


}
