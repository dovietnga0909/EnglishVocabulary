package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import itpsoft.englishvocabulary.adapter.MenuAdapter;
import itpsoft.englishvocabulary.adapter.TopicGridAdapter;
import itpsoft.englishvocabulary.alarm.AlarmReceiver;
import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.google.gcm.ServerUtilities;
import itpsoft.englishvocabulary.google.utils.CommonUtilities;
import itpsoft.englishvocabulary.google.utils.WakeLocker;
import itpsoft.englishvocabulary.models.MenuItem;
import itpsoft.englishvocabulary.models.Question;
import itpsoft.englishvocabulary.models.Topic;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.AdmodBanner;
import itpsoft.englishvocabulary.ultils.Keyboard;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;
import itpsoft.englishvocabulary.view.DrawerArrowDrawable;

import static android.view.Gravity.START;

public class HomeActivity extends Activity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private DrawerLayout drawer;
    private Resources resources;
    private float offset;
    private boolean flipped;
    private ListView listMenu;
    private ArrayList<Object> arrMenu;
    private MenuAdapter menuAdapter;
    private ImageView add;
    private AlertDialog alertDialog;
    private Rect displayRectangle;
    private Topic topic;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private TextView fullName;
    private int intervalTime = 1000 * 60 * 60 * 24;
    private long reminTime = -1;
    private boolean modify = false;
    private ProgressDialog progressDialog;
    private Vocabulary vocabulary;
    private DbController dbController;
    private String s_hours = "";
    private String s_minute = "";
    private String s_months = "";
    private String s_day = "";
    private int minute;
    private int hours;
    private int date;
    private int months;
    private int years_now;
    private AlertDialog aDialog;
    private boolean logout = false;

    private TopicGridAdapter topicGridAdapter;
    private GridView itemTopic;

    private AdapterView adapterView;
    private int positionItem = -1;
    private int clickItemTopic = 1;
    private boolean remind = false;
    private boolean startedVocabulary = false;

    private AdView adView;
    private AdmodBanner admodBanner;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        vocabulary = new Vocabulary();

        //register gcm
        registryGCM();

        //Admod popub
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.popub_ad_unit_id));

        //goi method load qc
        requestNewInterstitial();

        //click close popub
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.d("LuanDT", "close popub---->load popbub khac");
                requestNewInterstitial();
                if(remind){
                    Log.d("LuanDT", "close popub createDialogRemind");
                    createDialogRemind();
                } else if (startedVocabulary) {
                    //start Vocabulary
                    Log.d("LuanDT", "close popub startVocabulary");
                    startVocabulary(adapterView, positionItem);
                }
                super.onAdClosed();
            }
        });
        //End Admod popub

        //Admod
        adView = (AdView) findViewById(R.id.adView);
        admodBanner = new AdmodBanner(adView);

        ///start up
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(HomeActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, alarmIntent, 0);
        reminTime = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_REMIN_TIME, (long) -1);

        displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dbController = DbController.getInstance(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView back = (ImageView) findViewById(R.id.drawer_indicator);
        resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));
        back.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });

        initView();
    }

    //load popub
    private void requestNewInterstitial(){
        Log.d("LuanDT", "load popub");
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        dateSetChange();
        testSyncVoca();
        Log.d("LuanDT", "onResume Home");
        super.onResume();
    }

    private void initView() {
        topic = new Topic(HomeActivity.this);
        //Menu
        listMenu = (ListView) findViewById(R.id.menu_list);
        createDataMenu();
        View menuHeader = LayoutInflater.from(HomeActivity.this).inflate(R.layout.menu_header, null);
        listMenu.addHeaderView(menuHeader);
        fullName = (TextView) menuHeader.findViewById(R.id.name);
        fullName.setText(SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_FULLNAME, ""));
        menuAdapter = new MenuAdapter(HomeActivity.this, arrMenu);
        listMenu.setAdapter(menuAdapter);

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    if (drawer.isDrawerVisible(START)) {
                        drawer.closeDrawer(START);
                    }
                } else if (i == 2) {
                    boolean isLogin = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_LOGIN, false);
                    if (isLogin) {
                        dateTimeSync();
                        String time = "" + s_hours + ":" + s_minute + " " + s_day + "/" + s_months + "/" + years_now;

                        //set is sync
                        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_SYNC, true);

                        //set time sync last
                        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_TIME_LAST_SYNC, time);

                        //update ui
                        ((MenuItem) arrMenu.get(1)).setValue(time);
                        menuAdapter.notifyDataSetChanged();

                        //goi method sync
                        try {
                            syncDelete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Intent intent = new Intent();
                        intent.setClass(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else if (i == 3) {
                    if(mInterstitialAd.isLoaded()){
                        Log.d("LuanDT", "show popub");
                        mInterstitialAd.show();
                        remind = true;
                    } else {
                        createDialogRemind();
                    }
                } else if (i == 4) {
                    Question question = new Question(HomeActivity.this);
                    if(question.checkData()>=2) {
                        Intent intent = new Intent();
                        intent.setClass(HomeActivity.this, QuestionGameActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(HomeActivity.this, getResources().getString(R.string.null_data), Toast.LENGTH_SHORT).show();
                    }
                } else if (i == 6) {
                    boolean isLogin = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_LOGIN, false);
                    if (isLogin) {
                        logout = true;

                        //goi method sync
                        try {
                            syncDelete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (i == 8){
                    Intent intent = new Intent(HomeActivity.this,AboutActivity.class);
                    startActivity(intent);
                } else if (i == 9){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com"));
                    startActivity(browserIntent);
                } else if (i == 10){
                    final String nameAccCompany = "ITPlus+Academy"; // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id="+ nameAccCompany)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + nameAccCompany)));
                    }
                }
            }
        });
        //End menu
        // get height and width one item
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        //GridTopic
        topicGridAdapter = new TopicGridAdapter(HomeActivity.this,topic);
        itemTopic = (GridView)findViewById(R.id.gvTopic);
        if ((metrics.widthPixels / metrics.scaledDensity) < 400) {
            itemTopic.setNumColumns(2);
        } else if ((metrics.widthPixels / metrics.scaledDensity) > 600) {
            itemTopic.setNumColumns(4);
        } else {
            itemTopic.setNumColumns(3);
        }
        itemTopic.setAdapter(topicGridAdapter);
        itemTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterView = parent;
                positionItem = position;

                if(clickItemTopic == 2) {
                    clickItemTopic = 1;
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        startedVocabulary = true;
                        Log.d("LuanDT", "show popub");
                    } else {
                        Log.d("LuanDT", "chua load xong ---> goi load lai");
                        startVocabulary(parent, position);
                    }

                } else {
                    Log.d("LuanDT", "chua duoc show popub");
                    clickItemTopic = clickItemTopic + 1;
                    startVocabulary(parent, position);
                }
            }
        });
        //Button Add
        add = (ImageView) findViewById(R.id.imgAddVoca);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(START)) {
                    drawer.closeDrawer(START);
                }
                createDialogAddTopic();
            }
        });
    }

    private void startVocabulary(AdapterView adapterView, int position){
        startedVocabulary = false;
        Topic topic = (Topic) adapterView.getAdapter().getItem(position);
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, VocabularyActivity.class);
        intent.putExtra("topic_id", topic.getId());
        intent.putExtra("topic_name", topic.getName());
        HomeActivity.this.startActivity(intent);
        ((Activity) HomeActivity.this).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    private void createDataMenu() {
        boolean isSync = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_SYNC, false);
        boolean isLogin = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_LOGIN, false);
        arrMenu = new ArrayList<Object>();
        arrMenu.add(new MenuItem("#03A9F4", R.drawable.ic_home, resources.getString(R.string.ev), ""));
        if (isSync) {
            arrMenu.add(new MenuItem("#53dd00", R.drawable.ic_refesh, resources.getString(R.string.sync), SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_TIME_LAST_SYNC, "-1")));
        } else {
            arrMenu.add(new MenuItem("#53dd00", R.drawable.ic_refesh, resources.getString(R.string.sync), resources.getString(R.string.off)));
        }
        arrMenu.add(new MenuItem("#ff6f00", R.drawable.ic_clock, resources.getString(R.string.reminds_study_time), resources.getString(R.string.off)));
        arrMenu.add(new MenuItem("#f50057", R.drawable.game, resources.getString(R.string.game), ""));
        arrMenu.add("");
        if (isLogin) {
            arrMenu.add(new MenuItem("#9c27b0", R.drawable.ic_logout, resources.getString(R.string.logout), ""));
        } else {
            arrMenu.add(new MenuItem("#9c27b0", R.drawable.ic_login, resources.getString(R.string.login), ""));
        }
        arrMenu.add("");
        arrMenu.add(new MenuItem("#6200ea", R.drawable.about, resources.getString(R.string.about), ""));
        arrMenu.add(new MenuItem("#1a237e", R.drawable.guide, resources.getString(R.string.guide_detail), ""));
        arrMenu.add(new MenuItem("#0d47a1", R.drawable.more_app, resources.getString(R.string.more_app), ""));

    }

    private void createDialogRemind() {
        remind = false;
        reminTime = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_REMIN_TIME, (long) -1);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(true);
        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_remind, null, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
