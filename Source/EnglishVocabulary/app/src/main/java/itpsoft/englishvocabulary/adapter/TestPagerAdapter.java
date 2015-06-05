package itpsoft.englishvocabulary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Do on 05/06/2015.
 */
public class TestPagerAdapter extends FragmentPagerAdapter {
    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */


    private List<Fragment> fragments;

    public TestPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
