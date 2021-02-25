package com.example.courseregistration.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseregistration.LoginActivity;
import com.example.courseregistration.R;
import com.example.courseregistration.connection.PreferenceManager;
import com.example.courseregistration.connection.RetrofitAPI;
import com.example.courseregistration.connection.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//수강신청 메뉴에 관한 목록들을 보여주는 리사이클러뷰 어댑터
public class RecyclerAdapter_reserve extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ArrayList<String>> list;
    int select;

    public RecyclerAdapter_reserve(ArrayList<ArrayList<String>> list, int select){
      this.list = list;
      this.select = select;
    }

    class Holder4 extends RecyclerView.ViewHolder{
        TextView classnum, classname, professor, classroom, credit, division;
        Button btn;
        public Holder4(@NonNull View itemView) {
            super(itemView);
            classnum = (TextView) itemView.findViewById(R.id.classnum_register);
            classname = (TextView) itemView.findViewById(R.id.classname_register);
            professor = (TextView) itemView.findViewById(R.id.professor_register);
            classroom = (TextView) itemView.findViewById(R.id.classroom_register);
            credit = (TextView) itemView.findViewById(R.id.credit_register);
            division = (TextView)itemView.findViewById(R.id.classDivision_register);
            btn = (Button) itemView.findViewById(R.id.register_btn);

            String token = PreferenceManager.getString(itemView.getContext(),"accessToken");
            String id = PreferenceManager.getString(itemView.getContext(), "userID");
            RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);

            //수강신청 버튼 동작
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String classnum_str = classnum.getText().toString();
                    String division_str = division.getText().toString();
                    String classtime_info = classroom.getText().toString();

                    Map map = new HashMap();
                    map.put("id",id);
                    map.put("classnum", classnum_str);
                    map.put("division",division_str);
                    map.put("classtime", classtime_info);

                    retrofitAPI.register_reserve(map).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

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
                                        //모든 액티비티를 종료한 후 로그인 화면으로 유도

                                        Intent login = new Intent(itemView.getContext(), LoginActivity.class);
                                        Intent intent = ((Activity)itemView.getContext()).getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        ((Activity)itemView.getContext()).overridePendingTransition(0, 0);
                                        ((Activity)itemView.getContext()).finishAffinity();
                                        itemView.getContext().startActivity(login);

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

                            else if(response.code()==404){
                                String str = "동시간대 신청한 수업이 존재합니다.";
                                showDialog(str,builder);
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(itemView.getContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    class Holder5 extends RecyclerView.ViewHolder{
        TextView classnum, classname, professor, classroom, credit, division;
        Button btn;
        public Holder5(@NonNull View itemView) {
            super(itemView);
            classnum = (TextView) itemView.findViewById(R.id.classnum_register);
            classname = (TextView) itemView.findViewById(R.id.classname_register);
            professor = (TextView) itemView.findViewById(R.id.professor_register);
            classroom = (TextView) itemView.findViewById(R.id.classroom_register);
            credit = (TextView) itemView.findViewById(R.id.credit_register);
            division = (TextView) itemView.findViewById(R.id.classDivision_register);
            btn = (Button) itemView.findViewById(R.id.register_btn);

            String token = PreferenceManager.getString(itemView.getContext(),"accessToken");
            String id = PreferenceManager.getString(itemView.getContext(), "userID");
            RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String classnum_str = classnum.getText().toString();
                    String division_str = division.getText().toString();
                    String classtime_info = classroom.getText().toString();

                    Map map = new HashMap();
                    map.put("id", id);
                    map.put("classnum", classnum_str);
                    map.put("division", division_str);
                    map.put("classtime", classtime_info);

                    retrofitAPI.register_search(map).enqueue(new Callback() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
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
                                        //모든 액티비티를 종료한 후 로그인 화면으로 유도

                                        Intent login = new Intent(itemView.getContext(), LoginActivity.class);
                                        Intent intent = ((Activity)itemView.getContext()).getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        ((Activity)itemView.getContext()).overridePendingTransition(0, 0);
                                        ((Activity)itemView.getContext()).finishAffinity();
                                        itemView.getContext().startActivity(login);
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

                            else if(response.code()==404){
                                String str = "동시간대 신청한 수업이 존재합니다.";
                                showDialog(str,builder);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(itemView.getContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    class Holder6 extends RecyclerView.ViewHolder {
        TextView classnum, classname, professor, classroom, credit, division;
        Button btn;

        public Holder6(@NonNull View itemView) {
            super(itemView);
            classnum = (TextView) itemView.findViewById(R.id.classnum_register2);
            classname = (TextView) itemView.findViewById(R.id.classname_register2);
            professor = (TextView) itemView.findViewById(R.id.professor_register2);
            classroom = (TextView) itemView.findViewById(R.id.classroom_register2);
            credit = (TextView) itemView.findViewById(R.id.credit_register2);
            division = (TextView) itemView.findViewById(R.id.classDivision_register2);
            btn = (Button) itemView.findViewById(R.id.register_delete_btn);

            String token = PreferenceManager.getString(itemView.getContext(),"accessToken");
            String id = PreferenceManager.getString(itemView.getContext(), "userID");
            RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    String classnum_str = classnum.getText().toString();
                    String division_str = division.getText().toString();
                    Map map = new HashMap();
                    map.put("id", id);
                    map.put("classnum", classnum_str);
                    map.put("division", division_str);

                    retrofitAPI.RegisterDelete(map).enqueue(new Callback() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        @Override
                        public void onResponse(Call call, Response response) {
                            if(response.isSuccessful()){
                                list.remove(pos);
                                builder.setTitle("안내메시지");
                                builder.setMessage("삭제되었습니다.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notifyItemRemoved(pos);
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }

                            else if (response.code()==401){
                                builder.setTitle("안내메시지");
                                builder.setMessage("세션이 만료되었습니다. 다시 로그인해주세요.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent login = new Intent(itemView.getContext(), LoginActivity.class);
                                        Intent intent = ((Activity)itemView.getContext()).getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        ((Activity)itemView.getContext()).overridePendingTransition(0, 0);
                                        ((Activity)itemView.getContext()).finishAffinity();
                                        itemView.getContext().startActivity(login);

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }

                            else if (response.code()==500){
                                String str = "오류가 발생했습니다.";
                                showDialog(str,builder);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(itemView.getContext(), "통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case 0:
                View view1 = inflater.inflate(R.layout.recyclerview_reserve_item, parent, false);
                return new Holder4(view1);

            case 1:
                View view2 = inflater.inflate(R.layout.recyclerview_reserve_item, parent, false);
                return new Holder5(view2);

            case 2:
                View view3 = inflater.inflate(R.layout.recyclerview_register_list, parent, false);
                return new Holder6(view3);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RecyclerAdapter_reserve.Holder4){
            ((RecyclerAdapter_reserve.Holder4) holder).classnum.setText(list.get(position).get(0));
            ((RecyclerAdapter_reserve.Holder4) holder).classname.setText(list.get(position).get(1));
            ((RecyclerAdapter_reserve.Holder4) holder).division.setText("("+list.get(position).get(2)+")");
            ((RecyclerAdapter_reserve.Holder4) holder).credit.setText(list.get(position).get(3));
            ((RecyclerAdapter_reserve.Holder4) holder).professor.setText(list.get(position).get(4));
            ((RecyclerAdapter_reserve.Holder4) holder).classroom.setText(list.get(position).get(5)+" "+list.get(position).get(6));

        }

        else if(holder instanceof RecyclerAdapter_reserve.Holder5){
            ((RecyclerAdapter_reserve.Holder5) holder).classnum.setText(list.get(position).get(0));
            ((RecyclerAdapter_reserve.Holder5) holder).classname.setText(list.get(position).get(1));
            ((RecyclerAdapter_reserve.Holder5) holder).division.setText("("+list.get(position).get(2)+")");
            ((RecyclerAdapter_reserve.Holder5) holder).credit.setText(list.get(position).get(3));
            ((RecyclerAdapter_reserve.Holder5) holder).professor.setText(list.get(position).get(4));
            ((RecyclerAdapter_reserve.Holder5) holder).classroom.setText(list.get(position).get(5)+" "+list.get(position).get(6));
        }

        else if(holder instanceof RecyclerAdapter_reserve.Holder6){
            ((RecyclerAdapter_reserve.Holder6) holder).classnum.setText(list.get(position).get(0));
            ((RecyclerAdapter_reserve.Holder6) holder).classname.setText(list.get(position).get(1));
            ((RecyclerAdapter_reserve.Holder6) holder).division.setText("("+list.get(position).get(2)+")");
            ((RecyclerAdapter_reserve.Holder6) holder).credit.setText(list.get(position).get(3));
            ((RecyclerAdapter_reserve.Holder6) holder).professor.setText(list.get(position).get(4));
            ((RecyclerAdapter_reserve.Holder6) holder).classroom.setText(list.get(position).get(5));
        }
    }

    @Override
    public int getItemCount() {return list.size();}


    @Override
    public int getItemViewType(int position) {
        if (select == 0)
            return 0;

        else if (select == 1)
            return 1;

        else if (select == 2)
            return 2;

        return -1;
    }


    //다이얼로그 메시지 메소드
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
