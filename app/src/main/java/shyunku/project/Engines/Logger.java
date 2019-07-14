package shyunku.project.Engines;

import android.util.Log;

public class Logger {
    public static void Log(String tag, String msg){
        Log.e("try", tag + " = "+ msg);
    }

    public static void Log(String msg){
        Log.e("try", msg);
    }
}
