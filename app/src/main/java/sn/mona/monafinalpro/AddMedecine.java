package sn.mona.monafinalpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import android.provider.MediaStore.Video.Thumbnails;

public class AddMedecine extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 100;
    private static final int PERMISSION_CODE = 101;
    private static final int VIDEO_PICK_CODE = 102;
    private TextInputEditText EtName, EtUse,EtSickness,EtSymp,EtIngre;
    private ImageView imgbtncam,imgVideo;
    private Button btnSave, btnCancel;
    private Boolean toEdit=false;
    private String imp;
    private Button btnUpload;
    private Uri filePath;
    //عنوان الصورة في الهاتف بعد اختيارها
    private Uri toUploadimageUri;
    private Uri toUploadvideoUri;
    //عنوان الصورة في الفايربيس
    private Uri downladuri;
    private Uri downladurivid;
    private   Medecine m = new Medecine();
    // thread مقطع برنامج يعمل بالتوازي او بالتزامن مع التطبيق
    //لرفع الصورة او الملف على الموقعfirebase storage
    StorageTask uploadTask;


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
        imgVideo=findViewById(R.id.imgVideo);
        btnCancel = findViewById(R.id.btnCancelAdd);
        btnSave = findViewById(R.id.btnSaveAdd);



//        SharedPreferences preferences=getSharedPreferences("mypref",MODE_PRIVATE);
//        String key = preferences.getString("key", "");
//        if(key.length()==0)
//        {
//            Toast.makeText(this, "No key found", Toast.LENGTH_SHORT).show();
//
//        }
//        else
//        {
//            Toast.makeText(this, "key:"+key, Toast.LENGTH_SHORT).show();
//        }
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
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                Toast.makeText(getApplicationContext(), "video", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions,PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickVideoFromGallery();
                    }

                }

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //في صورة لكن بعدني مش رافعها
                if (toUploadimageUri!= null && downladuri==null)
                {
                    uploadImage(toUploadimageUri);
                }
                else
                if (toUploadvideoUri!= null && downladurivid==null)
                {
                    uploadVideo(toUploadvideoUri);
                }
                else
                {
                    checkAndSave();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
//        imgbtncam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imageChooser();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent()!=null && getIntent().hasExtra("Medicine")){
            toEdit=true;
            m=(Medecine) getIntent().getExtras().get("Medicine");
            btnSave.setText("update");
            EtName.setText(m.getName());
            EtUse.setText(m.getUse());
            EtSickness.setText(m.getSickness());
            EtSymp.setText(m.getSymptoms());
            EtIngre.setText(m.getContents());

        }
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
            if(uploadTask !=null || (uploadTask !=null && uploadTask.isInProgress()))
            {
                Toast.makeText(this, " uploadTask.isInProgress(", Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage(toUploadimageUri);



        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
//    private void createTask(Medecine m) {
//        //.1
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        //.2
//        DatabaseReference reference =
//                database.getReference();
//        //to get the user uid (or other details like email)
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        String uid = auth.getCurrentUser().getUid();
//        m.setOwner(uid);
//
//        String key = reference.child("tasks").push().getKey();
//        m.setKey(key);
//        reference.child("tasks").child(uid).child(key).setValue(m).
//                addOnCompleteListener(AddMedecine.this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(AddMedecine.this, "add successful", Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            Toast.makeText(AddMedecine.this, "add failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            task.getException().printStackTrace();
//                        }
//
//                    }
//                });
//    }
    private void uploadImage(Uri fileP) {

        if(fileP != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            //
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            uploadTask =ref.putFile(fileP)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downladuri = task.getResult();
                                    m.setImage(downladuri.toString());
                                    if (toUploadvideoUri!= null && downladurivid==null)
                                    {
                                        uploadVideo(toUploadvideoUri);
                                    }else
                                    checkAndSave();
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
        //    createTask(m);
        }
    }
        private void pickImageFromGallery() {
            //intent to pick image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        }
        private void pickVideoFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, VIDEO_PICK_CODE);
    }
    private void uploadVideo(Uri fileP) {

        if(fileP != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            //
            final StorageReference ref = storageReference.child("videos/"+ UUID.randomUUID().toString());
            uploadTask =ref.putFile(fileP)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downladurivid = task.getResult();
                                    m.setVideo(downladurivid.toString());
                                    checkAndSave();
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
            m.setVideo("");
            //    createTask(m);
        }
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

        m.setSickness(sickness);
        m.setUse(use);
        m.setName(name);
        m.setSymptoms(symptoms);
        m.setIngredients(ingredients);
        //استخراج الرقم المميز للمستعمل UID
        //مستخدم دخل مسبقا
        String owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
        m.setOwner(owner);
        //استخراج الرقم المميز للمهمة
        String Key=m.getKey();
        if (toEdit==false){
           Key = FirebaseDatabase.getInstance().getReference().child("medcine").push().getKey();
        }

        m.setKey(Key);

        FirebaseDatabase.getInstance().getReference().child("medcine").child(Key).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
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
//    // this function is triggered when user
//    // selects the image from the imageChooser
//    public void onActivityResult1(int requestCode,int resultCode,Intent data)
//    {
//        AddMedecine.super.onActivityResult(requestCode,resultCode,data);
//
//        if (resultCode==RESULT_OK)
//        {
//            // compare the resultCode with the
//            // SELECT_PICTURE constant
//            if (requestCode==200)
//            {
//                // Get the url of the image from data
//                Uri selectedImageUri=data.getData();
//                if (null!=selectedImageUri)
//                {
//                    // update the preview image in the layout
//                    imgbtncam.setImageURI(selectedImageUri);
//                }
//            }
//        }
//    }
    //handle result of picked images
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){
            //set image to image view
            toUploadimageUri = data.getData();
            imgbtncam.setImageURI(toUploadimageUri);
        }
        if (resultCode==RESULT_OK && requestCode== VIDEO_PICK_CODE){
            //set image to image view
            toUploadvideoUri = data.getData();



            Bitmap bmThumbnail;
            MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
            mMMR.setDataSource(getApplicationContext(), toUploadvideoUri);
            bmThumbnail = mMMR.getFrameAtTime();
//MICRO_KIND, size: 96 x 96 thumbnail
            //bmThumbnail = ThumbnailUtils.createVideoThumbnail(toUploadvideoUri.getPath(), Thumbnails.MICRO_KIND);
            imgVideo.setImageBitmap(bmThumbnail);

//// MINI_KIND, size: 512 x 384 thumbnail
//            bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);
//            imgVideo.setImageBitmap(bmThumbnail);
//            imgbtncam.setImageURI(toUploadvideoUri);
        }
    }

}
