package itpsoft.englishvocabulary.gcm;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by LuanDT on 17/09/2015.
 */
public class CommonUtilities {
    //url server add RegId
    public static final String SERVER_URL = "http://192.168.1.154:8080/ServicesEnglishVocabulary/rest/gcm/add";
    //sender Id (google project number)
    public static final String SENDER_ID = "46449090875";
    //action receiver
    public static final String DISPLAY_MESSAGE_ACTION = "DISPLAY_MESSAGE";

    public static void register(final Context context, String reg_id) {
        RequestParams params = new RequestParams();
        params.add("reg_id", reg_id);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(SERVER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String jsonResponse = response.toString();
                Log.d("LuanDT", "jsonResponse: " + jsonResponse);
                try {
                    JSONObject object = new JSONObject(jsonResponse);
                    if (object.getInt("success") == 1) {
                        SPGcmUtils.instance(context).set(SPGcmUtils.PROPERTY_ON_SERVER, true);
                    } else {
                        SPGcmUtils.instance(context).set(SPGcmUtils.PROPERTY_ON_SERVER, false);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("LuanDT", "add regId onFailure");
            }
        });
    }
}
