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

//과목 조회, 장바구니 목록을 보여주는 리사이클러뷰 어댑터
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ArrayList<String>> list;
    int select;
    static String classnumber,title,division_info,credit_info,professor_info,classroom_info,reserve_info,personnel_info;

    public RecyclerAdapter(ArrayList<ArrayList<String>> list, int select){
        this.list=list;
        this.select=select;
    }


    class Holder1 extends RecyclerView.ViewHolder{
        TextView classnum, classname, professor, classroom, credit, personnel,reserve, division;
        public Holder1(@NonNull View itemView){
            super(itemView);
            classnum = itemView.findViewById(R.id.classnum1);
            classname = itemView.findViewById(R.id.classname1);
            division = itemView.findViewById(R.id.classDivision1);
            professor = itemView.findViewById(R.id.professor_name);
            classroom = itemView.findViewById(R.id.classroom1);
            credit= itemView.findViewById(R.id.credit);
            reserve = itemView.findViewById(R.id.reserve1);
            personnel = itemView.findViewById(R.id.personnel1);
        }
    }



    class Holder2 extends RecyclerView.ViewHolder{
        TextView classnum, classname, professor, classroom, credit, personnel, reserve, division;
        Button btn;

        public Holder2(@NonNull View itemView){
            super(itemView);
            classnum = itemView.findViewById(R.id.classnum2);
            classname = itemView.findViewById(R.id.classname2);
            division = itemView.findViewById(R.id.classDivision2);
            professor = itemView.findViewById(R.id.professor_name2);
            classroom = itemView.findViewById(R.id.classroom2);
            credit= itemView.findViewById(R.id.credit2);
            personnel = itemView.findViewById(R.id.personnel2);
            reserve = itemView.findViewById(R.id.reserve2);
            btn = itemView.findViewById(R.id.reserve_Btn);

            //장바구니 담기 버튼 동작
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String token = PreferenceManager.getString(itemView.getContext(),"accessToken");
                    String id = PreferenceManager.getString(itemView.getContext(), "userID");
                    String classnum_str = classnum.getText().toString();
                    String division_str = division.getText().toString();

                    Map map = new HashMap();
                    map.put("id",id);
                    map.put("classnum", classnum_str);
                    map.put("division",division_str);

                    RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
                    int pos = getAdapterPosition();


                    retrofitAPI.getReserve(map).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ArrayList<String>child = new ArrayList<>();
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                            if(response.isSuccessful()){
                                try{
                                    final JSONArray classInfo = new JSONArray(response.body().string());
                                    JSONObject object = classInfo.getJSONObject(0);
                                     classnumber = object.getString("classnum");
                                     title = object.getString("title");
                                     division_info = object.getString("division");
                                     credit_info = "("+object.getString("credit")+"학점)";
                                     professor_info = object.getString("professor");
                                     classroom_info = object.getString("classtime")+" "+object.getString("classroom");
                                     reserve_info = object.getString("reserve")+"명";
                                     personnel_info = "/ "+object.getString("personnel")+"명";

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                child.add(classnumber); child.add(title); child.add(division_info); child.add(credit_info);
                                child.add(professor_info); child.add(classroom_info); child.add(reserve_info);child.add(personnel_info);
                                list.set(pos,child);
                                builder.setTitle("안내메시지");
                                builder.setMessage("찜강신청이 완료되었습니다.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notifyItemChanged(pos);
                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }

                            else if(response.code()==401){
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


                            else if(response.code()==500){
                                builder.setTitle("안내메시지");
                                builder.setMessage("이미 찜강신청한 과목입니다.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
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

    class Holder3 extends RecyclerView.ViewHolder{
        TextView classnum, classname, professor, classroom, credit, personnel,reserve, division;
        Button btn;
        public Holder3(@NonNull  View itemView){
            super(itemView);
            classnum = itemView.findViewById(R.id.classnum3);
            classname = itemView.findViewById(R.id.classname3);
            division = itemView.findViewById(R.id.classDivision3);
            professor = itemView.findViewById(R.id.professor_name3);
            classroom = itemView.findViewById(R.id.classroom3);
            credit= itemView.findViewById(R.id.credit3);
            reserve = itemView.findViewById(R.id.reserve3);
            personnel = itemView.findViewById(R.id.personnel3);
            btn = itemView.findViewById(R.id.reserve_delete_btn);

            //장바구니에 담은 과목 삭제 버튼 동작
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String token = PreferenceManager.getString(itemView.getContext(),"accessToken");
                    String id = PreferenceManager.getString(itemView.getContext(), "userID");
                    String classnum_str = classnum.getText().toString();
                    String division_str = division.getText().toString();
                    Map map = new HashMap();
                    map.put("id",id);
                    map.put("classnum", classnum_str);
                    map.put("division",division_str);
                    RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, token);
                    int pos = getAdapterPosition();

                    retrofitAPI.ReserveDelete(map).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

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
                                builder.setTitle("안내메시지");
                                builder.setMessage("오류가 발생했습니다.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
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



    @Override
    public int getItemViewType(int position) {
        //프래그먼트로부터 select 변수를 받아와 뷰 홀더를 선택

        if (select == 0)
            return 0;
        else if(select ==1)
            return 1;
        else if(select ==2)
            return 2;

        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //case에 따라 리스트 형태가 달라짐

        switch (viewType){
            case 0:
                View view1 = inflater.inflate(R.layout.recyclerview_item, parent, false);
                return new Holder1(view1);

            case 1:
                View view2 = inflater.inflate(R.layout.recyclerview_item2, parent, false);
                return new Holder2(view2);

            case 2:
                View view3 =inflater.inflate(R.layout.recyclerview_reserve_list, parent, false);
                return new Holder3(view3);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof Holder1){
            ((Holder1) holder).classnum.setText(list.get(position).get(0));
            ((Holder1) holder).classname.setText(list.get(position).get(1));
            ((Holder1) holder).division.setText("("+list.get(position).get(2)+")");
            ((Holder1) holder).credit.setText(list.get(position).get(3));
            ((Holder1) holder).professor.setText(list.get(position).get(4));
            ((Holder1) holder).classroom.setText(list.get(position).get(5));
            ((Holder1) holder).reserve.setText(list.get(position).get(6));
            ((Holder1) holder).personnel.setText(list.get(position).get(7));
        }

        else if (holder instanceof Holder2){
            ((Holder2) holder).classnum.setText(list.get(position).get(0));
            ((Holder2) holder).classname.setText(list.get(position).get(1));
            ((Holder2) holder).division.setText("("+list.get(position).get(2)+")");
            ((Holder2) holder).credit.setText(list.get(position).get(3));
            ((Holder2) holder).professor.setText(list.get(position).get(4));
            ((Holder2) holder).classroom.setText(list.get(position).get(5));
            ((Holder2) holder).reserve.setText(list.get(position).get(6));
            ((Holder2) holder).personnel.setText(list.get(position).get(7));

        }

        else if (holder instanceof Holder3){
            ((Holder3) holder).classnum.setText(list.get(position).get(0));
            ((Holder3) holder).classname.setText(list.get(position).get(1));
            ((Holder3) holder).division.setText("("+list.get(position).get(2)+")");
            ((Holder3) holder).credit.setText(list.get(position).get(3));
            ((Holder3) holder).professor.setText(list.get(position).get(4));
            ((Holder3) holder).classroom.setText(list.get(position).get(5));
            ((Holder3) holder).reserve.setText(list.get(position).get(6));
            ((Holder3) holder).personnel.setText(list.get(position).get(7));

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}



