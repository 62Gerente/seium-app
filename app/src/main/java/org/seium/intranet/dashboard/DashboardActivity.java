package org.seium.intranet.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import org.seium.intranet.R;
import org.seium.intranet.tweet.TweetsFragment;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MaterialViewPager viewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        viewPager.getViewPager().setAdapter(new DashboardPagerAdapter(getSupportFragmentManager()));
        viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());
        viewPager.setMaterialViewPagerListener(new DashboardPagerListener());
    }

    private class DashboardPagerAdapter extends FragmentStatePagerAdapter {
        public DashboardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                    return new TweetsFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                default:
                    return "Tweets";
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    private class DashboardPagerListener implements MaterialViewPager.Listener {
        @Override
        public HeaderDesign getHeaderDesign(int page) {
            switch (page) {
                default:
                    return HeaderDesign.fromColorResAndDrawable(R.color.primary, ContextCompat
                            .getDrawable(getApplicationContext(), R.drawable.background));
            }
        }
    }
}
