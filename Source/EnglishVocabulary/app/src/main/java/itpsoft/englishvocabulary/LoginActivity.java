package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Do on 20/07/2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;

    private String username;
    private String password;

    private String userId;
    private String fullname;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide keyboard firstTime
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.activity_login);
        init();

    }

    private void init(){
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        btnLogin    =  (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }
    private void login(){
        Log.d("NgaDV","login onclick");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("username",username);
        params.add("password",password);

        progressDialog.show();

        client.post(this, getResources().getString(R.string.api_login), params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                Log.d("NgaDV", "http onstart");
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("NgaDV", "http onsuccess");
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.getString("error").equals("1")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_pass_username_wrong), Toast.LENGTH_SHORT).show();
                    } else {

                        userId = json.getString("user_id");
                        fullname = json.getString("fullname");

                        Log.d("NgaDV", userId + "userID");
                        Log.d("NgaDV", fullname + "fullname");
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_LOGIN, "true");
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_FULLNAME, fullname);
                        SPUtil.instance(LoginActivity.this).set(SPUtil.KEY_USER_ID, userId);

                        finish();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("NgaDV", "http onFailure");
                progressDialog.cancel();
            }
        });


    }

    private boolean validateFormLogin(){
        boolean validate;
        username = edtUsername.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (username.equals("")){
            edtUsername.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            Toast.makeText(this,getResources().getString(R.string.alert_empty),Toast.LENGTH_SHORT).show();
            edtUsername.setFocusable(true);
            validate = false;
        }else if (password.equals("")){
            edtPassword.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            Toast.makeText(this,getResources().getString(R.string.alert_empty),Toast.LENGTH_SHORT).show();
            edtPassword.setFocusable(true);
            validate = false;
        }else {
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
        switch (v.getId()){
            case R.id.btnLogin:
                if (validateFormLogin()){
                    login();
                }
                break;
            case R.id.btnRegister:
                Intent  intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
        }
    }
}
