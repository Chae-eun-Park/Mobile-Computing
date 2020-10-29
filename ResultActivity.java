package com.example.smartvoca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ResultActivity extends AppCompatActivity {
    private TextView tv_result;
    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname"); //보내는 쪽이랑 받는 쪽 "여기"일치시켜주자
        String photourl = intent.getStringExtra("photourl"); //보내는 쪽이랑 받는 쪽 일치시켜주자

        tv_result = findViewById(R.id.tv_result);
        tv_result.setText(nickname); //닉네임 텍스트를 텍스트뷰에 세팅한것

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photourl).into(iv_profile); //가져온 프로필을 이미지 뷰에 세팅
    }
}