package com.example.julia.weatherguide.data.data_services.base;

import com.example.julia.weatherguide.utils.Preconditions;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;


public class BaseDatabaseService {

    private final StorIOSQLite sqLite;

    protected BaseDatabaseService(StorIOSQLite sqLite){
        Preconditions.nonNull(sqLite);
        this.sqLite = sqLite;
    }

    protected StorIOSQLite getSQLite(){
        return sqLite;
    }

}
