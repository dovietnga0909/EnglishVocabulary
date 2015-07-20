package itpsoft.englishvocabulary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Do on 17/07/2015.
 */
public class SplashActivity extends Activity{
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh);

        countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("NgaDV","onTick");
            }

            @Override
            public void onFinish() {

                Log.d("NgaDV","onFinish()");
                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(intent);

                SplashActivity.this.finish();
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        countDownTimer.cancel();
    }
}