package sn.mona.monafinalpro;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import sn.mona.monafinalpro.Data.Medecine;

public class AddMedecine extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 100;
    private static final int PERMISSION_CODE = 101;
    private TextInputEditText EtName, EtUse,EtSickness,EtSymp,EtIngre;
    private ImageButton imgbtncam;
    private Button btnSave, btnCancel;
    private String imp;
    private Button btnUpload;
    private Uri filePath;
    private Uri toUploadimageUri;
    private Uri downladuri;
    private Medecine m;
    StorageTask uploadMedcine;


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
        imgbtncam = findViewById(R.id.imgbtncam);
        btnCancel = findViewById(R.id.btnCancelAdd);
        btnSave = findViewById(R.id.btnSaveAdd);
        btnUpload=findViewById(R.id.btnUpload);


        SharedPreferences preferences=getSharedPreferences("mypref",MODE_PRIVATE);
        String key = preferences.getString("key", "");
        if(key.length()==0)
        {
            Toast.makeText(this, "No key found", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this, "key:"+key, Toast.LENGTH_SHORT).show();
        }
        //upload: 4
        imgbtncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions,PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }

                }

            }
        });


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
        imgbtncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

    }
    private void dataHandler() {
        boolean isok=true;
        String ingredients=EtIngre.getText().toString();
        String symptoms=EtSymp.getText().toString();
        String sickness=EtSickness.getText().toString();
        String use=EtUse.getText().toString();
        String name=EtName.getText().toString();

//        String text=etText.getText().toString();
//        String  date=etDueDate.getText().toString();
        if(name.length()==0)
        {
            EtName.setError("name can not be empty");
            isok=false;

        }
        if(use.length()==0)
        {
            EtUse.setError("name can not be empty");
            isok=false;

        }
        if(sickness.length()==0)
        {
            EtSickness.setError("name can not be empty");
            isok=false;

        }
        if(symptoms.length()==0)
        {
            EtSymp.setError("name can not be empty");
            isok=false;

        }
        if(ingredients.length()==0)
        {
            EtIngre.setError("name can not be empty");
            isok=false;

        }



//        if(text.length()==0)
//        {
//            etText.setError("Text can not be empty");
//            isok=false;
//
//        }
        if(isok)
        {
            m=new Medecine();
            m.setName(name);
            m.setUse(use);
            m.setSickness(sickness);
            m.setSymptoms(symptoms);
            m.setIngredients(ingredients);
            //createTask(t);
            if(uploadMedcine!=null || (uploadMedcine!=null && uploadMedcine.isInProgress()))
            {
                Toast.makeText(this, " uploadTask.isInProgress(", Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage(toUploadimageUri);



        }


    }
    private void createTask(Medecine m) {
        //.1
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //.2
        DatabaseReference reference =
                database.getReference();
        //to get the user uid (or other details like email)
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        m.setOwner(uid);

        String key = reference.child("tasks").push().getKey();
        m.setKey(key);
        reference.child("tasks").child(uid).child(key).setValue(m).
                addOnCompleteListener(AddMedecine.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddMedecine.this, "add successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddMedecine.this, "add failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }

                    }
                });
    }
    private void uploadImage(Uri fileP) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            //
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            uploadMedcine=ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downladuri = task.getResult();
                                    m.setImage(downladuri.toString());
                                    createTask(m);

                                }
                            });

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else
        {
            m.setImage("");
            createTask(m);
        }
    }
        private void pickImageFromGallery() {
            //intent to pick image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
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





    }
    private void imageChooser()
    {
        // create an instance of the
        // intent of the type image
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i,"select picture"),200);

    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        AddMedecine.super.onActivityResult(requestCode,resultCode,data);

        if (resultCode==RESULT_OK)
        {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode==200)
            {
                // Get the url of the image from data
                Uri selectedImageUri=data.getData();
                if (null!=selectedImageUri)
                {
                    // update the preview image in the layout
                    imgbtncam.setImageURI(selectedImageUri);
                }
            }
        }
    }
}