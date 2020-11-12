package com.example.smartvoca;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Myword_main extends AppCompatActivity {
    EditText word_input; //단어, 뜻 입력
    ListView myword_list; //보여주는 리스트뷰
    //FirebaseListAdapter<Content> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 빈 데이터 리스트 생성.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myword_main);
        word_input = (EditText) findViewById(R.id.word_input);
        myword_list = (ListView)findViewById(R.id.myword_list);
        final ArrayList<String> items = new ArrayList<String>();
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items);

        //DB에 내용 넣기
        DatabaseReference contentref = FirebaseDatabase.getInstance().getReference().child("content");

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.myword_list);
        listview.setAdapter(adapter);
        //myword_list.setAdapter(new FirebaseListAdapter<Content>(this, Content.class, android.R.layout.simple_list_item_single_choice, contentref));


        //추가
        final Button addButton = (Button)findViewById(R.id.add) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count;
                count = adapter.getCount();

                // 아이템 추가.
                String text = word_input.getText().toString();
                if(text.length() != 0) {
                    items.add(text);
                    word_input.setText(""); //입력하는 곳 빈칸으로 만들어주기
                    // listview 갱신
                    adapter.notifyDataSetChanged();
                }
            }
        }) ;

        //수정
        final Button modifyButton = (Button)findViewById(R.id.modify) ;
        modifyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked;
                count = adapter.getCount();
                String text = word_input.getText().toString();

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {
                        items.set(checked, text);
                        word_input.setText("");
                        // listview 갱신
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;

        //삭제
        final Button deleteButton = (Button)findViewById(R.id.delete) ;
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        items.remove(checked) ;

                        // listview 선택 초기화.
                        listview.clearChoices();

                        // listview 갱신.
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;

        // edit버튼누르면 add, modify, delete visible설정
        final ImageButton edit = (ImageButton) findViewById(R.id.edit);
        edit.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(addButton.getVisibility() == v.GONE){
                    addButton.setVisibility(v.VISIBLE);
                    modifyButton.setVisibility(v.VISIBLE);
                    deleteButton.setVisibility(v.VISIBLE);
                    word_input.setVisibility(v.VISIBLE);
                    myword_list.setVisibility(v.GONE);

                }else{
                    addButton.setVisibility(v.GONE);
                    modifyButton.setVisibility(v.GONE);
                    deleteButton.setVisibility(v.GONE);
                    word_input.setVisibility(v.GONE);
                    myword_list.setVisibility(v.VISIBLE);
                }
            }
        });
    }
}