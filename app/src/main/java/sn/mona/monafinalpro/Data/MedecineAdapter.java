package sn.mona.monafinalpro.Data;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import sn.mona.monafinalpro.AddMedecine;
import sn.mona.monafinalpro.R;

public class MedecineAdapter extends ArrayAdapter<Medecine> {
    //تخزين المهام بمبنى معطيات مصفوفة
    public MedecineAdapter(@NonNull Context context) {
        super(context, R.layout.medecine_item);
    }
    //عرض معطيات في واجهة


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem = LayoutInflater.from(getContext()).inflate(R.layout.medecine_item, parent, false);
        TextView tvSickness=vitem.findViewById(R.id.tvSickness);
        TextView tvHowtouse=vitem.findViewById(R.id.tvHowtouse);
        TextView tvContents=vitem.findViewById(R.id.tvContents);
        TextView tvSymp=vitem.findViewById(R.id.tvSymp);
        TextView tvName=vitem.findViewById(R.id.tvName);
        RatingBar rb=vitem.findViewById(R.id.rb);
        ImageButton bedit=vitem.findViewById(R.id.bedit);

         Button imgbtndel=vitem.findViewById(R.id.imgbtndel);

        ImageView imgbtnmed=vitem.findViewById(R.id.imgbtnmed);
        final Medecine medecine=getItem(position);
        downloadImageToLocalFile(medecine.getImage(),imgbtnmed);
        tvSickness.setText(medecine.getSickness());
        tvContents.setText(medecine.getContents());
        tvHowtouse.setText(medecine.getUse());
        tvSymp.setText(medecine.getSymptoms());
        tvName.setText(medecine.getName());

imgbtndel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FirebaseDatabase.getInstance().getReference().child("medcine").child(medecine.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "unsuccessfully", Toast.LENGTH_SHORT).show();

                }
            }

            });
        }
    });
bedit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(getContext(), AddMedecine.class);
        i.putExtra("Medicine",medecine);
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
    }
});

        return vitem;
    }
    //تنزيل الصورة من الفايربيس وتعرضها على الفاينل
    private void downloadImageToLocalFile(String fileURL, final ImageView toView) {
        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileURL);
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");


            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Toast.makeText(getContext(), "downloaded Image To Local File", Toast.LENGTH_SHORT).show();
                    toView.setImageURI(Uri.fromFile(localFile));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(getContext(), "onFailure downloaded Image To Local File "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
