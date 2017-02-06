package me.Austin.MT;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created by mcaus_000 on 2/5/2017.
 */
public class CustomFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord record) {
        String msg = record.getMessage();
        System.out.println(msg);
        return !(record.getMessage().contains("singup") || record.getMessage().contains("login"));
    }

}
