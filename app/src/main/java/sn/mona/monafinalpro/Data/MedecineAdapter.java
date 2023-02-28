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
import android.widget.CheckBox;
import android.widget.Filter;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sn.mona.monafinalpro.AddMedecine;
import sn.mona.monafinalpro.MainActivity;
import sn.mona.monafinalpro.MineActivity;
import sn.mona.monafinalpro.R;

public class MedecineAdapter extends ArrayAdapter<Medecine> {
    //search 5:
    private ValueFilter valueFilter;
    List<String> mData;
    List<String> mStringFilterList;


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
        //edit and delete فقط لصاحب المعطيات
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(medecine.getOwner())==false)
        {
            bedit.setVisibility(View.GONE);
            imgbtndel.setVisibility(View.GONE);
        }
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
    //search 1:

    @NonNull
    @Override
    public Filter getFilter() {
        //search 4:
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;

    }

    public void setChoises(CheckBox chName, CheckBox chUse, CheckBox chIng, CheckBox chSickness, CheckBox chSymp)
    {
    }

    //search 2:                    ///abstract class
    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence toSearch) {
            FilterResults results = new FilterResults();
            List<String> all = new ArrayList<>();
            if (toSearch != null && toSearch.length() > 0) {
                List<String> filterList = new ArrayList<>();
                //لكي يبين كل الي بالليست عندما لا يوجد بحث

                //search 3:
                //استخراج الرقم المميز للمهمة
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("medcine");//listener لمراقبة اي تغيير يحدث تحت الجذر المحدد
                // اي تغيير بقيمة صفة او حذف او اضافة كائن يتم اعلام ال listener
                // عندما يتم تنزيل كل المعطيات الموجودة تحت الجذر
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clear();
                        for (DataSnapshot d : snapshot.getChildren())//يمر على جميع قيم مبنى المعطيات  d
                        {
                            String[] s = toSearch.toString().split(" ");//قسم جملة البحث لكلمات
                            Medecine m = d.getValue(Medecine.class);//استخراج الكائن المحفوظ
                            // if(m.getSymptoms().contains(toSearch))
                            int count=0;
                            for (int i = 0; i < s.length ; i++) {
                                MainActivity mainActivity = (MainActivity) getContext();

                                //طريقة البحث عن طريق تشيك بوكس
                                if (mainActivity.getChSymp().isChecked() && m.getSymptoms().toLowerCase().contains(s[i].toLowerCase())) {
                                    count++;
                                    filterList.add(m.getSymptoms());
                                }
                                if (mainActivity.getChName().isChecked() && m.getName().toLowerCase().contains(s[i].toLowerCase())) {
                                    count++;
                                    filterList.add(m.getName());
                                }
                                if (mainActivity.getChSickness().isChecked() && m.getSickness().toLowerCase().contains(s[i].toLowerCase())) {
                                    count++;
                                    filterList.add(m.getSickness());
                                }
                                if (mainActivity.getChIng().isChecked() && m.getIngredients().toLowerCase().contains(s[i].toLowerCase())) {
                                    {
                                        count++;
                                        filterList.add(m.getIngredients());
                                    }
                                }
                                if (mainActivity.getChUse().isChecked() && m.getUse().toLowerCase().contains(s[i].toLowerCase()))
                                {
                                    count++;
                                    filterList.add(m.getUse());
                                }
                            }

                            if (count>0) {
                                add(m);//اضافة الكائن للوسيط
                              //  filterList.add(m.getSymptoms());
                            }
                            else
                            {
                                all.add(m.getSymptoms());
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = all.size();
                results.values = all;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //search 6:
            mData = (List<String>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
