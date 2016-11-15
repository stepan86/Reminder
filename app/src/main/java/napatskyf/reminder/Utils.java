package napatskyf.reminder;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SERVER 1C 8 hlib on 13.09.2016.
 */
public class Utils {
public  static String getDate(long date){
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
    return  dateFormat.format(date);

}
    public  static String getTime(long date){
       SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return  dateFormat.format(date);

    }

    public static String getFullDate(long date)
    {

        SimpleDateFormat fullDataFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

        return fullDataFormat.format(date);
    }


}
