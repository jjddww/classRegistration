package com.example.courseregistration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.courseregistration.Adapters.ViewPagerAdapter;
import com.example.courseregistration.Fragment.ReserveFragment1;
import com.example.courseregistration.Fragment.Search1;
import com.example.courseregistration.Fragment.Search2;
import com.google.android.material.tabs.TabLayout;

//장바구니 기능 액티비티

public class reserveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        Toolbar toolbar = findViewById(R.id.toolbar_reserve);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ReserveFragment1 reserveFragment1 = new ReserveFragment1(2);
        Search1 search1 = new Search1(1);
        Search2 search2 = new Search2(1);

        ViewPager viewpager = findViewById(R.id.viewpager_reserve); //뷰페이저
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()); //뷰페이저 어댑터에 search1, search2 프래그먼트를 붙임.
        adapter.addItem(search1); adapter.addItem(search2); adapter.addItem(reserveFragment1);
        viewpager.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab_reserve);
        tab.setupWithViewPager(viewpager);
        tab.getTabAt(0).setText("전공/교필");
        tab.getTabAt(1).setText("교선");
        tab.getTabAt(2).setText("장바구니 내역");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}