package com.example.courseregistration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseregistration.connection.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    static private Context ctx;
    static String name, token, id;
    static RetrofitAPI retrofitAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent login = new Intent(this, LoginActivity.class);
        ctx=this;

        TextView nameText = (TextView) findViewById(R.id.name);
        TextView InfoText = (TextView)findViewById(R.id.usrInfo);
        TextView creditText = (TextView) findViewById(R.id.credit);

          name = PreferenceManager.getString(ctx, "userName");
          token = PreferenceManager.getString(ctx,"accessToken");
          id = PreferenceManager.getString(ctx,"userID");
           retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);

           retrofitAPI.getMain(id).enqueue(new Callback<ServiceInterface>() {
            @Override
            public void onResponse(Call<ServiceInterface> call, Response<ServiceInterface> response) {
                if(response.isSuccessful()){
                    String major = response.body().getMajor();
                    String grade = response.body().getGrade();
                    int credit = response.body().getCredit();

                    nameText.setText(name+"님 환영합니다!");
                    InfoText.setText(major+" / "+grade+"학년");
                    creditText.setText("신청가능학점: "+credit+"학점");
                }

                else if (response.code()==401){
                    showDialog(login,ctx);
                }
            }

            @Override
            public void onFailure(Call<ServiceInterface> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("안내메시지");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.removeKey(ctx,"userID");
                        PreferenceManager.removeKey(ctx,"accessToken");
                        PreferenceManager.removeKey(ctx,"userName");
                        PreferenceManager.removeKey(ctx,"userMajor");
                        PreferenceManager.removeKey(ctx,"usergrade");
                        ActivityCompat.finishAffinity(MainActivity.this);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    public void notice(View v){
        Intent notice = new Intent(this, NoticeActivity.class);
        load(retrofitAPI, ctx, notice, id);
    }


    public void lookup(View v){
        Intent lookup = new Intent (this, SearchClassActivity.class);
        load(retrofitAPI, ctx, lookup, id);
    }


    public void reserve(View v){
        Intent reserve = new Intent(this, reserveActivity.class);
        load(retrofitAPI, ctx, reserve, id);
    }


    public void registration(View v){
        Intent registration = new Intent(this, RegisterActivity.class);
        load(retrofitAPI, ctx, registration, id);
    }


    public void load(RetrofitAPI retrofitAPI, Context context, Intent intent, String ID){
        Intent login = new Intent(context, LoginActivity.class);
        retrofitAPI.getConnect(ID).enqueue(new Callback<ServiceInterface>() {
            @Override
            public void onResponse(Call<ServiceInterface> call, Response<ServiceInterface> response) {
                if(response.isSuccessful()){
                    startActivity(intent);
                }
                else if(response.code()==401){
                    showDialog(login,ctx);
                }
            }
            @Override
            public void onFailure(Call<ServiceInterface> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void showDialog(Intent login, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("안내메시지");
        builder.setMessage("세션이 만료되었습니다. 다시 로그인해주세요.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(login);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}