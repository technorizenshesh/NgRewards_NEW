package main.com.ngrewards.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

import main.com.ngrewards.constant.MySession;


/**
 * Manages setting of the app's locale.
 */
public class LocaleHelper {

    public static Context onAttach(Context context) {
        String locale = getPersistedLocale(context);
        return setLocale(context, locale);
    }

    public static String getPersistedLocale(Context context) {
        MySession preferences = new MySession(context);
        return preferences.getValueOf(MySession.KEY_LANGUAGE);
    }


    public static Context setLocale(Context context, String localeSpec) {
        Locale locale;
        if (localeSpec.equals("system")) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = new Locale(localeSpec);
        }
        Locale.setDefault(locale);
        return updateResources(context, locale);
    }

    private static Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
