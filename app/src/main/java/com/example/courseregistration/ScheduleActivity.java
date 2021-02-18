package com.example.courseregistration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseregistration.connection.PreferenceManager;
import com.example.courseregistration.connection.RetrofitAPI;
import com.example.courseregistration.connection.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity {
    static String token, id;
    static RetrofitAPI retrofitAPI;
    static ArrayList<ArrayList<String>> list;
    static ArrayList<String> child;
    Random random = new Random();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar_timeTable);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);

        id= PreferenceManager.getString(this,"userID");
        token = PreferenceManager.getString(this,"accessToken");
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
        list = new ArrayList<>();
        child = new ArrayList<>();
        String [] colors = getResources().getStringArray(R.array.colors);
        Context ctx =this;


        TextView [] textView = new TextView [50];
        String [] columns = {"b","c","d","e","f"};
        for(int i =0; i<columns.length;i++){
            for(int j =0; j<10; j++){
                String rID = columns[i]+Integer.toString(j+1);
                int resource = getResources().getIdentifier(rID, "id", this.getPackageName());
                textView[i*10+j] = findViewById(resource);
            }
        }

        Map map = new HashMap();
        map.put("id",id);

        retrofitAPI.getRegister_list(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        final JSONArray classInfo = new JSONArray(response.body().string());
                        for (int i =0; i<classInfo.length(); i++){
                            child = new ArrayList<>();
                            JSONObject object = classInfo.getJSONObject(i);
                            String title = object.getString("title");
                            String classtime = object.getString("classtime");
                            String classroom = object.getString("classroom");
                            child.add(title); child.add(classtime); child.add(classroom);
                            list.add(child);
                        }
                        setTimeTable(textView,list,colors);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else if (response.code() == 500){
                    Toast.makeText(getApplicationContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

                else if (response.code() == 401){
                    Intent login = new Intent(ctx, LoginActivity.class);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("안내메시지");
                    builder.setMessage("세션이 만료되었습니다. 다시 로그인해주세요.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.finishAffinity(ScheduleActivity.this);
                            startActivity(login);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public int dpToPx(int dp) { float density = getResources().getDisplayMetrics().density; return Math.round((float) dp * density); }


    //시간표 셋팅하는 함수
    public void setTimeTable(TextView[]textView, ArrayList<ArrayList<String>> list, String[]colors){
        ArrayList<ArrayList<String>> classList = new ArrayList<>();
        ArrayList<String>childList;

        for (int i =0; i<list.size();i++){
            childList = new ArrayList<>();
            childList.add(list.get(i).get(0));
            String [] divide = list.get(i).get(1).split(" ");
            childList.add(divide[0]);

            if(divide[1].length()==2){
                childList.add(divide[1].substring(0,1));
                childList.add(divide[1].substring(1,2));
            }

            else if(divide[1].length()==3){
                childList.add(divide[1].substring(0,1));
                childList.add(divide[1].substring(1,2));
                childList.add(divide[1].substring(2,3));
            }

            else if(divide[1].length()==4){
                if(divide[1].equals("8910")) {
                    childList.add(divide[1].substring(0, 1));
                    childList.add(divide[1].substring(1, 2));
                    childList.add(divide[1].substring(2, 4));
                }

                else{
                    childList.add(divide[1].substring(0, 1));
                    childList.add(divide[1].substring(1, 2));
                    childList.add(divide[1].substring(2, 3));
                    childList.add(divide[1].substring(3, 4));
                }
            }
            else if(divide[1].length()==5){
                childList.add(divide[1].substring(0,1));
                childList.add(divide[1].substring(1,3));
                childList.add(divide[1].substring(3,5));
            }
            childList.add(list.get(i).get(2));
            classList.add(childList);

        }

        for(int i =0; i<classList.size();i++){
             if(classList.get(i).get(1).equals("월")){
                 String text = classList.get(i).get(0)+"\n"+classList.get(i).get(classList.get(i).size()-1);
                 int size = classList.get(i).size()-3;
                 int start = Integer.parseInt(classList.get(i).get(2));
                 int colorIdx = random.nextInt(colors.length);
                 setTextOption(0,textView, start, size, colors, colorIdx, text);
             }


            else if(classList.get(i).get(1).equals("화")){
                 String text = classList.get(i).get(0)+"\n"+classList.get(i).get(classList.get(i).size()-1);
                 int size = classList.get(i).size()-3;
                 int start = Integer.parseInt(classList.get(i).get(2));
                 int colorIdx = random.nextInt(colors.length);
                 setTextOption(1, textView, start, size, colors, colorIdx, text);

            }


            else if(classList.get(i).get(1).equals("수")){
                 String text = classList.get(i).get(0)+"\n"+classList.get(i).get(classList.get(i).size()-1);
                 int size = classList.get(i).size()-3;
                 int start = Integer.parseInt(classList.get(i).get(2));
                 int colorIdx = random.nextInt(colors.length);
                 setTextOption(2, textView, start, size, colors, colorIdx, text);
            }


            else if(classList.get(i).get(1).equals("목")){
                 String text = classList.get(i).get(0)+"\n"+classList.get(i).get(classList.get(i).size()-1);
                 int size = classList.get(i).size()-3;
                 int start = Integer.parseInt(classList.get(i).get(2));
                 int colorIdx = random.nextInt(colors.length);
                 setTextOption(3, textView, start, size, colors, colorIdx, text);
            }


            else if(classList.get(i).get(1).equals("금")){
                 String text = classList.get(i).get(0)+"\n"+classList.get(i).get(classList.get(i).size()-1);
                 int size = classList.get(i).size()-3;
                 int start = Integer.parseInt(classList.get(i).get(2));
                 int colorIdx = random.nextInt(colors.length);
                 setTextOption(4, textView, start, size, colors, colorIdx, text);
            }
        }

    }


    public void setTextOption(int column, TextView[]textView, int start, int size, String[]colors, int colorIdx, String text){
        int checkIdx = column*10+start-1;
        Typeface typeface = ResourcesCompat.getFont(this, R.font.samliphopangchebasic);

        for(int j = start; j< start+size; j++){
            int idx = column*10+j-1;
            textView[idx].setBackgroundColor(Color.parseColor(colors[colorIdx]));

            if(idx==checkIdx) {
                textView[idx].setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) 11.7);
                textView[idx].setTextColor(Color.parseColor("#000000"));
                textView[idx].setTypeface(typeface);
                textView[idx].setText(text);
            }
        }
    }
}