package com.agiklo.oracledatabase.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface CurrentTimeInterface {

    default String getCurrentDateTime(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return dateFormatter.format(new Date());
    }
}
