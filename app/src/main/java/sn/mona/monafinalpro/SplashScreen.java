package sn.mona.monafinalpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //فتح الشاشة التالية تلقائيا بعد فترة من الزمن
        Handler h=new Handler();
        Runnable r=new Runnable() {
            @Override
            //للانتقال من شاشة الانتظار splash screen لشاشة التسجيل sign in
            public void run() {
                Intent i = new Intent(SplashScreen.this, SignIn.class);
                startActivity(i);  //to start the i which is going from splash activity to sign in
                finish();  //to close the current activity
            }
        };
        h.postDelayed(r,3000);//الانتظار عدد من الثواني

    }
}