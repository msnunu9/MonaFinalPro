package sn.mona.monafinalpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import sn.mona.monafinalpro.Data.Medecine;

public class AddMedecine extends AppCompatActivity {
    private TextInputEditText EtSub,EtTitle;
    private ImageButton imbtn;
    private Button ntnSave,ntnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        EtSub=findViewById(R.id.EtSub);
        EtTitle=findViewById(R.id.EtTitle);
        imbtn=findViewById(R.id.imbtn);
        ntnSave=findViewById(R.id.ntnSave);
        ntnCancel=findViewById(R.id.ntnCancel);

        ntnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSave();
            }
        });

    }
    private void checkAndSave() {
        String title = EtTitle.getText().toString();
        String subj = EtSub.getText().toString();
        //بناء الكائن واعطائه قيم للصفات
        Medecine m=new Medecine()
    }

}