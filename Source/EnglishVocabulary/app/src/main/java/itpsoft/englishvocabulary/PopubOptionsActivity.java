package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import itpsoft.englishvocabulary.adapter.DialogAdapter;
import itpsoft.englishvocabulary.adapter.DialogTempAdapter;
import itpsoft.englishvocabulary.models.Temp;
import itpsoft.englishvocabulary.models.Topic;
import itpsoft.englishvocabulary.receiver.PopubAlarmReceiver;
import itpsoft.englishvocabulary.service.ScreenOnService;
import itpsoft.englishvocabulary.ultils.AdmodBanner;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Do on 03/09/2015.
 */
public class PopubOptionsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout lnChooseTopic, lnChooseNotifications, lnChooseNumberVoca;
    private TextView tvChoosedTopic, tvChoosedNumberVoca;
    private ToggleButton tbStatus;
    private ImageView imgBack;
    private AlertDialog alertDialog;
    private Rect displayRectangle;

    private DialogAdapter dialogAdapter;
    private DialogTempAdapter dialogTempAdapter;
    private ArrayList<Topic> listTopic;
    private ArrayList<Topic> listTopicChoosed;
    private ArrayList<Temp> listNotifications;
    private ArrayList listNumberVoca;
    private Topic topic;
    private Temp temp;

    private String strIdTopicChoosed;
    private String strNameTopicChoosed;

    private Context context;
    private boolean clickItem;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;

    private AdView adView;
    private AdmodBanner admodBanner;

    private String checkStatePopub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popub_options);
        //Admod
        /*adView = (AdView) findViewById(R.id.adView);
        admodBanner = new AdmodBanner(adView);*/
        init();
    }

    private void init() {

        context = PopubOptionsActivity.this;
        topic = new Topic(context);
        SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"1");

        displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        //initial
        lnChooseTopic = (LinearLayout) findViewById(R.id.lnChooseToppic);
//        lnChooseNotifications = (LinearLayout) findViewById(R.id.lnChooseNotifications);
        lnChooseNumberVoca = (LinearLayout) findViewById(R.id.lnChooseNumberVocabulary);

        tvChoosedTopic = (TextView) findViewById(R.id.tvChoosedTopic);
//        tvChoosedNotifications = (TextView) findViewById(R.id.tvChoosedNotifications);
        tvChoosedNumberVoca = (TextView) findViewById(R.id.tvChoosedNumberVocabulary);

        tbStatus = (ToggleButton) findViewById(R.id.tgStatus);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        //action
        tvChoosedTopic.setText(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_PUPUB_TOPIC, context.getString(R.string.choose_topic)));
