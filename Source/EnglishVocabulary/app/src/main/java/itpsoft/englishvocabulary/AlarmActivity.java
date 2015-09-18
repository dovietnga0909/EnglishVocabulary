package itpsoft.englishvocabulary;

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
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import itpsoft.englishvocabulary.receiver.AlarmReceiver;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;

//https://developer.android.com/training/scheduling/alarms.html
public class AlarmActivity extends Activity {

    private PendingIntent pendingIntent;
    private MediaPlayer mp = null;
    private int timeOut = 1000 * 60;
    private AudioManager mAudioManager;
    private int originalVolume;
    private long remindTime;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);
        remindTime = SPUtil.instance(AlarmActivity.this).get(SPUtil.KEY_REMIN_TIME, (long) -1);
        if (remindTime != -1) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(remindTime);
            String hour = "";
            String minute = "";
            if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                hour = "0" + calendar.get(Calendar.HOUR_OF_DAY);
            } else {
                hour = "" + calendar.get(Calendar.HOUR_OF_DAY);
            }
            if (calendar.get(Calendar.MINUTE) < 10) {
                minute = "0" + calendar.get(Calendar.MINUTE);
            } else {
                minute = "" + calendar.get(Calendar.MINUTE);
            }
            TextView time = (TextView) findViewById(R.id.time);
            time.setText(hour + ":" + minute);
        } else {
            finish();
        }
        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        v.vibrate(pattern, 1);
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
                v.cancel();
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
                v.cancel();
                mp.stop();
                finish();
            }
        }.start();

        startApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.cancel();
                mp.stop();
                finish();
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
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
        v.cancel();
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
        v.cancel();
        finish();
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        v.cancel();
        finish();
        super.onBackPressed();
    }
}
