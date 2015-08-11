package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import itpsoft.englishvocabulary.ultils.Log;

/**
 * Created by Do on 30/07/2015.
 */
public class FeedBackActivity extends Activity implements View.OnClickListener {

    private EditText edtContentFeedBack;
    private Button btnSend;
    private ProgressDialog progressDialog;
    private String content;

    private AdView adView;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_back);
        //Admod
        adView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
        init();
    }

    public void init(){
        edtContentFeedBack = (EditText)findViewById(R.id.edtContentFeedBack);
        btnSend = (Button)findViewById(R.id.btnSendFeedBack);
        progressDialog = new ProgressDialog(this);

        btnSend.setOnClickListener(this);

        progressDialog.setMessage(getResources().getString(R.string.please_wait));
    }

    public void sendFeedBack(){

        content = edtContentFeedBack.getText().toString().trim();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("content",content);

        client.post(this,getResources().getString(R.string.api_feedback),params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                Log.d("NgaDV","onstart");
                progressDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject json = new JSONObject(response.toString());
                    Log.d("NgaDV","Json : " + json);
                    if (json.getString("error").equals("1")) {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_error_try_again), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), json.getString("error_msg"), Toast.LENGTH_SHORT).show();

                        Log.d("NgaDV", "onstart");
                    } else {

                        Log.d("NgaDV","onSuccess");
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_success), Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Log.d("NgaDV", "onFailure");
                Toast.makeText(FeedBackActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();

            }

        });
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendFeedBack:
            if (!edtContentFeedBack.getText().toString().trim().equals("")){
                sendFeedBack();
            }else {
                edtContentFeedBack.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//                Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
                edtContentFeedBack.requestFocus();
                edtContentFeedBack.setError(getResources().getString(R.string.alert_empty));
            }
        }
    }
}
