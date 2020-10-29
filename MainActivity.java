package com.example.smartvoca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

/*부팅시- activity.main (부팅화면)
* 로그인화면으로
* 내 단어장-리스트 선택- 단어보여주는 화면 my_word_list
* etc*/

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
   // private Button btn_go_login; //로그인하는 버튼(activity_login)
    private Button btn_booting; //부팅화면에서 로그인으로 가는 버튼
    private SignInButton btn_google; //구글로그인버튼
    private FirebaseAuth auth; //파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient; //구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) { //앱이 실행될 때 처음 수행되는 곳
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 없애주기 위해 추가

        setContentView(R.layout.booting);
        //화면 전환 - 인텐트 보내기(startActivity)
        //다음 넘어갈 화면 준비 (layout xml, java)
        // -> androidmanifest.xml에 activity등록
        // -> intent 객체만들어서 startActivity한다

        //googlesignin 버튼에서 기본적인 옵션세팅
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체 초기화

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE); //아까만들었던 REQ코드 넣어줌
                //SIGN 인증을 하는 절차를 화면에 넘어가서 인증하고 받아서 다시 넘어오기
            }
        });
    }

    //구글 로그인 인증을 요청했을 때 결과 값을 되돌려 받는 곳
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //req코드가 넘어와서 그 값
        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){ //인증결과가 성공적인가?
                //생략해도 True()됨
                GoogleSignInAccount account = result.getSignInAccount(); //모든데이터가 account여기있음
                resultLogin(account); //로그인결과값 출력수행하라는 메소드
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //최종적으로 구글 로그인 성공했는가?
                        if(task.isSuccessful()){ //로그인이 성공했으면
                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                            //account데이터를 가져는 왔는데 결론적으로 써먹어야함
                            intent.putExtra("nickname", account.getDisplayName());
                            intent.putExtra("photourl", String.valueOf(account.getPhotoUrl())); //포토url을 String.ValueOf로 특정 자료형을 String형태로 변환
                            startActivity(intent);
                        } else{ //로그인이 실패했으면
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
