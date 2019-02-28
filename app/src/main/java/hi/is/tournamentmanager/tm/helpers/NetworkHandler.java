package hi.is.tournamentmanager.tm.helpers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public class NetworkHandler {

    private static final String BASE_URL = "http://10.0.2.2:8080";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, JSONObject data, String type, AsyncHttpResponseHandler responseHandler) {
        StringEntity body = null;
        try {
            body = new StringEntity(data.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        body.setContentType(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        client.post(null, getAbsoluteUrl(url), body, type, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
