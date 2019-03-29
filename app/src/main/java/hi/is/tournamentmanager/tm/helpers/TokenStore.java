package hi.is.tournamentmanager.tm.helpers;

import android.content.SharedPreferences;

public class TokenStore {
    private final static String TOKEN = "TOKEN";
    private final static String USER_ID = "USER_ID";
    private final static String USERNAME = "USER_NAME";

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

    public static Long getUserId(SharedPreferences sharedPreferences){
        return sharedPreferences.getLong(USER_ID, -1);
    }

    public static String getUserName(SharedPreferences sharedPreferences){
        return sharedPreferences.getString(USERNAME, "");
    }

    public static void storeUser(SharedPreferences sharedPreferences, Long user_id, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_ID, user_id);
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static void clearUser(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_ID);
        editor.apply();
    }
}
