package itpsoft.englishvocabulary.alarm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.WelcomeActivity;
import itpsoft.englishvocabulary.ultils.Log;

//https://developer.android.com/training/scheduling/alarms.html
public class AlarmActivity extends Activity {

    private PendingIntent pendingIntent;
    private MediaPlayer mp = null;
    private int timeOut = 1000 * 60;
    private AudioManager mAudioManager;
    private int originalVolume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, alarmIntent, 0);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        Button stopMusic = (Button) findViewById(R.id.stopMusic);
        Button startApp = (Button) findViewById(R.id.startApp);
        mp = MediaPlayer.create(getBaseContext(), R.raw.audio);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
            }
        });

        stopMusic.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                mp.stop();
                finish();
                return false;
            }
        });

        playSound(this, getAlarmUri());
        new CountDownTimer(timeOut, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mp.stop();
                finish();
            }
        }.start();

        startApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                finish();
                Intent intent = new Intent(AlarmActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void playSound(final Context context, Uri alert) {
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    mp.start();
                } catch (Throwable t) {
                    Log.i("Animation", "Thread  exception " + t);
                }
            }
        });
        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }               //Get an alarm sound. Try for an alarm. If none set, try notification,

    //show lockscreen
    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }
    //Otherwise, ringtone.
    private Uri getAlarmUri() {

        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    @Override
    protected void onUserLeaveHint() {
        mp.stop();
        finish();
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        finish();
        super.onBackPressed();
    }
}