//                    dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
        ToggleButton dStatus = (ToggleButton) dialogView.findViewById(R.id.status);
        final TimePicker dTime = (TimePicker) dialogView.findViewById(R.id.time_picker);
        final TextView dDisable = (TextView) dialogView.findViewById(R.id.disable);
        final Button dSave = (Button) dialogView.findViewById(R.id.save);
        dBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                if (modify) {
                    modify = false;
                    Toast.makeText(HomeActivity.this, resources.getString(R.string.dont_save), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (reminTime != -1) {
            dStatus.setChecked(true);
            dDisable.setVisibility(View.GONE);

            // lay gio
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminTime);
            dTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            dTime.setCurrentMinute(calendar.get(Calendar.MINUTE));
        } else {
            dStatus.setChecked(false);
            dDisable.setVisibility(View.VISIBLE);
        }
        dStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    modify = true;
                    dDisable.setVisibility(View.GONE);
                } else {
                    modify = false;
                    Keyboard.hideKeyboard(HomeActivity.this, dTime);
                    SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_REMIN_TIME, (long) -1);

                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    ((MenuItem) arrMenu.get(2)).setValue(resources.getString(R.string.off));
                    menuAdapter.notifyDataSetChanged();
                    dDisable.setVisibility(View.VISIBLE);
                }
            }
        });
        dSave.setVisibility(View.INVISIBLE);
        dSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modify = false;
                Calendar now = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, dTime.getCurrentHour());
                calendar.set(Calendar.MINUTE, dTime.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                if (calendar.before(now)) {
                    calendar.add(Calendar.DATE, 1);
                }
                long time = calendar.getTimeInMillis();
                startAlarm(time);
                updateTimeMenuItem(calendar);
                menuAdapter.notifyDataSetChanged();
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                Toast.makeText(HomeActivity.this, resources.getString(R.string.change_success), Toast.LENGTH_SHORT).show();
            }
        });
        dTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                modify = true;
                dSave.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createDialogAddTopic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(true);
        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_add_topic, null, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
