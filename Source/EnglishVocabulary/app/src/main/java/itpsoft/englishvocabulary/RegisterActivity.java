package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.ProgressDialog;
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
        //hide keyboard firstTime
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
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

        if(fullname.equals("")){
            Log.d("NgaDV",fullname);
            edtFullname.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtFullname.setFocusable(true);
            validate = false;
        }else if (username.equals("")){
            Log.d("NgaDV",username);
            edtUsername.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtUsername.setFocusable(true);
            validate = false;
        }else if (password.equals("")){
            Log.d("NgaDV",password);
            edtPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtPassword.setFocusable(true);
            validate = false;
        }else if (confirmPassword.equals("")){
            Log.d("NgaDV",confirmPassword);
            edtConfirmPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast.makeText(this, getResources().getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
            edtConfirmPassword.setFocusable(true);
            validate = false;
        }else if (!confirmPassword.equals(password)){
            Log.d("NgaDV",confirmPassword);
            edtConfirmPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast.makeText(this, getResources().getString(R.string.alert_pass_unequal_confirm), Toast.LENGTH_SHORT).show();
            edtConfirmPassword.setFocusable(true);
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

        Log.d("NgaDV", fullname);
        Log.d("NgaDV", username);
        Log.d("NgaDV", password);

        client.post(this, getResources().getString(R.string.api_register), params, new JsonHttpResponseHandler() {
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
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_error_try_again), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_success), Toast.LENGTH_SHORT).show();
                        finish();
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
