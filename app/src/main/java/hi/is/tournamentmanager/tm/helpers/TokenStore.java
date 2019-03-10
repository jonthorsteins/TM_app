package hi.is.tournamentmanager.tm.helpers;

import android.content.SharedPreferences;

public class TokenStore {
    private final static String TOKEN = "TOKEN";
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
}
