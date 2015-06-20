package itpsoft.englishvocabulary.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.TestActivity;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.SpeakEnglish;


/**
 * Created by Do on 05/06/2015.
 */
public class ListenFragment extends Fragment implements TextToSpeech.OnInitListener {

    private TextView txtVietnamese,txtQuestion,txtTotal,txtNumTrue,txtNumSkip;
    private EditText edtAnswers;
    private ImageView imgIcDeleteTxt;
    private CheckBox ckSuggest;
    private Button btnAnswers,btnRepeat,btnSkip;
    private TextToSpeech textToSpeech;
//    private Context mContext;
    private ArrayList<Vocabulary> listVocabularys;
    private SpeakEnglish speakEnglish;

    private static int LISTEN_POS_VOCABULARY    = 0;
    private static int LISTEN_NUM_QUESTION      = 0;
    private static int LISTEN_NUM_SKIP          = 0;
    private static int LISTEN_NUM_TRUE          = 0;

    private String strEnglish,strVietnamese;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_test_listen, container, false);

        //hide keyboard firstTime
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scrContentTestListen);
        //TextView
        txtVietnamese   = (TextView)rootView.findViewById(R.id.txtVietnamese);
            //hide txtVietnamese
            txtVietnamese.setVisibility(View.INVISIBLE);
        txtQuestion         = (TextView)rootView.findViewById(R.id.txtQuestions);
        txtTotal        = (TextView)rootView.findViewById(R.id.txtTotal);
        txtNumTrue      = (TextView)rootView.findViewById(R.id.txt_num_true);
        txtNumSkip      = (TextView)rootView.findViewById(R.id.txt_num_skip);
        //EditText
        edtAnswers      = (EditText)rootView.findViewById(R.id.edtAnswers);
        //ImageView
        imgIcDeleteTxt  = (ImageView)rootView.findViewById(R.id.imgIcDeleteTxt);
        //Checkbox
        ckSuggest       = (CheckBox)rootView.findViewById(R.id.ckSuggest);
        //Button
        btnAnswers      = (Button)rootView.findViewById(R.id.btnAnswer);
        btnRepeat       = (Button)rootView.findViewById(R.id.btnRepeat);
        btnSkip         = (Button)rootView.findViewById(R.id.btnSkip);
        //TextToSpeech
        textToSpeech = new TextToSpeech(getActivity(), this);
//        textToSpeech = new TextToSpeech(getActivity(),getActivity().);
        //speakEnglish
        speakEnglish    = new SpeakEnglish(getActivity(), textToSpeech);
        //Vocabulary
        listVocabularys = new TestActivity().getListVocabularies();

        txtTotal.setText(Integer.toString(listVocabularys.size()));

        ckSuggest.setEnabled(false);
        //set Visible txtVietnamese khi duoc check
        ckSuggest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ckSuggest.isChecked()) {
                    txtVietnamese.setVisibility(View.VISIBLE);
                } else {
                    txtVietnamese.setVisibility(View.INVISIBLE);
                }
            }
        });
        //set imgIcDeleteTxt Gone When edtAnswers = ""
