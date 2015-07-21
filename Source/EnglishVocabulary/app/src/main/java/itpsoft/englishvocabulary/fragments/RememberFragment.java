package itpsoft.englishvocabulary.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.TestActivity;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.Keyboard;
import itpsoft.englishvocabulary.ultils.Log;

/**
 * Created by Do on 05/06/2015.
 */
public class RememberFragment extends Fragment {

    private TextView txtEnglish,txtQuestion,txtTotal,txtNumTrue,txtNumSkip;
    private EditText edtAnswers;
    private ImageView imgIcDeleteTxt, imgIcBack;
    private Button btnAnswers,btnSkip;

    private ArrayList<Vocabulary> listVocabularys;
    private Vocabulary vocabulary;
    private static int REMEMBER_POS_VOCABULARY  = 0;
    private static int REMEMBER_NUM_QUESTION    = 0;
    private static int REMEMBER_NUM_TOTAL       = 0;
    private static int REMEMBER_NUM_SKIP        = 0;
    private static int REMEMBER_NUM_TRUE        = 0;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_test_remember,container,false);

//        imgIcBack = (ImageView) rootView.findViewById(R.id.drawer_indicator);
//        imgIcBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
//            }
//        });
        //TextView
        txtEnglish      = (TextView)rootView.findViewById(R.id.txtEnglish);
        //hide txtVietnamese
        txtEnglish.setVisibility(View.INVISIBLE);
        txtQuestion     = (TextView)rootView.findViewById(R.id.txtQuestions);
        txtTotal        = (TextView)rootView.findViewById(R.id.txtTotal);
        txtNumTrue      = (TextView)rootView.findViewById(R.id.txt_num_true);
        txtNumSkip      = (TextView)rootView.findViewById(R.id.txt_num_skip);
        //EditText
        edtAnswers      = (EditText) rootView.findViewById(R.id.edtAnswers);
        //ImageView
        imgIcDeleteTxt  =(ImageView)rootView.findViewById(R.id.imgIcDeleteTxt);

        //Button
        btnAnswers      = (Button)rootView.findViewById(R.id.btnAnswer);
        btnSkip         = (Button)rootView.findViewById(R.id.btnSkip);

        //Vocabulary
        listVocabularys = new TestActivity().getListVocabularies();
        Log.d("NgaDV", "list vocabulary size() RememberFragment : " + listVocabularys.size());



        txtTotal.setText(Integer.toString(listVocabularys.size()));
        //set imgIcDeleteTxt Gone When edtAnswers = ""
//        imgIcDeleteTxt.setVisibility(View.GONE);

        edtAnswers.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!edtAnswers.getText().toString().trim().equals("")){
                    imgIcDeleteTxt.setVisibility(View.VISIBLE);
                }else {
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


        imgIcDeleteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtAnswers.setText("");
            }
        });

        btnAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Keyboard.showKeyboard(getActivity(), edtAnswers);

                REMEMBER_NUM_QUESTION++;
                txtQuestion.setText(Integer.toString(REMEMBER_NUM_QUESTION));
                btnAnswers.setText(getResources().getString(R.string.txt_btn_answers));
                txtEnglish.setVisibility(View.VISIBLE);
                txtEnglish.setText(listVocabularys.get(REMEMBER_POS_VOCABULARY).getEnglish());

                btnAnswers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtAnswers.getText().toString().trim().toLowerCase().equals(listVocabularys.get(REMEMBER_POS_VOCABULARY).getVietnamese().toLowerCase())) {
                            if (REMEMBER_POS_VOCABULARY == listVocabularys.size() - 1) {
                                final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());

                                mDialog.setTitle(getResources().getString(R.string.txt_completet));
                                mDialog.setMessage(getResources().getString(R.string.txt_msg_part1)
                                        + REMEMBER_NUM_TRUE + getResources().getString(R.string.txt_msg_part2)
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

                                REMEMBER_POS_VOCABULARY = 0;
                                REMEMBER_NUM_SKIP = 0;
                                REMEMBER_NUM_QUESTION = 1;
                                REMEMBER_NUM_TRUE = 0;

                                Toast.makeText(getActivity(), "Ban da hoan thanh", Toast.LENGTH_SHORT).show();
                                txtQuestion.setText(Integer.toString(REMEMBER_NUM_QUESTION));
                                txtNumTrue.setText(Integer.toString(REMEMBER_NUM_TRUE));
                                txtEnglish.setText(listVocabularys.get(REMEMBER_POS_VOCABULARY).getEnglish());
                                txtNumSkip.setText(Integer.toString(REMEMBER_NUM_SKIP));
                            } else {
                                REMEMBER_POS_VOCABULARY++;
                                REMEMBER_NUM_QUESTION++;
                                REMEMBER_NUM_TRUE++;

                                txtQuestion.setText(Integer.toString(REMEMBER_NUM_QUESTION));
                                txtNumTrue.setText(Integer.toString(REMEMBER_NUM_TRUE));
                                txtEnglish.setText(listVocabularys.get(REMEMBER_POS_VOCABULARY).getEnglish());

                                edtAnswers.setText("");
                            }

                        } else {
                            edtAnswers.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                            txtEnglish.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                        }



                    }
                });

                btnSkip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (REMEMBER_POS_VOCABULARY == listVocabularys.size()-1) {

                            final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());

                            mDialog.setTitle(getResources().getString(R.string.txt_completet));
                            mDialog.setMessage(getResources().getString(R.string.txt_msg_part1)
                                    + REMEMBER_NUM_TRUE + getResources().getString(R.string.txt_msg_part2)
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

                            REMEMBER_POS_VOCABULARY = 0;
                            REMEMBER_NUM_SKIP       = 0;
                            REMEMBER_NUM_QUESTION   = 1;
                            REMEMBER_NUM_TRUE       = 0;

                            Toast.makeText(getActivity(),"Ban da hoan thanh",Toast.LENGTH_SHORT).show();
                            txtQuestion.setText(Integer.toString(REMEMBER_NUM_QUESTION));
                            txtNumTrue.setText(Integer.toString(REMEMBER_NUM_TRUE));
                            txtEnglish.setText(listVocabularys.get(REMEMBER_POS_VOCABULARY).getEnglish());
                            txtNumSkip.setText(Integer.toString(REMEMBER_NUM_SKIP));
                        }else {
                            REMEMBER_POS_VOCABULARY++;
                            REMEMBER_NUM_QUESTION++;
                            REMEMBER_NUM_SKIP++;

                            txtQuestion.setText(Integer.toString(REMEMBER_NUM_QUESTION));
                            txtNumSkip.setText(Integer.toString(REMEMBER_NUM_SKIP));
                            txtEnglish.setText(listVocabularys.get(REMEMBER_POS_VOCABULARY).getEnglish());

                            edtAnswers.setText("");
                        }
                    }
                });


            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.d("NgaDV", "onDestroy");
        REMEMBER_NUM_TRUE       = 0;
        REMEMBER_NUM_QUESTION   = 0;
        REMEMBER_POS_VOCABULARY = 0;
        REMEMBER_NUM_SKIP       = 0;
        REMEMBER_NUM_TOTAL      = 0;
        super.onDestroy();
    }
}
