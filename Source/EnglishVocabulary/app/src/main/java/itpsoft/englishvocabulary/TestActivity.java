package itpsoft.englishvocabulary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import itpsoft.englishvocabulary.adapter.TestPagerAdapter;
import itpsoft.englishvocabulary.fragments.ListenFragment;
import itpsoft.englishvocabulary.fragments.RememberFragment;

/**
 * Created by Do on 05/06/2015.
 */
public class TestActivity extends FragmentActivity implements ActionBar.TabListener
        ,ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener {

    TestPagerAdapter testPagerAdapter;
    ViewPager testViewPager;
    public static TabHost tabHost;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        fragments = new Vector<Fragment>();
        tabHost = (TabHost) findViewById(R.id.tabhost);

        addTab(this, getResources().getString(R.string.txt_tab_test_listen), R.drawable.ic_tab_listen_selector, ListenFragment.class);
        addTab(this, getResources().getString(R.string.txt_tab_test_remember), R.drawable.ic_tab_remember_selector, RememberFragment.class);

        this.testPagerAdapter = new TestPagerAdapter(
                super.getSupportFragmentManager(), fragments);
        this.testViewPager = (ViewPager) super.findViewById(R.id.testViewPager);
        this.testViewPager.setAdapter(this.testPagerAdapter);
        this.testViewPager.setOnPageChangeListener(this);
    }

    private void addTab(TestActivity testActivity,String labelId,
                        int drawableId, Class<?> c){
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab" + labelId);
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_test_indicator, null, false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.txtTabIndicator);
            title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.iconTabIndicator);
            icon.setImageResource(drawableId);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
//            icon.setLayoutParams(params);

        tabSpec.setIndicator(tabIndicator);
        tabSpec.setContent(testActivity.new TabFactory(testActivity));
        tabHost.addTab(tabSpec);
        tabHost.setOnTabChangedListener(this);

        fragments.add(Fragment.instantiate(this,c.getName()));


    }

    class TabFactory implements TabHost.TabContentFactory{
        private final Context mContext;

        public TabFactory(Context context){
            mContext = context;
        }

        public  View createTabContent(String tag){
            View view = new View(mContext);
            view.setMinimumHeight(0);
            view.setMinimumWidth(0);

            return view;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        this.tabHost.setCurrentTab(position);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int position = this.tabHost.getCurrentTab();
        this.testViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
        // super.onBackPressed();
    }
}
