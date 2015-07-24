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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.Log;
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
    private Vocabulary vocabulary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }
    private void login(){
        Log.d("NgaDV", "login onclick");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);

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
                Log.d("NgaDV", "http onsuccess");
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.getString("error").equals("1")) {
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
                Log.d("NgaDV", "http onFailure");
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
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
            edtUsername.requestFocus();
            validate = false;
        }else if (password.equals("")){
            edtPassword.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            Toast.makeText(this,getResources().getString(R.string.alert_empty),Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
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

    private void syncAddDataToDatabase(){
        vocabulary = new Vocabulary();
        vocabulary.excuteAddDataToDatabase(LoginActivity.this, new Vocabulary.OnLoadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                Log.d("LuanDT", "onFailure syncAddDataToDatabase");
            }
        });
    }
}
