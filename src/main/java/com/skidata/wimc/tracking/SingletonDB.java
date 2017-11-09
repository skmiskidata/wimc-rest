package com.skidata.wimc.tracking;

import java.util.ArrayList;

/**
 * Created by skmi on 10. 11. 2017.
 */
public class SingletonDB {

    private ArrayList<UniqueCar> uniqueCars = new ArrayList<>();

    public ArrayList<UniqueCar> getUniqueCars() {
        return uniqueCars;
    }

    private static SingletonDB _instance = null;

    public synchronized static SingletonDB getInstance() {
        if (_instance == null) {
            _instance = new SingletonDB();
        }
        return _instance;
    }

}