//                    dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
        final EditText dText = (EditText) dialogView.findViewById(R.id.text);
        final ImageView dDelete = (ImageView) dialogView.findViewById(R.id.delete);
        Button dAdd = (Button) dialogView.findViewById(R.id.add);

        View.OnClickListener dOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.delete:
                        dText.setText("");
                        break;
                    case R.id.back:
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        break;
                    case R.id.add:
                        if (dText.getText().toString().trim().length() > 0) {
                            String name = dText.getText().toString();
                            name = name.replace("'", "\"");
                            int result = topic.insert(topic.maxId() + 1, name);
                            if (result == Topic.INSERT_SUCCESS) {
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();
                                topicGridAdapter.notifyDataSetChanged();
//                                listTopic.setSelection(topicAdapter.getCount() - 1);
                                itemTopic.setSelection(topicGridAdapter.getCount() - 1);
                                Toast.makeText(HomeActivity.this, resources.getString(R.string.added), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.INSERT_EXITS) {
                                Toast.makeText(HomeActivity.this, resources.getString(R.string.exits), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.INSERT_FALSE) {
                                Toast.makeText(HomeActivity.this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, resources.getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        dDelete.setOnClickListener(dOnClickListener);
        dBack.setOnClickListener(dOnClickListener);
        dAdd.setOnClickListener(dOnClickListener);
        Keyboard.showKeyboardDialog(alertDialog);
        dText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!dText.getText().toString().trim().equals("")) {
                    dDelete.setVisibility(View.VISIBLE);
                } else {
                    dDelete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void startAlarm(long time) {
        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_REMIN_TIME, time);
        //cancel alarm
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        // new alarm
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void dateSetChange() {
        topicGridAdapter.notifyDataSetChanged();
        long reminTime = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_REMIN_TIME, (long) -1);
        if (reminTime != -1) {
            // lay gio
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminTime);
            updateTimeMenuItem(calendar);
        } else {
            ((MenuItem) arrMenu.get(2)).setValue(resources.getString(R.string.off));
        }
        menuAdapter.notifyDataSetChanged();
    }

    private void updateTimeMenuItem(Calendar calendar) {
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
        ((MenuItem) arrMenu.get(2)).setValue(hour + ":" + minute);
    }

    public void testSyncVoca() {

        Log.d("LuanDT", "testSyncVoca");
        String[] id_delete = new String[0];
        String[] id_update = new String[0];
        String idDelete = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_VOCA_DELETE, "");
        String idUpdate = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_VOCA_UPDATE, "");

        if (!idDelete.equals("")) {
            id_delete = idDelete.split(",");
        }

        if (!idUpdate.equals("")) {
            id_update = idUpdate.split(",");
        }
        ArrayList<String> listIdDelete = new ArrayList<String>(Arrays.asList(id_delete));
        ArrayList<String> listIdUpdate = new ArrayList<String>(Arrays.asList(id_update));

        if (listIdDelete.size() > 0) {
            for (int i = 0; i < listIdDelete.size(); i++) {
                Log.d("LuanDT", "id_delete: " + listIdDelete.get(i));
                for (int j = 0; j < listIdUpdate.size(); j++) {
                    Log.d("LuanDT", "id_update: " + listIdUpdate.get(j));
                    if (listIdDelete.get(i).equals(listIdUpdate.get(j))) {
                        listIdUpdate.remove(j);
                    }
                }
            }
            Log.d("LuanDT", "listIdUpdate.size(): " + listIdUpdate.size());
        }

        StringBuffer newIdUpdate = new StringBuffer();
        for (int i = 0; i < listIdUpdate.size(); i++) {
            if (i == 0) {
                newIdUpdate = newIdUpdate.append(listIdUpdate.get(i));
            } else {
                newIdUpdate = newIdUpdate.append("," + listIdUpdate.get(i));
            }
        }

        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_VOCA_UPDATE, "" + newIdUpdate);

    }

    public void testSyncCate() {

        Log.d("LuanDT", "testSyncCate");
        String[] id_delete = new String[0];
        String[] id_update = new String[0];
        String idDelete = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_CATE_DELETE, "");
        String idUpdate = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_CATE_UPDATE, "");

        if (!idDelete.equals("")) {
            id_delete = idDelete.split(",");
        }

        if (!idUpdate.equals("")) {
            id_update = idUpdate.split(",");
        }

        ArrayList<String> listIdDelete = new ArrayList<String>(Arrays.asList(id_delete));
        ArrayList<String> listIdUpdate = new ArrayList<String>(Arrays.asList(id_update));

        if (listIdDelete.size() > 0) {
            for (int i = 0; i < listIdDelete.size(); i++) {
                Log.d("LuanDT", "id_delete_cate: " + listIdDelete.get(i));
                for (int j = 0; j < listIdUpdate.size(); j++) {
                    Log.d("LuanDT", "id_update_cate: " + listIdUpdate.get(j));
                    if (listIdDelete.get(i).equals(listIdUpdate.get(j))) {
                        listIdUpdate.remove(j);
                    }
                }
            }
            Log.d("LuanDT", "listIdUpdate_cate.size(): " + listIdUpdate.size());
        }

        StringBuffer newIdUpdate = new StringBuffer();
        for (int i = 0; i < listIdUpdate.size(); i++) {
            if (i == 0) {
                newIdUpdate = newIdUpdate.append(listIdUpdate.get(i));
            } else {
                newIdUpdate = newIdUpdate.append("," + listIdUpdate.get(i));
            }
        }

        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_CATE_UPDATE, "" + newIdUpdate);

    }

    //////////////excute///////////////////////////

    private void syncInsert() {
//        progressDialog();
        vocabulary.excuteInsert(HomeActivity.this, vocabulary.listVocabularyAdd(), new Vocabulary.OnLoadListener() {
            @Override
            public void onStart() {
//                progressDialog.show();
            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();

                //check logout
                if (logout) {
                    logout();
                    Toast.makeText(HomeActivity.this, resources.getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, resources.getString(R.string.sync_success), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, resources.getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                Log.d("LuanDT", "onFailure syncInsert");
            }
        });
    }

    private void syncUpdate() {
//        progressDialog();
        vocabulary.excuteUpdate(HomeActivity.this, vocabulary.listVocabularyUpdate(), new Vocabulary.OnLoadListener() {
            @Override
            public void onStart() {
//                progressDialog.show();
            }

            @Override
            public void onSuccess() {
//                progressDialog.dismiss();
                syncInsert();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, resources.getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                Log.d("LuanDT", "onFailure syncUpdate");
            }
        });
    }

    private void syncDelete() throws JSONException {
        progressDialog();
        vocabulary.excuteDelete(HomeActivity.this, listVocabularyDelete(), new Vocabulary.OnLoadListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess() {
                syncUpdate();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, resources.getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
                Log.d("LuanDT", "onFailure syncDelete");
            }
        });
    }

    //list vocabulary delete sync
    public JSONArray listVocabularyDelete() throws JSONException {

        String deleteCate = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_CATE_DELETE, "");
        String deleteVoca = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_VOCA_DELETE, "");

        JSONArray array = new JSONArray();
        JSONObject voca = new JSONObject();

        voca.put("table", "vocabularies");
        voca.put("sql", deleteVoca);

        array.put(voca);

        JSONObject voca1 = new JSONObject();

        voca1.put("table", "categories");
        voca1.put("sql", deleteCate);


        array.put(voca1);
        return array;
    }

    private ProgressDialog progressDialog() {
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getResources().getString(R.string.waiting));
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
//                    showDialogConfirmCancel();
                }
                return false;
            }
        });
        return progressDialog;
    }

    private void showDialogConfirmCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                HomeActivity.this);
        builder.setMessage(getResources().getString(R.string.cancel_sync));
        builder.setPositiveButton(getResources().getString(R.string.txt_ok),
                new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        aDialog.dismiss();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.txt_no),
                new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        aDialog.dismiss();
                    }
                });
        aDialog = builder.create();
        aDialog.show();
    }

    //date time sync
    public void dateTimeSync() {
        final Calendar c = Calendar.getInstance();
        minute = c.get(Calendar.MINUTE);
        hours = c.get(Calendar.HOUR_OF_DAY);
        date = c.get(Calendar.DATE);
        months = c.get(Calendar.MONTH);
        years_now = c.get(Calendar.YEAR);

        if (hours < 10) {
            s_hours = "0" + hours;
        } else {
            s_hours = "" + hours;
        }

        if (minute < 10) {
            s_minute = "0" + minute;
        } else {
            s_minute = "" + minute;
        }

        months = months + 1;

        if (months < 10) {
            s_months = "0" + months;
        } else {
            s_months = "" + months;
        }

        if (date < 10) {
            s_day = "0" + date;
        } else {
            s_day = "" + date;
        }

    }

    private void logout() {
        dbController.deleteAllDataTable();
        dateSetChange();
        SPUtil.instance(HomeActivity.this).logout();
        SPUtil.instance(HomeActivity.this).set(SPUtil.KEY_AGREE_RUN, true);

        fullName.setText("");

        ((MenuItem) arrMenu.get(1)).setValue(resources.getString(R.string.off));
        ((MenuItem) arrMenu.get(2)).setValue(resources.getString(R.string.off));
        ((MenuItem) arrMenu.get(5)).setTitle(resources.getString(R.string.login));
        ((MenuItem) arrMenu.get(5)).setIcon(R.drawable.ic_login);
        menuAdapter.notifyDataSetChanged();
    }

    //gcm
    AsyncTask<Void, Void, Void> mRegisterTask;

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message depending upon your app
             * requirement For now i am just displaying it on the screen
             * */
            WakeLocker.release();
        }
    };

    private void registryGCM() {
        GCMRegistrar.checkDevice(getApplicationContext());
        GCMRegistrar.checkManifest(getApplicationContext());

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));

        final String regId = GCMRegistrar
                .getRegistrationId(getApplicationContext());

        if (regId.equals("")) {
            GCMRegistrar.register(getApplicationContext(), CommonUtilities.SENDER_ID);
        } else {
            if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
            } else {
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        ServerUtilities
                                .register(getApplicationContext(), regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(getApplicationContext());
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}
