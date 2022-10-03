package sn.mona.monafinalpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    //1.تعريف الصفات
    private TextInputEditText etemail,etpaasword,etconfirm;
    private Button btnSave,btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //2.
        etemail=findViewById(R.id.etemail);
        etpaasword=findViewById(R.id.etpassword);
        etconfirm=findViewById(R.id.etconfirm);
        btnSave=findViewById(R.id.btnsave);
        btncancel=findViewById(R.id.btncancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSave();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSave();
            }
        });
    }
    //تستعمل للدخول والتسجيل و الخروج ,sign in up and out
    private void checkAndSave() {
        String email=etemail.getText().toString();
        String pass=etpaasword.getText().toString();
        String passConf=etconfirm.getText().toString();

        boolean isok=true;
        if (email.length()*pass.length()*passConf.length()==0)
        {
            etemail.setError("one of the files is empty");
            isok=false;
        }
        if (pass.equals(passConf)==false)
        {
            etconfirm.setError("is not equal to password");
            isok=false;
        }
        if (isok)
        {
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                /**
                 * معالجة حدث عندما تكون المهمة ومعلومات عن الحدث
                 */
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //فحص اذا بناء حساب ناجح
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SignUp.this, "cereation succrssfuly", Toast.LENGTH_SHORT).show();
                        //اغلاق الشاشة الحالية
                        finish();
                    }
                    else
                        Toast.makeText(SignUp.this, "cereation failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}