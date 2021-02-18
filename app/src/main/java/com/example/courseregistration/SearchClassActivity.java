package com.example.courseregistration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.courseregistration.Adapters.ViewPagerAdapter;
import com.example.courseregistration.Fragment.Search1;
import com.example.courseregistration.Fragment.Search2;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchClassActivity extends AppCompatActivity {
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_class);
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Search1 search1 = new Search1(0);
        Search2 search2 = new Search2(0);

        ViewPager viewpager = findViewById(R.id.viewpager); //뷰페이저
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()); //뷰페이저 어댑터에 search1, search2 프래그먼트가 붙어있음
        adapter.addItem(search1);
        adapter.addItem(search2);
        viewpager.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(viewpager);
        tab.getTabAt(0).setText("전공/교필");
        tab.getTabAt(1).setText("교선");
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