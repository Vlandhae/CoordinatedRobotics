package com.application.base.actions.actions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampHelper {
    public static String getTimeStamp() {
        return new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(new Timestamp(new Date().getTime())).toString();
    }
}
