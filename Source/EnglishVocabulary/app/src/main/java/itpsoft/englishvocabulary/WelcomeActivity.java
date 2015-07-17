package itpsoft.englishvocabulary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;
import itpsoft.englishvocabulary.view.ColorAnimationView;


public class WelcomeActivity
        extends FragmentActivity {
    private static final int[] resource = new int[]{R.drawable.welcome1, R.drawable.welcome4,
            R.drawable.welcome3, R.drawable.welcome4};
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private ViewPager viewPager;
    private boolean agreeRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agreeRun = SPUtil.instance(WelcomeActivity.this).get(SPUtil.KEY_AGREE_RUN, false);
        if(agreeRun){
            Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_welcome);
        MyFragmentStatePager adpter = new MyFragmentStatePager(getSupportFragmentManager());
        ColorAnimationView colorAnimationView = (ColorAnimationView) findViewById(R.id.ColorAnimationView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adpter);

//        colorAnimationView.setmViewPager(viewPager, resource.length);
        colorAnimationView.setmViewPager(viewPager,resource.length,0xffFF8080,0xff8080FF,0xffffffff,0xff8080FF);
        colorAnimationView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("TAG", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TAG", "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("TAG", "onPageScrollStateChanged");
            }
        });
    }


    public class MyFragmentStatePager
            extends FragmentStatePagerAdapter {

        public MyFragmentStatePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new MyFragment(position);
        }

        @Override
        public int getCount() {
            return resource.length;
        }
    }

    @SuppressLint("ValidFragment")
    public class MyFragment
            extends Fragment {
        private int position;

        public MyFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.welcome_layout, null);
            LinearLayout control = (LinearLayout) view.findViewById(R.id.control);
            final Button start = (Button) view.findViewById(R.id.start);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            final Button next = (Button) view.findViewById(R.id.next);
            Button prev = (Button) view.findViewById(R.id.prev);
            imageView.setImageResource(resource[position]);
            control.setVisibility(View.GONE);
            start.setVisibility(View.GONE);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(position + 1);
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(position - 1);
                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPUtil.instance(WelcomeActivity.this).set(SPUtil.KEY_AGREE_RUN, true);
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            if (position < resource.length - 1) {
                if(position==0){
                    prev.setVisibility(View.INVISIBLE);
                }else{
                    prev.setVisibility(View.VISIBLE);
                }
                control.setVisibility(View.VISIBLE);
            } else {
                start.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }
}
