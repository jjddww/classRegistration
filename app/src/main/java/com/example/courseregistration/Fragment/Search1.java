package com.example.courseregistration.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseregistration.Adapters.RecyclerAdapter;
import com.example.courseregistration.LoginActivity;
import com.example.courseregistration.R;
import com.example.courseregistration.SearchClassActivity;
import com.example.courseregistration.connection.PreferenceManager;
import com.example.courseregistration.connection.RetrofitAPI;
import com.example.courseregistration.connection.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search1 extends Fragment {
    static ArrayList<ArrayList<String>> list;
    static ArrayList<String> child;
    static String major, grade, id, token;
    static RetrofitAPI retrofitAPI;
    static RecyclerAdapter adapter;
    static RecyclerView recyclerView;
    static int select;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Search1(int select) {
        // Required empty public constructor
        this.select=select;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search1, container, false);
        id= PreferenceManager.getString(getActivity().getApplicationContext(),"userID");
        token = PreferenceManager.getString(getActivity().getApplicationContext(),"accessToken");
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
        list = new ArrayList<>();
        adapter = new RecyclerAdapter(list, select);
        recyclerView = view.findViewById(R.id.major_search);

        Spinner majorSpinner = (Spinner)view.findViewById(R.id.spinner_search1);
        Spinner gradeSpinner = (Spinner)view.findViewById(R.id.spinner_search2);

        String userMajor = PreferenceManager.getString(getActivity().getApplicationContext(),"userMajor");
        String userGrade = PreferenceManager.getString(getActivity().getApplicationContext(),"usergrade");
        majorSpinner.setSelection(getIndex(majorSpinner, userMajor));
        gradeSpinner.setSelection(getIndex(gradeSpinner, userGrade+"학년"));

        Map map = new HashMap();
        map.put("id",id); map.put("major", userMajor); map.put("grade", userGrade+"학년");
        load(retrofitAPI,map,view); // 초기화면 불러오기

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                major = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grade = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Button btn = (Button)view.findViewById(R.id.searchBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("id", id); map.put("major", major); map.put("grade",grade);
                load(retrofitAPI, map, view);
            }
        });
        return view;

    }


/////미리 드롭다운 메뉴 셋팅하는 함수////////
    private int getIndex(Spinner spinner, String item){
        for(int i =0; i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item))
                return i;
        }
        return 0;
    }



///////리사이클러뷰에 정보 표시하는 메소드/////////
    private void load(RetrofitAPI retrofitAPI, Map map, View view){
        Intent login = new Intent(getActivity(), LoginActivity.class);
        retrofitAPI.SearchMajor(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    list.clear();
                    try {
                        final JSONArray classInfo = new JSONArray(response.body().string());
                        for (int i =0; i<classInfo.length(); i++){
                            child = new ArrayList<>();
                            JSONObject object = classInfo.getJSONObject(i);
                            String classnum = object.getString("classnum");
                            String title = object.getString("title");
                            String division = object.getString("division");
                            String credit = "("+object.getString("credit")+"학점)";
                            String professor = object.getString("professor");
                            String classroom = object.getString("classtime")+" "+object.getString("classroom");
                            String reserve = object.getString("reserve")+"명";
                            String personnel = "/ "+object.getString("personnel")+"명";
                            child.add(classnum);child.add(title); child.add(division); child.add(credit);
                            child.add(professor);  child.add(classroom); child.add(reserve); child.add(personnel);
                            list.add(child);
                        }
                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        recyclerView.setAdapter(adapter);

                        if(list.size()>0) adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else if (response.code() == 500){
                    Toast.makeText(getActivity().getApplicationContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

                else if (response.code() == 401){
                    showDialog(login);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    ////다이얼로그 메시지 띄우는 메소드///////
    public void showDialog(Intent login){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("안내메시지");
        builder.setMessage("세션이 만료되었습니다. 다시 로그인해주세요.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finishAffinity();

                startActivity(login);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}