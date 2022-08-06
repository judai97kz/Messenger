package com.judai.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatRoom extends AppCompatActivity {

    EditText ir,pr;
    Button sr,lo,cr;
    ListView lvr;
    ArrayList<String> listdata1;
    ArrayAdapter<String> adapter1;
    Context context1;
    String tendem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        context1 = this;
        ir = (EditText) findViewById(R.id.ir);
        pr = (EditText) findViewById(R.id.pr);
        sr = (Button) findViewById(R.id.sr);
        cr = (Button) findViewById(R.id.cr);
        lo = (Button) findViewById(R.id.lo);
        lvr = (ListView) findViewById(R.id.lvr) ;
        Intent intent = getIntent();
        String value1 = intent.getStringExtra("key1");
        int value4 = intent.getIntExtra("key4",0);
        String value5 = intent.getStringExtra("key5");
        if(value4 == 1)
        {
            tendem = value5;
        }
        else
        {
            tendem = value1;
        }
        FirebaseDatabase m1 = FirebaseDatabase.getInstance();
        DatabaseReference m2 = m1.getReference();
        listdata1 = new ArrayList<>();

        m2.child("Data/"+tendem+"/join").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String d = snapshot.getValue().toString();
                listdata1.add(d);
                Set<String> set = new HashSet<String>(listdata1);
                List<String> xoatrung = new ArrayList<String>(set);
                adapter1 = new ArrayAdapter<>(context1, android.R.layout.simple_list_item_1,xoatrung);
                lvr.setAdapter(adapter1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tp = ir.getText().toString();
                String mp = pr.getText().toString();
                if(tp.equals("")==true)
                {
                    Toast.makeText(ChatRoom.this,"Hãy Nhập ID phòng Để Tạo !",Toast.LENGTH_SHORT).show();
                }else
                {
                    m2.child("Room/"+tp+"/id").setValue(tp);
                    m2.child("Room/"+tp+"/pass").setValue(mp);
                    m2.child("Room/"+tp+"/chat").setValue("");
                    m2.child("Data/"+tendem+"/join").push().setValue(tp);
                    adapter1.notifyDataSetChanged();
                    Intent i2 = new Intent(ChatRoom.this,Chat.class);
                    i2.putExtra("key2",tp);
                    i2.putExtra("key3",tendem);
                    startActivity(i2);
                }
            }
        });
        sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tp = ir.getText().toString();
                String mp = pr.getText().toString();
                if(tp.equals("")==true)
                {
                    Toast.makeText(ChatRoom.this,"Hãy Nhập ID Và Mật Khẩu Phòng Để Tham Gia !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    m2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("Room/"+tp))
                            {
                                m2.child("Room/"+tp+"/pass").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String checkp = snapshot.getValue().toString();
                                        if (mp.equals(checkp)==true)
                                        {
                                            m2.child("Data/"+tendem+"/join").push().setValue(tp);
                                            adapter1.notifyDataSetChanged();
                                            Intent i2 = new Intent(ChatRoom.this,Chat.class);
                                            i2.putExtra("key2",tp);
                                            i2.putExtra("key3",tendem);
                                            startActivity(i2);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(ChatRoom.this,"Phòng Không Tồn Tại !",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
        lvr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a = (String) adapter1.getItem(i);
                Intent i2 = new Intent(ChatRoom.this,Chat.class);
                i2.putExtra("key2",a);
                i2.putExtra("key3",tendem);
                startActivity(i2);
            }
        });
        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(ChatRoom.this,MainActivity.class);
                startActivity(i3);
            }
        });



    }
}