//        tvChoosedNotifications.setText(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_POPUB_NOTIFI, context.getString(R.string.choose_notifications)));
        tvChoosedNumberVoca.setText(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_POPUB_NUMBER_VOCA, context.getString(R.string.choose_number_voca)));

        ///start up
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(context, PopubAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//        reminTime = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_REMIN_TIME, (long) -1);

        checkStatePopub = SPUtil.instance(context).get(SPUtil.KEY_POPUB_STATE, context.getResources().getString(R.string.off));
        if(checkStatePopub.equals(context.getResources().getString(R.string.off))){
            tbStatus.setChecked(false);
        }else {
            tbStatus.setChecked(true);
        }
        imgBack.setOnClickListener(this);

        lnChooseTopic.setOnClickListener(this);
//        lnChooseNotifications.setOnClickListener(this);
        lnChooseNumberVoca.setOnClickListener(this);

//        String

        tbStatus.setOnCheckedChangeListener(this);
    }

    private void DialogChooseTopic() {
        listTopic = topic.getAll();
        listTopicChoosed = topic.getTopicByStrId(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_ID_POPUB_TOPIC, context.getString(R.string.choose_topic)));

        if (!listTopicChoosed.isEmpty()) {
            for (int i = 0; i < listTopicChoosed.size(); i++) {
                Topic topic = listTopicChoosed.get(i);
                for (int j = 0; j < listTopic.size(); j++) {
                    Topic topic1 = listTopic.get(j);
                    if (topic1.getId() == topic.getId()) {
                        topic1.setIsChecked(true);
                    }
                }
            }
        }

        dialogAdapter = new DialogAdapter(context, R.layout.item_checkbox, listTopic);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layoutCheckbox = LayoutInflater.from(context).inflate(R.layout.dialog_choose, null, false);

        final ListView lvTopic = (ListView) layoutCheckbox.findViewById(R.id.lvItem);
        Button btnDone = (Button) layoutCheckbox.findViewById(R.id.btnDone);
        TextView tvTitle = (TextView) layoutCheckbox.findViewById(R.id.title);
        ImageView imgDialogBack = (ImageView) layoutCheckbox.findViewById(R.id.back);

        tvTitle.setText(getResources().getString(R.string.choose_topic));
        imgDialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        lvTopic.setAdapter(dialogAdapter);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strIdTopicChoosed = "";
                strNameTopicChoosed = "";
                listTopicChoosed.clear();
                for (int i = 0; i < listTopic.size(); i++) {
                    if (listTopic.get(i).isChecked()) {
                        listTopicChoosed.add(listTopic.get(i));
                    }
                }
                if (listTopicChoosed.size() == 0) {
                    Toast.makeText(context, context.getResources().getString(R.string.must_choose_one), Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < listTopicChoosed.size(); i++) {
                        if (i == (listTopicChoosed.size() - 1)) {
                            strNameTopicChoosed += listTopicChoosed.get(i).getName();
                            strIdTopicChoosed += listTopicChoosed.get(i).getId();
                        } else {
                            strNameTopicChoosed += listTopicChoosed.get(i).getName() + ",";
                            strIdTopicChoosed += listTopicChoosed.get(i).getId() + ",";
                        }
                    }
                    SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_ID_POPUB_TOPIC, strIdTopicChoosed);
                    SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_NAME_PUPUB_TOPIC, strNameTopicChoosed);
                    tvChoosedTopic.setText(strNameTopicChoosed);
                    listTopic.clear();
                    listTopicChoosed.clear();
                    alertDialog.dismiss();
                }

            }
        });
        layoutCheckbox.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        builder.setView(layoutCheckbox);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void DialogChooseNotificatons() {
        listNotifications = new ArrayList<Temp>();
        listNotifications.add(new Temp("1",context.getResources().getString(R.string.after_lock_on)));
        listNotifications.add(new Temp("2",context.getResources().getString(R.string.after_2_h)));
        listNotifications.add(new Temp("3",context.getResources().getString(R.string.after_4_h)));
        listNotifications.add(new Temp("4",context.getResources().getString(R.string.after_6_h)));
        listNotifications.add(new Temp("5",context.getResources().getString(R.string.after_8_h)));

        Log.d("NgaDV", "listNotifications.size()" + listNotifications.size());

        dialogTempAdapter = new DialogTempAdapter(context,listNotifications,R.layout.item_radio);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layoutCheckbox = LayoutInflater.from(context).inflate(R.layout.dialog_choose,null,false);

        ListView lvItem = (ListView) layoutCheckbox.findViewById(R.id.lvItem);
        Button      btnDone = (Button) layoutCheckbox.findViewById(R.id.btnDone);
        TextView    tvTitle = (TextView)layoutCheckbox.findViewById(R.id.title);
        ImageView imgDialogBack = (ImageView)layoutCheckbox.findViewById(R.id.back);

        tvTitle.setText(getResources().getString(R.string.choose_topic));
        imgDialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        lvItem.setAdapter(dialogTempAdapter);
        lvItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem = true;
                temp = (Temp) parent.getAdapter().getItem(position);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItem){
                    SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_NAME_POPUB_NOTIFI,temp.getText());
                    SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,temp.getValue());
                }
