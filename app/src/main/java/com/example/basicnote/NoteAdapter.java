package com.example.basicnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.basicnote.models.Note;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Note> arrayList;

    public NoteAdapter(Context context, ArrayList<Note> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_note, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Note note = arrayList.get(position);
        viewHolder.tvId.setText(note.getId());
        viewHolder.tvTitle.setText(note.getTitle());
        viewHolder.tvDesc.setText(note.getDesc());
        viewHolder.checkBoxDone.setChecked(note.getDone());
        viewHolder.checkBoxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                viewHolder.checkBoxDone.setChecked(isChecked);
                arrayList.get(position).setDone(isChecked);
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        private final TextView tvId;
        private final TextView tvTitle;
        private final TextView tvDesc;
        private final CheckBox checkBoxDone;

        public ViewHolder(@NonNull View view) {
            this.tvId = view.findViewById(R.id.tvId);
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvDesc = view.findViewById(R.id.tvDesc);
            this.checkBoxDone = view.findViewById(R.id.checkboxDone);
        }
    }
}
