package itpsoft.englishvocabulary.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itpsoft.englishvocabulary.R;


/**
 * Created by Do on 05/06/2015.
 */
public class ListenFragment extends Fragment {

    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_test_listen, container, false);

        return rootView;
    }
}
