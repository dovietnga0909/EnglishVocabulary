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
    private Topic topic;

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
//        arrTopic = topic.getAll();
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
                                if(dText.getText().toString().trim().length()>0) {
                                    boolean result = topic.insert(dText.getText().toString());
                                    if (result) {
                                        if (alertDialog.isShowing())
                                            alertDialog.dismiss();
                                        topicAdapter.notifyDataSetChanged();
                                        listTopic.setSelection(topicAdapter.getCount() - 1);
                                        Toast.makeText(HomeActivity.this, resources.getString(R.string.added), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(HomeActivity.this, resources.getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                                }
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
                        if (i > 0) {
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
        arrMenu.add(new MenuItem("#03A9F4", R.drawable.ic_home, resources.getString(R.string.ev), ""));
        arrMenu.add(new MenuItem("#53dd00", R.drawable.ic_refesh, resources.getString(R.string.sync), resources.getString(R.string.off)));
        arrMenu.add(new MenuItem("#ff6f00", R.drawable.ic_clock, resources.getString(R.string.reminds_study_time), resources.getString(R.string.off)));
        arrMenu.add("");
        arrMenu.add(new MenuItem("#03A9F4", R.drawable.ic_logout, resources.getString(R.string.logout), ""));
    }
}
