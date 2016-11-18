package com.example.joju.myapplication7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    String password = "123";
    int password_changed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        OnClickListener listener  =  new OnClickListener()
        {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText)findViewById(R.id.EmailInput);  //매칭된 레이아웃에서 해당아이디를 찾아 글자박스의 객체 생성
                String userid =  editText.getText().toString();                ///스트링 객체 선언 후 글자박스의 객체의 스트링타입을 넣어줌


                EditText editText2 = (EditText)findViewById(R.id.PasswordInput);  //매칭된 레이아웃에서 해당아이디를 찾아 글자박스의 객체 생성
                String userpassword =  editText2.getText().toString();                ///스트링 객체 선언 후 글자박스의 객체의 스트링타입을 넣어줌


               Intent intent1 = new Intent(MainActivity.this, MusicChartActivity.class);      ////인텐트 객체 하나 생성 후 메모리 할당 후 보내는액티비티와 받는 액티비티 입력

                if(userid.equals("qqq") && userpassword.equals(password))
                {

                    intent1.putExtra("입력한 id", userid);
                    intent1.putExtra("입력한 비번", userpassword);

                    startActivityForResult(intent1,1);

                    Toast.makeText(MainActivity.this, "로그인 성공", LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(MainActivity.this, "로그인 실패", LENGTH_SHORT).show();
            }

        };

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(listener);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1)
        {

            String result = data.getStringExtra("변경 비밀번호");
            password = result;
            password_changed  = 1;
        }

        else
            password_changed = 0;

    }


//
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Toast.makeText(this, "main_onSaveInstanceState", LENGTH_SHORT).show();
//    }


//    protected void onStart() {
//
//        super.onStart();
//        Toast.makeText(this, "main_start", LENGTH_SHORT).show();
//
//    }
//
//
//    protected void onRestart() {
//
//        super.onRestart();
//        Toast.makeText(this, "main_restart", LENGTH_SHORT).show();
//    }
//
//    protected void onPause()
//    {
//        super.onPause();
//        Toast.makeText(this, "main_pause", LENGTH_SHORT).show();
//    }
//
//    protected void onStop()
//    {
//        super.onStop();
//        Toast.makeText(this, "main_stop", LENGTH_SHORT).show();
//    }
//
    protected void onResume() {

        super.onResume();
        TextView password_changed_id = (TextView) findViewById(R.id.password_changed_id);
        password_changed_id.setVisibility(View.INVISIBLE);

        if(password_changed == 1) {
            password_changed_id.setVisibility(View.VISIBLE);
            password_changed = 0;
        }

//        Toast.makeText(this, "main_resume", LENGTH_SHORT).show();


    }

//    protected void onDestroy() {
//
//        super.onDestroy();
//        Toast.makeText(this, "main_destroy", LENGTH_SHORT).show();
//
//    }
//


}
