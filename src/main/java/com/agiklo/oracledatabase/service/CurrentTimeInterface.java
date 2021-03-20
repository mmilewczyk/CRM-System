package com.agiklo.oracledatabase.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * the task of the interface is to set the date in a specific format
 *
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
public interface CurrentTimeInterface {

    default String getCurrentDateTime(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return dateFormatter.format(new Date());
    }
}
