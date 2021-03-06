package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Do on 20/07/2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;

    private String username;
    private String password;

    private String userId;
    private String fullname;
    private ProgressDialog progressDialog;
    private Vocabulary vocabulary;
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        setContentView(R.layout.activity_login);

//        AdView adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        init();

        btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LuanDT", "onSuccess login facebook");

                profile = Profile.getCurrentProfile();
                if (profile != null) {

                    loginFacebook(profile.getId());

                }
            }

            @Override
            public void onCancel() {
                Log.d("LuanDT", "onCancel login facebook");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("LuanDT", "onError login facebook");
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void login() {
        Log.d("NgaDV", "login onclick");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);

        Log.d("LuanDT", "params: " + params);

        client.post(this, getResources().getString(R.string.api_login), params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d("NgaDV", "http onstart");
                progressDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("LuanDT", "response.toString(): " + response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    Log.d("NgaDV", "json : " + json);
                    if (json.getInt("error") == 1) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_pass_username_wrong), Toast.LENGTH_SHORT).show();
                    } else {

                        userId = json.getString("user_id");
                        fullname = json.getString("fullname");

                        Log.d("NgaDV", "userId: " + userId);
                        Log.d("NgaDV", "fullname: " + fullname);
                        Log.d("NgaDV", "username: " + edtUsername.getText().toString());

                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_LOGIN, true);
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_FULLNAME, fullname);
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_USER_ID, userId);
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_USERNAME, edtUsername.getText().toString());

                        //get all data
                        syncAddDataToDatabase();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.d("LuanDT", "http onFailure");
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("LuanDT", "http onFailure Throwable: " + responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    private boolean validateFormLogin() {
        boolean validate;
        username = edtUsername.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (username.equals("")) {
            edtUsername.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this,getResources().getString(R.string.alert_empty),Toast.LENGTH_SHORT).show();
            edtUsername.requestFocus();
            validate = false;
            edtUsername.setError(getResources().getString(R.string.alert_empty));
        } else if (password.equals("")) {
            edtPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this,getResources().getString(R.string.alert_empty),Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
            edtPassword.setError(getResources().getString(R.string.alert_empty));
            validate = false;
        } else {
            validate = true;
        }
        return validate;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (validateFormLogin()) {
                    login();
                }
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        }
    }

    private void syncAddDataToDatabase() {
        DbController dbController = DbController.getInstance(LoginActivity.this);
        dbController.deleteAllDataTable();
        vocabulary = new Vocabulary();
        vocabulary.excuteAddDataToDatabase(LoginActivity.this, new Vocabulary.OnLoadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                Log.d("LuanDT", "onFailure syncAddDataToDatabase");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        super.onBackPressed();
    }

    //login facebook
    private void loginFacebook(String userIdFacebook) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("username", userIdFacebook);

        Log.d("LuanDT", "params: " + params);

        client.post(this, getResources().getString(R.string.api_login_facebook), params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("LuanDT", "response.toString(): " + response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.getInt("error") == 1) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_error_try_again), Toast.LENGTH_SHORT).show();
                    } else {

                        Log.d("LuanDT", "userId: " + json.getInt("user_id"));

                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_LOGIN, true);
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_LOGIN_FACEBOOK, true);
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_FULLNAME, profile.getFirstName() + " " + profile.getMiddleName() + " " + profile.getLastName());
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_USER_ID, "" + json.getInt("user_id"));
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_USERNAME, "" + profile.getId());

                        //get all data
                        syncAddDataToDatabase();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.d("LuanDT", "http onFailure");
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("LuanDT", "http onFailure Throwable: " + responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }
}