//                tvChoosedNotifications.setText(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_POPUB_NOTIFI, temp.getText()));
                clickItem = false;
                alertDialog.dismiss();
            }
        });
        layoutCheckbox.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        builder.setView(layoutCheckbox);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void DialogChooseNumberVoca() {
        listNotifications = new ArrayList<Temp>();
        listNotifications.add(new Temp("1",context.getResources().getString(R.string.one_voca)));
        listNotifications.add(new Temp("2",context.getResources().getString(R.string.two_voca)));
        listNotifications.add(new Temp("3",context.getResources().getString(R.string.three_voca)));
        listNotifications.add(new Temp("4",context.getResources().getString(R.string.four_voca)));
        listNotifications.add(new Temp("5",context.getResources().getString(R.string.five_voca)));

        Log.d("NgaDV", "listNotifications.size()" + listNotifications.size());

        dialogTempAdapter = new DialogTempAdapter(context,listNotifications,R.layout.item_radio);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layoutCheckbox = LayoutInflater.from(context).inflate(R.layout.dialog_choose,null,false);

        ListView lvItem = (ListView) layoutCheckbox.findViewById(R.id.lvItem);
        Button      btnDone = (Button) layoutCheckbox.findViewById(R.id.btnDone);
        TextView    tvTitle = (TextView)layoutCheckbox.findViewById(R.id.title);
        ImageView imgDialogBack = (ImageView)layoutCheckbox.findViewById(R.id.back);

        tvTitle.setText(getResources().getString(R.string.choose_topic));
        imgDialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        lvItem.setAdapter(dialogTempAdapter);
        lvItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem = true;
                temp = (Temp) parent.getAdapter().getItem(position);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItem){
                    SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_NAME_POPUB_NUMBER_VOCA,temp.getText());
                    SPUtil.instance(context).set(SPUtil.KEY_CHOOSED_KEY_POPUB_NUMBER_VOCA, temp.getValue());
                    tvChoosedNumberVoca.setText(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_POPUB_NUMBER_VOCA, temp.getText()));
                }
                clickItem = false;
                alertDialog.dismiss();
            }
        });
        layoutCheckbox.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        builder.setView(layoutCheckbox);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                Intent intent = new Intent(context,HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                break;
            case R.id.lnChooseToppic:
                DialogChooseTopic();
                break;
//            case R.id.lnChooseNotifications:
//                DialogChooseNotificatons();
//                break;
            case R.id.lnChooseNumberVocabulary:
                DialogChooseNumberVoca();
                break;
        }
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            Intent serviceScreenOn = new Intent(context,ScreenOnService.class);
            serviceScreenOn.setAction("itpsoft.englishvocabulary.service.ScreenOnService");
            Log.d("NgaDV", "tbg_true");
            if (validateFormPopub()){
                startService(serviceScreenOn);
                Toast.makeText(context, context.getString(R.string.change_success), Toast.LENGTH_SHORT).show();
            }else {
                tbStatus.setChecked(false);
                stopService(serviceScreenOn);
            }
