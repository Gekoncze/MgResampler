package cz.mg.resampler.gui.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ErrorUtilities {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("k:m:s");
    
    public static String getStackTrace(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
    public static String generateLog(Exception e){
        String time = DATE_FORMAT.format(Calendar.getInstance().getTime());
        if(e == null) return "[" + time + "] " + "null";
        String exceptionName = e.getClass().getSimpleName();
        String exceptionMessage = e.getMessage();
        String stackTrace = ErrorUtilities.getStackTrace(e);
        return "[" + time + "] " + exceptionName + ": " + exceptionMessage + "\n--------------------------------------------------\n" + stackTrace;
    }
}
