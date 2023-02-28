package sn.mona.monafinalpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sn.mona.monafinalpro.Data.Medecine;
import sn.mona.monafinalpro.Data.MedecineAdapter;

public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtn;//
    private ListView dyn;
    private Button ntnCancel;
    private SearchView searchView;
    private CheckBox chIng;
    private CheckBox chSymp;
    private CheckBox chSickness;
    private CheckBox chUse;
    private CheckBox chName;
    //تعريف الوسيط
    MedecineAdapter medcineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //3.2 بناء الوسيط
        medcineAdapter = new MedecineAdapter(this);
        //تجهيز مؤشر لقائمة العرض
        dyn = findViewById(R.id.dyn);
        //3.3 ربط قائمة العرض بالوسيط
        dyn.setAdapter(medcineAdapter);
        ///تشغيل مراقب(ليسينير) لاي نغيير على قاعدة البيانات
        //        ويقوم بتنظيف المعطيات الموجه(حذفها)وتنزيل المعلومات الجديدة
        readMedcineFromFireBase();

        searchView=findViewById(R.id.search);
        searchView.setActivated(true);
        searchView.setQueryHint("Type your keyword here");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        chIng=findViewById(R.id.chIng);
        chSymp=findViewById(R.id.chSymp);
        chSickness=findViewById(R.id.chSickness);
        chUse=findViewById(R.id.chUse);
        chName=findViewById(R.id.chName);

        medcineAdapter.setChoises(chName,chUse,chIng,chSickness,chSymp);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                medcineAdapter.getFilter().filter(newText);

                return false;
            }
        });
        imgbtn = findViewById(R.id.imgbtn);


        //الانتقال من main activity الى addTask عند الضغط على زر الزائد
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddMedecine.class);
                startActivity(i);
            }
        });

    }

    public CheckBox getChIng() {
        return chIng;
    }

    public CheckBox getChSymp() {
        return chSymp;
    }

    public CheckBox getChSickness() {
        return chSickness;
    }

    public CheckBox getChUse() {
        return chUse;
    }

    public CheckBox getChName() {
        return chName;
    }

    private void readMedcineFromFireBase() {
        //مؤشر لجذر قاعدة البيانات التابعة للمشروع

        //استخراج الرقم المميز للمهمة
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("medcine");//listener لمراقبة اي تغيير يحدث تحت الجذر المحدد
// اي تغيير بقيمة صفة او حذف او اضافة كائن يتم اعلام ال listener
        // عندما يتم تنزيل كل المعطيات الموجودة تحت الجذر
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //remove all tasks from adapter
                medcineAdapter.clear();
                for (DataSnapshot d : snapshot.getChildren())//يمر على جميع قيم مبنى المعطيات  d
                {

                    Medecine m = d.getValue(Medecine.class);//استخراج الكائن المحفوظ
                   // if(m.getSymptoms().contains(toSearch))
                       medcineAdapter.add(m);//اضافة الكائن للوسيط
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * لبناء واضافة قائمة
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * لمعالجة حدث اختيار مركب من مركبات المنيو
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itmSetings) {
            Intent i = new Intent(MainActivity.this, Settings.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.itmine) {
            Intent i = new Intent(MainActivity.this, MineActivity.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.itmSignOut) {
            //1تجهيز البناء للديالوج
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Signing out");
            builder.setMessage("are you sure");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //اخفاء الديالوج
                    dialogInterface.dismiss();
                    //تسجيل الخروج من الحساب
                    FirebaseAuth.getInstance().signOut();
                    // الخروج من الشاشه
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //
                    dialogInterface.cancel();
                }
            });
            //بناء الديالوج
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;

    }

}


