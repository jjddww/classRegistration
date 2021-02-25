package com.example.courseregistration;
import com.example.courseregistration.connection.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    private Context ctx;
    static int intro=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        Intent intent = new Intent(this, LoadingActivity.class);
        final Intent main = new Intent(this, MainActivity.class);

        if(intro<=0) startActivity(intent);
        intro++;

        final EditText studentID = (EditText)findViewById(R.id.studentID); //학번 입력란
        final EditText password = (EditText)findViewById(R.id.password); //비밀번호 입력란
        Button loginBtn = (Button)findViewById(R.id.loginBtn); //로그인 버튼


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.35.38.243:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);


        loginBtn.setOnClickListener(new View.OnClickListener() { //버튼 클릭 리스너
            HashMap<String,Object> input = new HashMap<>();

            @Override
            public void onClick(View v) {
                input.put("id",studentID.getText().toString());
                input.put("password",password.getText().toString());

                retrofitAPI.postData(input).enqueue(new Callback<ServiceInterface>() {
                    @Override
                    public void onResponse(@NonNull Call<ServiceInterface> call, Response<ServiceInterface> response) {
                        if(response.isSuccessful()){
                            String access = response.body().getAccessToken();

                            if (access.equals("-1"))
                                Toast.makeText(getApplicationContext(),"존재하지 않는 학번입니다.", Toast.LENGTH_SHORT).show();

                            else if (access.equals("0"))
                                Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                            else{
                                //로그인 성공
                                //받아온 사용자 정보와 토큰을 저장
                                PreferenceManager.setString(ctx, "userName", response.body().getUsrName());
                                PreferenceManager.setString(ctx, "userID", response.body().getUsrID());
                                PreferenceManager.setString(ctx, "accessToken", access);
                                PreferenceManager.setString(ctx, "userMajor", response.body().getMajor());
                                PreferenceManager.setString(ctx, "usergrade", response.body().getGrade());
                                finish();
                                startActivity(main);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceInterface> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}