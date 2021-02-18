package com.example.courseregistration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.courseregistration.Adapters.ViewPagerAdapter;
import com.example.courseregistration.Fragment.RegisterFragment1;
import com.example.courseregistration.Fragment.RegisterFragment2;
import com.example.courseregistration.Fragment.RegisterFragment3;
import com.example.courseregistration.Fragment.RegisterFragment4;
import com.google.android.material.tabs.TabLayout;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);

        RegisterFragment1 registerFragment1 = new RegisterFragment1(0);
        RegisterFragment2 registerFragment2 = new RegisterFragment2(1);
        RegisterFragment3 registerFragment3 = new RegisterFragment3();
        RegisterFragment4 registerFragment4 = new RegisterFragment4(2);

        ViewPager viewpager = findViewById(R.id.viewpager_register); //뷰페이저
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addItem(registerFragment1);
        adapter.addItem(registerFragment2);
        adapter.addItem(registerFragment3);
        adapter.addItem(registerFragment4);

        viewpager.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab_register);
        tab.setupWithViewPager(viewpager);
        tab.getTabAt(0).setText("찜강내역");
        tab.getTabAt(1).setText("직접검색");
        tab.getTabAt(2).setText("학수번호 검색");
        tab.getTabAt(3).setText("수강신청 내역");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.schedule:
                Intent intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
                return true;

            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}