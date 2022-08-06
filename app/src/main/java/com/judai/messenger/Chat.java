package com.judai.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    EditText ed1;
    Button gui,back;
    ListView lv;
    ArrayList<String> listdata;
    ArrayAdapter<String> adapter;
    Context context;
    FirebaseDatabase m1 = FirebaseDatabase.getInstance();
    DatabaseReference m2 = m1.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;
        ed1 = (EditText) findViewById(R.id.ed);
        gui = (Button) findViewById(R.id.gui);
        lv = (ListView) findViewById(R.id.lv);
        back = (Button) findViewById(R.id.bk);
        Intent intent = getIntent();
        String value2 = intent.getStringExtra("key2");
        String value3 = intent.getStringExtra("key3");
        listdata = new ArrayList<>();
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,listdata);
        m2.child("Room/"+value2+"/chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String d = snapshot.getValue().toString();
                listdata.add(d);
                lv.setAdapter(adapter);
                //Hien thi cuoi danh sach
                lv.post(new Runnable() {
                    @Override
                    public void run() {
                        lv.setSelection(lv.getCount()-1);
                    }
                });
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
        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = ed1.getText().toString();
                if(c.equals("")==true)
                {   m2.child("Room/"+value2+"/chat").push().setValue(value3 + " : \uD83D\uDC4D" );
                    ed1.setText("");
                }
                else {
                    m2.child("Room/"+value2+"/chat").push().setValue(value3 + " : " + c);
                    adapter.notifyDataSetChanged();
                    ed1.setText("");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Chat.this,ChatRoom.class);
                intent1.putExtra("key4",1);
                intent1.putExtra("key5",value3);
                startActivity(intent1);
            }
        });
    }
}