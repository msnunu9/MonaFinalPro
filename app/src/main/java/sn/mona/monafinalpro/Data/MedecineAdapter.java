package sn.mona.monafinalpro.Data;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import sn.mona.monafinalpro.R;

public class MedecineAdapter extends ArrayAdapter<Medecine>
{
    //تخزين المهام بمبنى معطيات مصفوفة
    public MedecineAdapter(@NonNull Context context) {
        super(context, R.layout.medecine_item);
    }
    //عرض معطيات في واجهة
    public View getView
}
