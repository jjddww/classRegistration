package com.example.courseregistration.Fragment;

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
import android.widget.Toast;

import com.example.courseregistration.Adapters.RecyclerAdapter;
import com.example.courseregistration.Adapters.RecyclerAdapter_reserve;
import com.example.courseregistration.LoginActivity;
import com.example.courseregistration.R;
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

//수강신청 메뉴의 첫 번째 탭 프래그먼트
//장바구니에 담은 과목 수강신청
public class RegisterFragment1 extends Fragment {
    static String  id, token;
    static RecyclerView recyclerView;
    static ArrayList<ArrayList<String>> list;
    static ArrayList<String> child;
    static RecyclerAdapter_reserve adapter;
    static int select;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment1(int select) {
        this.select = select;
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

        View view = inflater.inflate(R.layout.fragment_register1, container, false);
        id= PreferenceManager.getString(getActivity().getApplicationContext(),"userID");
        token = PreferenceManager.getString(getActivity().getApplicationContext(),"accessToken");
        list = new ArrayList<>();
        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
        adapter = new RecyclerAdapter_reserve(list,select);
        recyclerView = view.findViewById(R.id.reserve_register);

        Map map = new HashMap();
        map.put("id", id);

        retrofitAPI.getReserve_register(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    list.clear();
                    child = new ArrayList<>();
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
                            String classroom = object.getString("classtime");
                            String classtime = object.getString("classroom");
                            child.add(classnum);child.add(title); child.add(division);child.add(credit);
                            child.add(professor);  child.add(classroom); child.add(classtime);
                            list.add(child);
                        }

                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        recyclerView.setAdapter(adapter);

                        if(list.size()>0) adapter.notifyDataSetChanged(); //데이터 변경을 알림

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
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    showDialog(login);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    //세션 만료를 알리는 다이얼로그 메소드
    public void showDialog(Intent login){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("안내메시지");
        builder.setMessage("세션이 만료되었습니다. 다시 로그인해주세요.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //모든 액티비티 종료 후 다시 로그인하도록 유도

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