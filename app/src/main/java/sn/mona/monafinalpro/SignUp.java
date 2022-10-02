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
    private TextInputEditText EtEmail,EtPaasword,EtConfirm;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //2.
        EtEmail=findViewById(R.id.EtEmail);
        EtPaasword=findViewById(R.id.EtPassword);
        EtConfirm=findViewById(R.id.Etemail);
        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSave();
            }
        });
    }
    //تستعمل للدخول والتسجيل و الخروج ,sign in up and out
    private void checkAndSave() {
        String email=EtEmail.getText().toString();
        String pass=EtPaasword.getText().toString();
        String passConf=EtConfirm.getText().toString();

        boolean isok=true;
        if (email.length()*pass.length()*passConf.length()==0)
        {
            EtEmail.setError("one of the files is empty");
            isok=false;
        }
        if (pass.equals(passConf)==false)
        {
            EtConfirm.setError("is not equal to password");
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
                        Toast.makeText(SignUp.this, "cereation faild"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}