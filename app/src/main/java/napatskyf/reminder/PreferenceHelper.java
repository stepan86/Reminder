package napatskyf.reminder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SERVER 1C 8 hlib on 11.09.2016.
 */
public class PreferenceHelper  {
    private static PreferenceHelper instance;
    private Context context;
    private SharedPreferences sharedPreferences;
    public  static  final String SPLASH_IS_INVISIBLE = "splash_is_invisible";

    private PreferenceHelper() {

    }

    public static PreferenceHelper getInstance(){
        if (instance == null){
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);

    }

    public  void putBoolean(String key , Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

}
