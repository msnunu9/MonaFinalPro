package sn.mona.monafinalpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import sn.mona.monafinalpro.Data.Medecine;

public class AddMedecine extends AppCompatActivity {
    private TextInputEditText EtName, EtUse,EtSickness,EtSymp,EtIngre;
    private ImageButton imgbtncam;
    private Button btnSave, btnCancel;
    private String imp;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medecine);
        EtName = findViewById(R.id.EtName);
        EtUse = findViewById(R.id.EtUse);
        EtSickness=findViewById(R.id.EtSickness);
        EtSymp=findViewById(R.id.EtSymp);
        EtIngre=findViewById(R.id.EtIngre);
        imgbtncam = findViewById(R.id.imbtn);
        btnCancel = findViewById(R.id.btnCancelAdd);
        btnSave = findViewById(R.id.btnSaveAdd);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSave();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }


    /**
     *
     */
    private void checkAndSave() {
        String name = EtName.getText().toString();
        String use = EtUse.getText().toString();
        String sickness=EtSickness.getText().toString();
        String symptoms=EtSymp.getText().toString();
        String ingredients=EtIngre.getText().toString();

        //بناء الكائن واعطائه قيم للصفات
        Medecine m = new Medecine();
        m.setSickness(m.getSickness());
        m.setUse(m.getUse());
        m.setName(m.getName());
        m.setSymptoms(m.getSymptoms());
        m.setIngredients(m.getIngredients());
        //استخراج الرقم المميز للمستعمل UID
        //مستخدم دخل مسبقا
        String owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
        m.setOwner(owner);
        //استخراج الرقم المميز للمهمة
        String key = FirebaseDatabase.getInstance().getReference().child("medcine").push().getKey();
        m.setKey(key);

        FirebaseDatabase.getInstance().getReference().child("medcine").child(key).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(AddMedecine.this, "add succecfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMedecine.this, "add failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgbtncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }
}