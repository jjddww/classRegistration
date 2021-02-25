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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
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

//수강신청 메뉴의 두 번째 탭 프래그먼트
//직접 과목 검색 후 수강신청
public class RegisterFragment2 extends Fragment {
    static String id,token,major,grade,division;
    static RecyclerAdapter_reserve adapter;
    static RecyclerView recyclerView;
    static ArrayList<ArrayList<String>> list;
    static ArrayList<String> child;
    static RetrofitAPI retrofitAPI;
    static int select;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment2(int select) {
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
        View view = inflater.inflate(R.layout.fragment_register2, container, false);
        RadioButton majorBtn = (RadioButton)view.findViewById(R.id.radio_major);
        RadioButton electivesBtn = (RadioButton)view.findViewById(R.id.radio_electives);
        Button search = (Button)view.findViewById(R.id.search_register);
        Spinner spinner1 = (Spinner)view.findViewById(R.id.spinner_register1);
        Spinner spinner2 = (Spinner)view.findViewById(R.id.spinner_register2);
        Spinner spinner3 = (Spinner)view.findViewById(R.id.spinner_register3);
        Spinner spinner4 = (Spinner)view.findViewById(R.id.spinner_register4);

        String userMajor = PreferenceManager.getString(getActivity().getApplicationContext(),"userMajor");
        String userGrade = PreferenceManager.getString(getActivity().getApplicationContext(),"usergrade");
        id= PreferenceManager.getString(getActivity().getApplicationContext(),"userID");
        token = PreferenceManager.getString(getActivity().getApplicationContext(),"accessToken");
        list = new ArrayList<>();
        spinner1.setSelection(getIndex(spinner1, userMajor));
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
        spinner3.setSelection(getIndex(spinner3,userGrade+"학년"));
        adapter = new RecyclerAdapter_reserve(list,select);
        recyclerView = view.findViewById(R.id.register_search);

        Map map = new HashMap();
        map.put("id",id); map.put("major", userMajor); map.put("grade", userGrade+"학년");
        load1(retrofitAPI, map);

        ///스피너가 사용자의 전공으로 미리 선택되어 있도록 셋팅
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                major = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///스피너가 사용자의 학년으로 미리 선택되어 있도록 셋팅
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grade = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                division = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //전공 라디오버튼 선택 시
        majorBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    spinner1.setVisibility(view.VISIBLE);
                    spinner3.setVisibility(view.VISIBLE);
                    spinner1.setClickable(true);
                    spinner3.setClickable(true);
                }
                else{
                    spinner1.setVisibility(view.GONE);
                    spinner3.setVisibility(view.GONE);
                    spinner1.setClickable(false);
                    spinner3.setClickable(false);
                }
            }
        });

        //교양 라디오버튼 선택 시
        electivesBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    spinner2.setVisibility(view.VISIBLE);
                    spinner4.setVisibility(view.VISIBLE);
                    spinner2.setClickable(true);
                    spinner4.setClickable(true);
                }

                else{
                    spinner2.setVisibility(view.GONE);
                    spinner4.setVisibility(view.GONE);
                    spinner2.setClickable(false);
                    spinner4.setClickable(false);
                }
            }
        });

        //과목 검색 버튼 동작
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (majorBtn.isChecked()){
                Map map = new HashMap();
                map.put("id",id); map.put("major",major); map.put("grade", grade);
                load1(retrofitAPI,map);
                }
                else if (electivesBtn.isChecked()){
                    Map map = new HashMap();
                    map.put("id",id); map.put("division",division);
                    load2(retrofitAPI,map);
                }
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

    //전공 검색 결과 로드하는 메소드
    private void load1(RetrofitAPI retrofitAPI, Map map){
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
                            String classroom = object.getString("classtime");
                            String classtime = object.getString("classroom");
                            child.add(classnum);child.add(title); child.add(division); child.add(credit);
                            child.add(professor);  child.add(classroom); child.add(classtime);
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

    //교양과목 검색 결과 로드하는 메소드
    private void load2(RetrofitAPI retrofitAPI, Map map){
        Intent login = new Intent(getActivity(), LoginActivity.class);
        retrofitAPI.SearchElectives(map).enqueue(new Callback<ResponseBody>() {
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
}