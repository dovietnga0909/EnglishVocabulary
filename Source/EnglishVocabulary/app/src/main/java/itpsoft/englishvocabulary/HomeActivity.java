package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import itpsoft.englishvocabulary.adapter.MenuAdapter;
import itpsoft.englishvocabulary.adapter.TopicAdapter;
import itpsoft.englishvocabulary.alarm.AlarmReceiver;
import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.models.MenuItem;
import itpsoft.englishvocabulary.models.Topic;
import itpsoft.englishvocabulary.ultils.Keyboard;
import itpsoft.englishvocabulary.ultils.SPUtil;
import itpsoft.englishvocabulary.view.DrawerArrowDrawable;

import static android.view.Gravity.START;

public class HomeActivity extends Activity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private DrawerLayout drawer;
    private Resources resources;
    private float offset;
    private boolean flipped;
    private ListView listMenu, listTopic;
    private ArrayList<Object> arrMenu;
    private MenuAdapter menuAdapter;
    private TopicAdapter topicAdapter;
    private ImageView add;
    private AlertDialog alertDialog;
    private Rect displayRectangle;
    private Topic topic;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private int intervalTime = 1000 * 60 * 60 * 24;
    private long reminTime = -1;
    private boolean modify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ///start up
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(HomeActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, alarmIntent, 0);
        reminTime = SPUtil.instance(HomeActivity.this).get(SPUtil.KEY_REMIN_TIME, (long) -1);

        displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        DbController dbController = DbController.getInstance(this);

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

    @Override
    protected void onResume() {
        dateSetChange();
        super.onResume();
    }

    private void initView() {
        topic = new Topic(HomeActivity.this);
        //Menu
        listMenu = (ListView) findViewById(R.id.menu_list);
        createDataMenu();
        View menuHeader = LayoutInflater.from(HomeActivity.this).inflate(R.layout.menu_header, null);
        listMenu.addHeaderView(menuHeader);
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

                } else if (i == 3) {
                    createDialogRemind();
                } else if (i == 5) {

                }
            }
        });
        //End menu
        //Topic
        listTopic = (ListView) findViewById(R.id.listTopic);
        topicAdapter = new TopicAdapter(HomeActivity.this, topic);
        listTopic.setAdapter(topicAdapter);
        listTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Topic itemSelect = (Topic) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), VocabularyActivity.class);
                intent.putExtra("topic_id", itemSelect.getId());
                intent.putExtra("topic_name", itemSelect.getName());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            }
        });
        listTopic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Topic topic = (Topic) adapterView.getAdapter().getItem(i);
                createDialogTopic(topic);
                return true;
            }
        });
        //End topic
        //Button Add
        add = (ImageView) findViewById(R.id.add);
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

    private void createDataMenu() {
        arrMenu = new ArrayList<Object>();
        arrMenu.add(new MenuItem("#03A9F4", R.drawable.ic_home, resources.getString(R.string.ev), ""));
        arrMenu.add(new MenuItem("#53dd00", R.drawable.ic_refesh, resources.getString(R.string.sync), resources.getString(R.string.off)));
        arrMenu.add(new MenuItem("#ff6f00", R.drawable.ic_clock, resources.getString(R.string.reminds_study_time), resources.getString(R.string.off)));
        arrMenu.add("");
        arrMenu.add(new MenuItem("#03A9F4", R.drawable.ic_logout, resources.getString(R.string.logout), ""));
    }

    private void createDialogRemind() {
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
                            int result = topic.insert(topic.maxId() + 1, dText.getText().toString());
                            if (result == Topic.INSERT_SUCCESS) {
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();
                                topicAdapter.notifyDataSetChanged();
                                listTopic.setSelection(topicAdapter.getCount() - 1);
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

    private void createDialogTopic(final Topic t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(true);
        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_menu, null, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ListView dList = (ListView) dialogView.findViewById(R.id.menu);
        ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
        TextView dTitle = (TextView) dialogView.findViewById(R.id.title);

        dTitle.setText(t.getName());
        dBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        });
        String arr[] = {resources.getString(R.string.rename), resources.getString(R.string.delete)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, arr);
        dList.setAdapter(adapter);
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                switch (i) {
                    case 0:
                        createDialogRenameTopic(t);
                        break;
                    case 1:
                        createDialogDeleteTopic(t);
                        break;
                }
            }
        });
    }

    private void createDialogRenameTopic(final Topic t) {
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
        TextView dTitle = (TextView) dialogView.findViewById(R.id.title);
        final EditText dText = (EditText) dialogView.findViewById(R.id.text);
        final ImageView dDelete = (ImageView) dialogView.findViewById(R.id.delete);
        Button dEdit = (Button) dialogView.findViewById(R.id.add);

        dTitle.setText(resources.getString(R.string.rename));
        dEdit.setText(resources.getString(R.string.save));
        dText.setHint(resources.getString(R.string.insert_new_name));
        dText.append(t.getName());
        dText.setEllipsize(TextUtils.TruncateAt.END);
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
                            int result = topic.rename(t, dText.getText().toString());
                            if (result == Topic.EDIT_SUCCESS) {
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();
                                topicAdapter.notifyDataSetChanged();
                                Toast.makeText(HomeActivity.this, resources.getString(R.string.edited), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.EDIT_SAME) {
                                Toast.makeText(HomeActivity.this, resources.getString(R.string.same), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.EDIT_EXITS) {
                                Toast.makeText(HomeActivity.this, resources.getString(R.string.exits), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.EDIT_FALSE) {
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
        dEdit.setOnClickListener(dOnClickListener);
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

    private void createDialogDeleteTopic(final Topic t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(true);
        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_confirm_delete, null, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
        TextView dContent = (TextView) dialogView.findViewById(R.id.content);
        Button dDelete = (Button) dialogView.findViewById(R.id.delete);

        View.OnClickListener dOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.back:
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        break;
                    case R.id.delete:
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        int result = topic.delete(t);
                        if (result == Topic.INSERT_SUCCESS) {
                            topicAdapter.notifyDataSetChanged();
                            Toast.makeText(HomeActivity.this, resources.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        dDelete.setOnClickListener(dOnClickListener);
        dBack.setOnClickListener(dOnClickListener);
        dContent.setText(resources.getString(R.string.really_delete) + " " + t.getName());
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
        topicAdapter.notifyDataSetChanged();
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
}
