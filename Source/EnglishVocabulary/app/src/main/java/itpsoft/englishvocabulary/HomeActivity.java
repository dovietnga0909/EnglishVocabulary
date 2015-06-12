package itpsoft.englishvocabulary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import itpsoft.englishvocabulary.adapter.MenuAdapter;
import itpsoft.englishvocabulary.adapter.TopicAdapter;
import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.models.MenuItem;
import itpsoft.englishvocabulary.models.Topic;
import itpsoft.englishvocabulary.ultils.Log;
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
    private ArrayList<Topic> arrTopic;
    private TopicAdapter topicAdapter;
    private ImageView add;
    private AlertDialog alertDialog;
    private Rect displayRectangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ///start up
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

    private void initView() {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setCancelable(true);
                    View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_remind, null, false);
                    dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
//                    dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
                    builder.setView(dialogView);
                    alertDialog = builder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.setCanceledOnTouchOutside(true);

                    ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
                    ToggleButton dStatus = (ToggleButton) dialogView.findViewById(R.id.status);
                    TimePicker dTime = (TimePicker) dialogView.findViewById(R.id.time_picker);
                    final TextView dDisable = (TextView) dialogView.findViewById(R.id.disable);
                    dBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (alertDialog.isShowing())
                                alertDialog.dismiss();
                        }
                    });
                    dStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                dDisable.setVisibility(View.GONE);
                            } else {
                                dDisable.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    alertDialog.show();
                } else if (i == 5) {

                }
            }
        });
        //End menu
        //Topic
        createDataTopic();
        listTopic = (ListView) findViewById(R.id.listTopic);
        topicAdapter = new TopicAdapter(HomeActivity.this, arrTopic);
//        View headerEmpty = LayoutInflater.from(HomeActivity.this).inflate(R.layout.empty_list, listTopic, false);
//        View footerEmpty = LayoutInflater.from(HomeActivity.this).inflate(R.layout.empty_list, listTopic, false);
//        listTopic.addHeaderView(headerEmpty);
//        listTopic.addFooterView(footerEmpty);
        listTopic.setAdapter(topicAdapter);
        listTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), VocabularyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                Toast.makeText(HomeActivity.this, "" + i, Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setCancelable(true);
                View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_add_topic, null, false);
                dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
//                    dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(false);
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
                                Toast.makeText(HomeActivity.this, dText.getText().toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                dDelete.setOnClickListener(dOnClickListener);
                dBack.setOnClickListener(dOnClickListener);
                dAdd.setOnClickListener(dOnClickListener);
                dText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (i2 > 0) {
                            dDelete.setVisibility(View.VISIBLE);
                        } else {
                            dDelete.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    private void createDataMenu() {
        arrMenu = new ArrayList<Object>();
        arrMenu.add(new MenuItem("#00FFFF", R.drawable.ic_logout, resources.getString(R.string.ev), ""));
        arrMenu.add(new MenuItem("#FF0000", R.drawable.ic_logout, resources.getString(R.string.sync), resources.getString(R.string.off)));
        arrMenu.add(new MenuItem("#FF00FF", R.drawable.ic_logout, resources.getString(R.string.reminds_study_time), resources.getString(R.string.off)));
        arrMenu.add("");
        arrMenu.add(new MenuItem("#00FF00", R.drawable.ic_logout, resources.getString(R.string.logout), ""));
    }

    private void createDataTopic() {
        arrTopic = new ArrayList<Topic>();
        arrTopic.add(new Topic("#F60000", 1, "Test topic", 22));
        arrTopic.add(new Topic("#ff9000", 1, "Test topic 1", 30));
        arrTopic.add(new Topic("#F60000", 1, "Test topic 2", 50));
        arrTopic.add(new Topic("#ff9000", 1, "Test topic 3", 99));
        arrTopic.add(new Topic("#F60000", 1, "Test topic 4", 100));
        arrTopic.add(new Topic("#ff9000", 1, "Test topic 5", 96));
        arrTopic.add(new Topic("#F60000", 1, "Test topic 6", 69));
        arrTopic.add(new Topic("#F60000", 1, "Test topic 7", 831));
    }
}
