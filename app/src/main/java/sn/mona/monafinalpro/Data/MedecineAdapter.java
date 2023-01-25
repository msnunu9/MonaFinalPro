package sn.mona.monafinalpro.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        ImageButton btninfo=vitem.findViewById(R.id.btninfo);
        CheckBox chb=vitem.findViewById(R.id.chb);
        ImageButton imgbtnmed=vitem.findViewById(R.id.imgbtnmed);
        final Medecine medecine=getItem(position);
        tvSickness.setText(medecine.getSickness());
        tvContents.setText(medecine.getContents());
        tvHowtouse.setText(medecine.getUse());
        tvSymp.setText(medecine.getSymptoms());
        tvName.setNa
        chb.setChecked(false);
        return vitem;
    }

}