//            startService(serviceScreenOn);
//            if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("1")){
//
//                startService(serviceScreenOn);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 1",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("2")){
//                stopService(serviceScreenOn);
//                Calendar now = Calendar.getInstance();
//                Calendar calendar = Calendar.getInstance();
//                if (calendar.before(now)) {
//                    calendar.add(Calendar.DATE, 1);
//                }
////                long time = calendar.getTimeInMillis();
//                long time = now.getTimeInMillis();
////                Log.d("NgaDV","phut = " + calendar.get(Ca));
//                startAlarm(time+500);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 2",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("3")){
//                stopService(serviceScreenOn);
//                /*
//                * đoạn code này để hiển thị text trên màn hình khoá. cũng không cần quan tâm đến nó đâu :D
//                *
//                * */
//                //String message = "This is a test";
//                //Settings.System.putString(context.getContentResolver(),
//                //        Settings.System.NAME, message);
//
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 3",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("4")){
//                stopService(serviceScreenOn);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 4",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("5")){
//                stopService(serviceScreenOn);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 5",Toast.LENGTH_SHORT).show();
//            }
        } else {
            Intent serviceScreenOn = new Intent(context,ScreenOnService.class);
            serviceScreenOn.setAction("itpsoft.englishvocabulary.service.ScreenOnService");

            SPUtil.instance(context).set(SPUtil.KEY_POPUB_STATE, context.getResources().getString(R.string.off));
            stopService(serviceScreenOn);
            Toast.makeText(context, context.getString(R.string.turn_off_popub), Toast.LENGTH_SHORT).show();
            Log.d("NgaDV", "tbg_false");
        }
    }
    private void startAlarm(long time) {
//        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_REMIN_TIME, time);
        //cancel alarm
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        // new alarm
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,time,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private boolean validateFormPopub(){
        boolean result = true;

        String strListVocaChoosed = SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_PUPUB_TOPIC, context.getResources().getString(R.string.choose_topic));
        String strListNumberVoca = SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_NAME_POPUB_NUMBER_VOCA, context.getResources().getString(R.string.choose_number_voca));
        if (strListVocaChoosed.equals(context.getResources().getString(R.string.choose_topic))){
            result = false;
            lnChooseTopic.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
            Toast.makeText(context, context.getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
        }else if (strListNumberVoca.equals(context.getResources().getString(R.string.choose_number_voca))){
            result = false;
            lnChooseNumberVoca.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake));
            Toast.makeText(context, context.getString(R.string.alert_empty), Toast.LENGTH_SHORT).show();
        }else {
            SPUtil.instance(context).set(SPUtil.KEY_POPUB_STATE, context.getResources().getString(R.string.on));
            result = true;
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context,HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NgaDV", "Onresume");

    }

    @Override
    protected void onDestroy() {
        Log.d("NgaDV", "on destroy activity");
        if (tbStatus.isChecked()) {

            Intent serviceScreenOn = new Intent(context,ScreenOnService.class);
            serviceScreenOn.setAction("itpsoft.englishvocabulary.service.ScreenOnService");
            Log.d("NgaDV", "tbg_true");
            if (validateFormPopub()){
                startService(serviceScreenOn);
            }else {
                tbStatus.setChecked(false);
                stopService(serviceScreenOn);
            }
//            startService(serviceScreenOn);
//            if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("1")){
//
//                startService(serviceScreenOn);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 1",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("2")){
//                stopService(serviceScreenOn);
//                Calendar now = Calendar.getInstance();
//                Calendar calendar = Calendar.getInstance();
//                if (calendar.before(now)) {
//                    calendar.add(Calendar.DATE, 1);
//                }
////                long time = calendar.getTimeInMillis();
//                long time = now.getTimeInMillis();
////                Log.d("NgaDV","phut = " + calendar.get(Ca));
//                startAlarm(time+500);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 2",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("3")){
//                stopService(serviceScreenOn);
//                /*
//                * đoạn code này để hiển thị text trên màn hình khoá. cũng không cần quan tâm đến nó đâu :D
//                *
//                * */
//                //String message = "This is a test";
//                //Settings.System.putString(context.getContentResolver(),
//                //        Settings.System.NAME, message);
//
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 3",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("4")){
//                stopService(serviceScreenOn);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 4",Toast.LENGTH_SHORT).show();
//            }else if(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NOTIFI,"").equals("5")){
//                stopService(serviceScreenOn);
//                Toast.makeText(context,"KEY_CHOOSED_KEY_POPUB_NOTIFI = 5",Toast.LENGTH_SHORT).show();
//            }
        } else {
            Intent serviceScreenOn = new Intent(context,ScreenOnService.class);
            serviceScreenOn.setAction("itpsoft.englishvocabulary.service.ScreenOnService");

            SPUtil.instance(context).set(SPUtil.KEY_POPUB_STATE, context.getResources().getString(R.string.off));
            stopService(serviceScreenOn);
            Log.d("NgaDV", "tbg_false");
        }
        super.onDestroy();
    }
}