//        imgIcDeleteTxt.setVisibility(View.GONE);

        edtAnswers.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!edtAnswers.getText().toString().trim().equals("")) {
                    imgIcDeleteTxt.setVisibility(View.VISIBLE);
                } else {
                    imgIcDeleteTxt.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        // Delete edtAnswers
        imgIcDeleteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtAnswers.setText("");
            }
        });



        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LISTEN_NUM_QUESTION++;

                Collections.shuffle(listVocabularys);
                ckSuggest.setEnabled(true);
                btnRepeat.setText(getResources().getString(R.string.txt_btn_repeat));

                txtVietnamese.setText(listVocabularys.get(LISTEN_POS_VOCABULARY).getVietnamese());
                txtQuestion.setText(Integer.toString(LISTEN_NUM_QUESTION));
                speakEnglish.speakOut(listVocabularys.get(LISTEN_POS_VOCABULARY).getEnglish());
                btnRepeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        speakEnglish.speakOut(listVocabularys.get(LISTEN_POS_VOCABULARY).getEnglish());

                    }
                });
                btnAnswers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(edtAnswers.getText().toString().toLowerCase().trim().equals(listVocabularys.get(LISTEN_POS_VOCABULARY).getEnglish().toString().toLowerCase())){
                            if(LISTEN_POS_VOCABULARY == listVocabularys.size()-1){

                                final AlertDialog.Builder mDialog =  new AlertDialog.Builder(getActivity());

                                mDialog.setTitle(getResources().getString(R.string.txt_completet));
                                mDialog.setMessage(getResources().getString(R.string.txt_msg_part1)
                                        + LISTEN_NUM_TRUE + getResources().getString(R.string.txt_msg_part2)
                                        + listVocabularys.size() + getResources().getString(R.string.txt_msg_part3)
                                        + getResources().getString(R.string.txt_msg_part4));
                                mDialog.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                mDialog.setNegativeButton(getResources().getString(R.string.txt_no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                });
                                mDialog.setCancelable(false);
                                mDialog.show();
                                Toast.makeText(getActivity(),"Ban da hoan thanh",Toast.LENGTH_SHORT).show();
                                LISTEN_POS_VOCABULARY   = 0;
                                LISTEN_NUM_QUESTION     = 1;
                                LISTEN_NUM_TRUE         = 0;
                                LISTEN_NUM_SKIP         = 0;

                                txtQuestion.setText(Integer.toString(LISTEN_NUM_QUESTION));
                                txtNumTrue.setText(Integer.toString(LISTEN_NUM_TRUE));
                                txtVietnamese.setText(listVocabularys.get(LISTEN_POS_VOCABULARY).getVietnamese());
                                txtNumSkip.setText(Integer.toString(LISTEN_NUM_SKIP));



                            }else {
                                LISTEN_POS_VOCABULARY++;
                                LISTEN_NUM_QUESTION++;
                                LISTEN_NUM_TRUE++;

                                txtQuestion.setText(Integer.toString(LISTEN_NUM_QUESTION));
                                txtNumTrue.setText(Integer.toString(LISTEN_NUM_TRUE));
                                txtVietnamese.setText(listVocabularys.get(LISTEN_POS_VOCABULARY).getVietnamese());
                                edtAnswers.setText("");
                                speakEnglish.speakOut(listVocabularys.get(LISTEN_POS_VOCABULARY).getEnglish());
                            }


                            Log.d("NgaDV","LISTEN_POS_VOCABULARY  =  "+LISTEN_POS_VOCABULARY);


                            Log.d("NgaDV", "LISTEN_POS_VOCABULARY = " + LISTEN_POS_VOCABULARY);
                        }
                        else {
                           //
                            Toast.makeText(getActivity(),"sai",Toast.LENGTH_SHORT).show();
                            edtAnswers.startAnimation(
                                    AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                            speakEnglish.speakOut(listVocabularys.get(LISTEN_POS_VOCABULARY).getEnglish());
                        }
                    }
                });

                btnSkip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(LISTEN_POS_VOCABULARY == listVocabularys.size()-1){

                            final AlertDialog.Builder mDialog =  new AlertDialog.Builder(getActivity());

                            mDialog.setTitle(getResources().getString(R.string.txt_completet));
                            mDialog.setMessage(getResources().getString(R.string.txt_msg_part1)
                                    + LISTEN_NUM_TRUE + getResources().getString(R.string.txt_msg_part2)
                                    + listVocabularys.size() + getResources().getString(R.string.txt_msg_part3)
                                    + getResources().getString(R.string.txt_msg_part4));
                            mDialog.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            mDialog.setNegativeButton(getResources().getString(R.string.txt_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            });
                            mDialog.setCancelable(false);
                            mDialog.show();
                            Toast.makeText(getActivity(),"Ban da hoan thanh",Toast.LENGTH_SHORT).show();
                            LISTEN_POS_VOCABULARY   = 0;
                            LISTEN_NUM_QUESTION     = 1;
                            LISTEN_NUM_TRUE         = 0;
                            LISTEN_NUM_SKIP         = 0;

                            txtQuestion.setText(Integer.toString(LISTEN_NUM_QUESTION));
                            txtNumTrue.setText(Integer.toString(LISTEN_NUM_TRUE));
                            txtVietnamese.setText(listVocabularys.get(LISTEN_POS_VOCABULARY).getVietnamese());
                            txtNumSkip.setText(Integer.toString(LISTEN_NUM_SKIP));


                        }else {
                            LISTEN_POS_VOCABULARY++;
                            LISTEN_NUM_QUESTION++;
                            LISTEN_NUM_SKIP++;

                            txtQuestion.setText(Integer.toString(LISTEN_NUM_QUESTION));
                            txtNumTrue.setText(Integer.toString(LISTEN_NUM_TRUE));
                            txtNumSkip.setText(Integer.toString(LISTEN_NUM_SKIP));
                            txtVietnamese.setText(listVocabularys.get(LISTEN_POS_VOCABULARY).getVietnamese());
                            speakEnglish.speakOut(listVocabularys.get(LISTEN_POS_VOCABULARY).getEnglish());
                        }
                    }
                });


            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        Log.d("NgaDV","onDestroy");
        LISTEN_NUM_TRUE = 0;
        LISTEN_NUM_QUESTION = 0;
        LISTEN_POS_VOCABULARY = 0;
        LISTEN_NUM_SKIP = 0;
        super.onDestroy();
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {

    }
}
