package magentoegypt.locafy.base_app;


import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class Ced_Load_Language {
    public static void setLocale(String local, Context con) {
        Locale locale = new Locale(local);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        con.getResources().updateConfiguration(config, con.getResources().getDisplayMetrics());
    }

    public void setLanguagetoLoad(String Language, Context context) {
        if(Language.equalsIgnoreCase("eg")){
            setLocale("ar", context); //set langauge of the app
        }else{
            setLocale("en", context); //set langauge of the app
        }

    }
}