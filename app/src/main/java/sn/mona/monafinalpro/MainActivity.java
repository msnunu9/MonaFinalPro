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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imgbtn=findViewById(R.id.imgbtn);


        //الانتقال من main activity الى addTask عند الضغط على زر الزائد
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AddMedecine.class);
                startActivity(i);
            }
        });
    }

    /**
     *لبناء واضافة قائمة
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    /**
     * لمعالجة حدث اختيار مركب من مركبات المنيو
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.itmSetings)
        {
            Intent i=new Intent(MainActivity.this, Settings.class);
            startActivity(i);
        }
        if (item.getItemId()==R.id.itmSignOut)
        {
            //1تجهيز البناء للديالوج
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
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
            AlertDialog dialog=builder.create();
            dialog.show();

        }
        return true;
    }

    }

}