package sn.mona.monafinalpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private Button btnup;
    private Button btnin;
    TextInputEditText EtPass,Etemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //يبني واجهة المستعمل بحيث يبني كل الكائنات الموجودة بملف التنسيق
        btnup=findViewById(R.id.btnup);
        btnin=findViewById(R.id.btnin);
        Etemail=findViewById(R.id.Etemail);
        EtPass=findViewById(R.id.EtPass);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignIn.this,SignUp.class);
                startActivity(i);

            }
        });
        btnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSave();
            }
        });
    }

    private void checkAndSave() {
        String email=Etemail.getText().toString();
        String pass=EtPass.getText().toString();
        boolean isOk=true;
        if (email.length()==0)
        {
            Etemail.setError("enter your email");
            isOk=false;
        }
        if (pass.length()==0)
        {
            EtPass.setError("enter your password");
            isOk=false;
        }
        if (email.indexOf('@')<=0)
        {
            Etemail.setError("wrong email");
            isOk=false;
        }
        if (pass.length()<7)
        {
            EtPass.setError("password at least 7 character");
            isOk=false;
        }
        //******
        if (isOk)
        {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SignIn.this, "successful", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(SignIn.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SignIn.this, "Not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}