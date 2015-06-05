package itpsoft.englishvocabulary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itpsoft.englishvocabulary.R;


/**
 * Created by Do on 05/06/2015.
 */
public class ListenFragment extends Fragment {

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_test_listen, container, false);

        return rootView;
    }
}
