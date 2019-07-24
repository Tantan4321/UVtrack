package com.tantan4321.uvtracker;

import java.util.ArrayList;

public class DataStore {
    ArrayList<Double>[] data = new ArrayList[3];

    double[] latest = new double[3];

    int timer;

    private static DataStore m_pInstance;
    public static DataStore GetInstance() {
        if (m_pInstance == null) m_pInstance = new DataStore();
        return m_pInstance;
    }

    public DataStore(){
        for(int i = 0; i < 3; i++){
            data[i] = new ArrayList<Double>();
            latest[i] = 0.0;
        }
        timer = 5085;
    }

    public void addVal(double uva, double uvb, double uv){
        data[0].add(uva);
        data[1].add(uvb);
        data[2].add(uv);
        latest[0] = uva;
        latest[1] = uvb;
        latest[2] = uv;
        if(Double.compare(uv, 1.00) > 0){
            timer--;
        }
    }

    public int getTimer() {
        return timer;
    }

    public double[] getLatest() {
        return latest;
    }
}
