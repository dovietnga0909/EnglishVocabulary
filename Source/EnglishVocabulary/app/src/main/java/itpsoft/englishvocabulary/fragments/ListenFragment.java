package itpsoft.englishvocabulary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import itpsoft.englishvocabulary.R;


/**
 * Created by Do on 05/06/2015.
 */
public class ListenFragment extends Fragment {

    private TextView txtVietnamese,txtTrue,txtTotal,txtNumTrue,txtNumSkip;
    private EditText edtAnswers;
    private ImageView imgIcDeleteTxt;
    private CheckBox ckSuggest;
    private Button btnAnswers,btnRepeat,btnSkip;

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
        txtTrue         = (TextView)rootView.findViewById(R.id.txtTrue);
        txtTotal        = (TextView)rootView.findViewById(R.id.txtTotal);
        txtNumTrue      = (TextView)rootView.findViewById(R.id.txt_num_true);
        txtNumSkip      = (TextView)rootView.findViewById(R.id.txt_num_skip);
        //EditText
        edtAnswers      = (EditText) rootView.findViewById(R.id.edtAnswers);
        //ImageView
        imgIcDeleteTxt  =(ImageView)rootView.findViewById(R.id.imgIcDeleteTxt);
        //Checkbox
        ckSuggest       = (CheckBox)rootView.findViewById(R.id.ckSuggest);
        //Button
        btnAnswers      = (Button)rootView.findViewById(R.id.btnAnswer);
        btnRepeat       = (Button)rootView.findViewById(R.id.btnRepeat);
        btnSkip         = (Button)rootView.findViewById(R.id.btnSkip);

        //set Visible txtVietnamese khi duoc check
        ckSuggest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ckSuggest.isChecked()){
                    txtVietnamese.setVisibility(View.VISIBLE);
                }else {
                    txtVietnamese.setVisibility(View.INVISIBLE);
                }
            }
        });
        //set imgIcDeleteTxt Gone When edtAnswers = ""
//        imgIcDeleteTxt.setVisibility(View.GONE);
//        if(edtAnswers.getText().toString().trim() != ""){
//            imgIcDeleteTxt.setVisibility(View.VISIBLE);
//        }

        imgIcDeleteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAnswers.setText("");
            }
        });

        return rootView;
    }
}
