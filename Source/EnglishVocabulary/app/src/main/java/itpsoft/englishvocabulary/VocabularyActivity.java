package itpsoft.englishvocabulary;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import itpsoft.englishvocabulary.adapter.VocabularyAdapter;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.Keyboard;
import itpsoft.englishvocabulary.ultils.SpeakEnglish;


public class VocabularyActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {

    private ActionBar actionBar;
    private VocabularyAdapter adapter;
    private ArrayList<Vocabulary> listVocabulary;
    private ListView listView;
    private Vocabulary vocabulary;
    public static int idTopic;
    private long idVoca;
    private int positionItemSelected;
    private String topicName;
    private EditText edtEnglish, edtVietnamese;
    private Button btnAddVoca, btnCancel, btnUpdate, btnDelete;
    private LinearLayout control_btn_update, layout_en, layout_vi;
    private TextToSpeech textToSpeech;
    private SpeakEnglish speakEnglish;
    private ImageView imgSoundEdt, imgClearEdtEnglish, imgClearEdtVn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        textToSpeech = new TextToSpeech(VocabularyActivity.this, this);
        speakEnglish = new SpeakEnglish(VocabularyActivity.this, textToSpeech);

        Intent intent = getIntent();
        idTopic = intent.getIntExtra("topic_id", 0);
        topicName = intent.getStringExtra("topic_name");

        edtEnglish = (EditText) findViewById(R.id.edtEnglish);
        edtVietnamese = (EditText) findViewById(R.id.edtVietnamese);

        control_btn_update = (LinearLayout) findViewById(R.id.control_btn_update);
        layout_en = (LinearLayout) findViewById(R.id.layout_en);
        layout_vi = (LinearLayout) findViewById(R.id.layout_vi);

        btnAddVoca = (Button) findViewById(R.id.btnAddVoca);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        imgSoundEdt = (ImageView) findViewById(R.id.imgEdtSound);
        imgClearEdtEnglish = (ImageView) findViewById(R.id.clearEdtEnglish);
        imgClearEdtVn = (ImageView) findViewById(R.id.clearEdtVn);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBar)));
        actionBar.setTitle(topicName);

        listView = (ListView) findViewById(R.id.listVocabulary);
        vocabulary = new Vocabulary();
        listVocabulary = vocabulary.initListVocabulary(idTopic);
        adapter = new VocabularyAdapter(VocabularyActivity.this, listVocabulary);
        listView.setAdapter(adapter);

        //click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                speakEnglish.speakOut(((Vocabulary) adapterView.getAdapter().getItem(i)).getEnglish());
            }
        });

        //long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                positionItemSelected = i;
                idVoca = ((Vocabulary) adapterView.getAdapter().getItem(i)).getId();
                edtEnglish.setText(((Vocabulary) adapterView.getAdapter().getItem(i)).getEnglish());
                edtVietnamese.setText(((Vocabulary) adapterView.getAdapter().getItem(i)).getVietnamese());
                control_btn_update.setVisibility(View.VISIBLE);
                btnAddVoca.setVisibility(View.GONE);

                if (edtEnglish.getText().toString() != "") {
                    imgClearEdtEnglish.setVisibility(View.VISIBLE);
                    imgSoundEdt.setVisibility(View.VISIBLE);
                }

                if (edtVietnamese.getText().toString() != "") {
                    imgClearEdtEnglish.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        //add vocabulary
        btnAddVoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String en = edtEnglish.getText().toString();
                String vi = edtVietnamese.getText().toString();

                if (validate()) {
                    int result = vocabulary.addVocabulary(idTopic, en, vi);
                    if (result == Vocabulary.INSERT_SUCCESS) {
                        clearEdt();
                        adapter.notifyDataSetChanged();
                        listView.setSelection(adapter.getCount() - 1);
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.added), Toast.LENGTH_SHORT).show();
                    } else if (result == Vocabulary.INSERT_EXITS) {
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.exits_voca), Toast.LENGTH_SHORT).show();
                    } else if (result == Vocabulary.INSERT_FALSE) {
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control_btn_update.setVisibility(View.GONE);
                btnAddVoca.setVisibility(View.VISIBLE);

                clearEdt();
            }
        });

        //update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String en = edtEnglish.getText().toString();
                String vi = edtVietnamese.getText().toString();
                if(validate()){
                    int result = vocabulary.updateVocabulary(idVoca, en, vi);
                    if (result == Vocabulary.EDIT_SUCCESS) {
                        control_btn_update.setVisibility(View.GONE);
                        btnAddVoca.setVisibility(View.VISIBLE);
                        clearEdt();
                        adapter.notifyDataSetChanged();
                        listView.setSelection(positionItemSelected);
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.edited), Toast.LENGTH_SHORT).show();
                    } else if (result == Vocabulary.EDIT_EXITS) {
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.exits_voca), Toast.LENGTH_SHORT).show();
                    } else if (result == Vocabulary.EDIT_FALSE) {
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    int result = vocabulary.delete(idVoca);
                    if (result == Vocabulary.DELETE_SUCCESS) {
                        control_btn_update.setVisibility(View.GONE);
                        btnAddVoca.setVisibility(View.VISIBLE);
                        clearEdt();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    } else if (result == Vocabulary.DELETE_FALSE) {
                        Toast.makeText(VocabularyActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //click icon sound in edittext
        imgSoundEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakEnglish.speakOut(edtEnglish.getText().toString());
            }
        });

        //click icon clear edittext english
        imgClearEdtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEnglish.setText("");
            }
        });

        //click icon clear edittext tieng viet
        imgClearEdtVn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtVietnamese.setText("");
            }
        });

        //edittext english
        edtEnglish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtEnglish.getText().toString().trim().equals("")) {
                    imgClearEdtEnglish.setVisibility(View.VISIBLE);
                    imgSoundEdt.setVisibility(View.VISIBLE);
                } else {
                    imgClearEdtEnglish.setVisibility(View.GONE);
                    imgSoundEdt.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //edittext vietnamese
        edtVietnamese.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!edtVietnamese.getText().toString().trim().equals("")){
                    imgClearEdtVn.setVisibility(View.VISIBLE);
                } else {
                    imgClearEdtVn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void clearEdt() {
        edtEnglish.setText("");
        edtVietnamese.setText("");
        edtEnglish.requestFocus();
    }

    //validate
    public boolean validate(){
        boolean check = false;
        if(TextUtils.isEmpty(edtEnglish.getText())){
            layout_en.startAnimation(
                    AnimationUtils.loadAnimation(VocabularyActivity.this, R.anim.shake));
            edtEnglish.setError(getResources().getString(R.string.empty));
            edtEnglish.requestFocus();
            Keyboard.showKeyboard(this, edtEnglish);
            return false;
        } else if(TextUtils.isEmpty(edtVietnamese.getText())){
            layout_vi.startAnimation(
                    AnimationUtils.loadAnimation(VocabularyActivity.this, R.anim.shake));
            edtVietnamese.setError(getResources().getString(R.string.empty));
            edtVietnamese.requestFocus();
            Keyboard.showKeyboard(this, edtVietnamese);
            return false;
        } else {
            check = true;
        }

        return check;
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_vocabulary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int i) {

    }
}