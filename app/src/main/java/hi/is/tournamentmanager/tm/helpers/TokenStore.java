package hi.is.tournamentmanager.tm.helpers;

import android.content.SharedPreferences;

public class TokenStore {
    private final static String TOKEN = "TOKEN";
    private final static String USER = "USER";

    public static String getToken(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(TOKEN,"");
    };

    public static void storeToken(SharedPreferences sharedPreferences, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static void clearToken(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN);
        editor.apply();
    }

    public static Long getUser(SharedPreferences sharedPreferences){
        return sharedPreferences.getLong(USER, -1);
    }

    public static void storeUser(SharedPreferences sharedPreferences, Long user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER, user);
        editor.apply();
    }

    public static void clearUser(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER);
        editor.apply();
    }
}
