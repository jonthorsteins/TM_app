package hi.is.tournamentmanager.tm.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public class NetworkHandler {
    //private static final String BASE_URL = "http://10.0.2.2:3000"; // When running locally on emulator
    private static final String BASE_URL = "https://tmrest.herokuapp.com"; // When running emulator
    //private static final String BASE_URL = "http://192.168.1.5:3000"; // When running on via USB
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, String token, AsyncHttpResponseHandler responseHandler) {
        if(token != null)
            client.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, JSONObject data, String type, String token, AsyncHttpResponseHandler responseHandler) {
        StringEntity body = null;
        try {
            body = new StringEntity(data.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        body.setContentType(new BasicHeader(HttpHeaders.CONTENT_TYPE, type));
        System.out.print(token);
        client.setAuthenticationPreemptive(true);
        if(token != null)
            client.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        client.post(null, getAbsoluteUrl(url), body, type, responseHandler);
    }

    public static void patch(String url, JSONObject data, String type, String token, AsyncHttpResponseHandler responseHandler) {
        StringEntity body = null;
        try {
            body = new StringEntity(data.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        body.setContentType(new BasicHeader(HttpHeaders.CONTENT_TYPE, type));
        System.out.print(token);
        client.setAuthenticationPreemptive(true);
        if(token != null)
            client.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        client.patch(null, getAbsoluteUrl(url), body, type, responseHandler);
    }

    public static void delete(String url, RequestParams params, String token, AsyncHttpResponseHandler responseHandler) {
        if(token != null)
            client.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        System.out.print("Request at: " + BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

    private static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
}
