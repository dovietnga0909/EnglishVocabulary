package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.regex.Pattern;

import itpsoft.englishvocabulary.ultils.Log;

/**
 * Created by Do on 20/07/2015.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private EditText edtFullname;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private Button btnRegister;

    private String fullname;
    private String username;
    private String password;
    private String confirmPassword;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        edtFullname = (EditText)findViewById(R.id.edtFullname);
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText)findViewById(R.id.edtConfirmPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        btnRegister.setOnClickListener(this);
    }
    private boolean validateFormRegister(){
        boolean validate;
        fullname        = edtFullname.getText().toString().trim();
        username        = edtUsername.getText().toString().trim();
        password        = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();
        boolean ptUsername = Pattern.compile("^[A-Za-z0-9]{4,30}$").matcher(username).matches();
        boolean ptPassword = Pattern.compile("^[A-Za-z0-9]{4,30}$").matcher(password).matches();

//        Log.d("NgaDV","ptUsername" + ptUsername);
//        Log.d("NgaDV","ptPassword" + ptPassword);

        if(fullname.equals("")){
            Log.d("NgaDV", fullname);
            edtFullname.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtFullname.requestFocus();
            validate = false;
            edtFullname.setError(getResources().getString(R.string.alert_empty));
        }else if(fullname.length() > 30){
            Log.d("NgaDV", fullname);
            edtFullname.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_fullname), Toast.LENGTH_SHORT).show();
            edtFullname.setError(getResources().getString(R.string.alert_fullname));
            edtFullname.requestFocus();
            validate = false;
        }else if (username.equals("")){
            Log.d("NgaDV",username);
            edtUsername.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtUsername.requestFocus();
            validate = false;
            edtUsername.setError(getResources().getString(R.string.alert_empty));
        }else if(!ptUsername){
            Log.d("NgaDV", "ptUsername: "+ptUsername);
            edtUsername.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_format), Toast.LENGTH_SHORT).show();
            edtUsername.requestFocus();
            edtUsername.setError(getResources().getString(R.string.alert_format));
            validate = false;
        }else if (password.equals("")){
            Log.d("NgaDV", password);
            edtPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
            validate = false;
            edtPassword.setError(getResources().getString(R.string.alert_empty));
        }else if(!ptPassword){
            Log.d("NgaDV", "ptPassword: "+ptPassword);
            edtPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_format), Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
            edtPassword.setError(getResources().getString(R.string.alert_format));
            validate = false;
        }else if (confirmPassword.equals("")){
            Log.d("NgaDV", confirmPassword);
            edtConfirmPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtConfirmPassword.requestFocus();;
            validate = false;
            edtConfirmPassword.setError(getResources().getString(R.string.alert_empty));
        }else if (!confirmPassword.equals(password)){
            Log.d("NgaDV",confirmPassword);
            edtConfirmPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//            Toast.makeText(this, getResources().getString(R.string.alert_pass_unequal_confirm), Toast.LENGTH_SHORT).show();
            edtConfirmPassword.setError(getResources().getString(R.string.alert_pass_unequal_confirm));
            edtConfirmPassword.requestFocus();
            validate = false;
        }else {
            validate = true;
        }
        return validate;
    }
    private void register(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        fullname        = edtFullname.getText().toString().trim();
        username        = edtUsername.getText().toString().trim();
        password        = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();
        params.add("username",username);
        params.add("fullname",fullname);
        params.add("password",password);

        Log.d("NgaDV", "fullname: " + fullname);
        Log.d("NgaDV", "username " + username);
        Log.d("NgaDV", "password: " + password);

        client.post(this, getResources().getString(R.string.api_register), params, new JsonHttpResponseHandler() {
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
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_error_try_again), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_success), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.d("NgaDV", "http onFailure");
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
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
            case R.id.btnRegister:
                if (validateFormRegister()){
                    register();
                }
        }
    }
}
