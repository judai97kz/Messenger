package com.judai.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    User userdata;
    EditText us,ps;
    Button dk,dn;
    FirebaseDatabase m1 = FirebaseDatabase.getInstance();
    DatabaseReference m2 = m1.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        us = (EditText) findViewById(R.id.us);
        ps = (EditText) findViewById(R.id.ps);
        dn = (Button) findViewById(R.id.dn);
        dk = (Button) findViewById(R.id.dk);
        userdata = new User(this,"Userdt.sqlite",null,1);
        userdata.QueryData("CREATE TABLE IF NOT EXISTS NguoiDung(Name VARCHAR(50) PRIMARY KEY,Pass VARCHAR(50))");

        Cursor data = userdata.getData("SELECT * FROM NguoiDung");
        while(data.moveToNext()){
            String ten = data.getString(0);
            String mk = data.getString(1);
            if(ten.equals("")==true || mk.equals("")==true)
            {
                Toast.makeText(MainActivity.this,"Hãy nhập thông tin !",Toast.LENGTH_LONG).show();
            }
            else {
                m2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("Data/" + ten)) {
                            m2.child("Data/" + ten + "/name").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String ct = snapshot.getValue().toString();
                                    m2.child("Data/" + ten + "/pass").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String cp = snapshot.getValue().toString();
                                            if (mk.equals(cp) == true && ten.equals(ct) == true) {
//                                                Intent i1 = new Intent(MainActivity.this, ChatRoom.class);
//                                                i1.putExtra("key1", ten);
//                                                startActivity(i1);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
        dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tk = us.getText().toString();
                String mk = ps.getText().toString();
                UserData ust = new UserData(tk,mk);
                if(tk.equals("")==true || mk.equals("")==true)
                {
                    Toast.makeText(MainActivity.this,"Hãy Nhập Đầy Đủ Thông Tin",Toast.LENGTH_LONG).show();
                }
                else {
                    m2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("Data/" + tk)) {
                                m2.child("Data/" + tk + "/name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String ct = snapshot.getValue().toString();
                                        m2.child("Data/" + tk + "/pass").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String cp = snapshot.getValue().toString();
                                                if (mk.equals(cp) == true && tk.equals(ct) == true) {
                                                    userdata.QueryData("INSERT INTO NguoiDung VALUES('"+ tk +"','"+ mk +"')");
                                                    Intent i1 = new Intent(MainActivity.this, ChatRoom.class);
                                                    i1.putExtra("key1", tk);
                                                    startActivity(i1);
                                                }
                                                else
                                                {
                                                    Toast.makeText(MainActivity.this, "Mật Khẩu Hoặc Không Đúng!", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Tên Đăng Nhập Không Đúng!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tk = us.getText().toString();
                String mk = ps.getText().toString();
                m2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Data/"+tk))
                        {
                            Toast.makeText(MainActivity.this,"Tên Đăng Ký Đã Có Người Sử Dụng !",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Tài khoản đăng ký thành công !",Toast.LENGTH_LONG).show();
                            m2.child("Data/"+tk+"/name").setValue(tk);
                            m2.child("Data/"+tk+"/pass").setValue(mk);
                            m2.child("Data/"+tk+"/join").push().setValue("Phong chung");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }



}