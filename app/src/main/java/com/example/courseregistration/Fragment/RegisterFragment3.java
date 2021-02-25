package com.example.courseregistration.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.courseregistration.LoginActivity;
import com.example.courseregistration.R;
import com.example.courseregistration.connection.PreferenceManager;
import com.example.courseregistration.connection.RetrofitAPI;
import com.example.courseregistration.connection.ServiceGenerator;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//수강신청 메뉴의 세 번째 탭 프래그먼트
//학수번호와 분반 입력 후 수강신청
public class RegisterFragment3 extends Fragment {
    static String  id, token;
    static EditText classnum_edit;
    static EditText division_edit;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment3() {

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

        View view = inflater.inflate(R.layout.fragment_register3, container, false);
        id= PreferenceManager.getString(getActivity().getApplicationContext(),"userID");
        token = PreferenceManager.getString(getActivity().getApplicationContext(),"accessToken");
        classnum_edit = (EditText)view.findViewById(R.id.classnum_input);
        division_edit = (EditText)view.findViewById(R.id.division_input);

        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
        Map map = new HashMap();

        Button register = (Button)view.findViewById(R.id.register_btn3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classnum = classnum_edit.getText().toString();
                String division = division_edit.getText().toString();
                map.put("id", id); map.put("classnum",classnum); map.put("division","("+division+")");

                retrofitAPI.register_classnum(map).enqueue(new Callback() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.isSuccessful()){
                            String str = "신청되었습니다.";
                            showDialog(str, builder);
                        }

                        else if(response.code()==401){
                            builder.setTitle("안내메시지");
                            builder.setMessage("세션이 만료되었습니다. 다시 로그인해주세요.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getActivity().getIntent();
                                    Intent login = new Intent(getActivity(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().overridePendingTransition(0, 0);
                                    getActivity().finishAffinity();
                                    startActivity(login);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else if(response.code()==500){
                            String str = "이미 수강신청한 과목입니다.";
                            showDialog(str, builder);
                        }

                        else if(response.code()==403){
                            String str = "신청 가능 인원을 초과하였습니다.";
                            showDialog(str, builder);
                        }

                        else if(response.code()==400){
                            String str = "신청 가능 학점을 초과하였습니다.";
                            showDialog(str, builder);
                        }

                        else if(response.code()==405){
                            String str = "과목 정보가 존재하지 않습니다.";
                            showDialog(str, builder);
                        }
                        else if(response.code()==404){
                            String str = "동시간대 신청한 수업이 존재합니다.";
                            showDialog(str, builder);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

    //다이얼로그 메소드

    public void showDialog(String str, AlertDialog.Builder builder){
        builder.setTitle("안내메시지");
        builder.setMessage(str);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}