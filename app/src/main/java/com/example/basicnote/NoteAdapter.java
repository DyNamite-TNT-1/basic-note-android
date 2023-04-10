package com.example.basicnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicnote.models.Note;
import com.example.basicnote.my_interface.IClickItemNoteListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

class NoteRvAdapter extends RecyclerView.Adapter<NoteRvAdapter.ViewHolder> {

    private final ArrayList<Note> arrayList;
    private IClickItemNoteListener iClickItemNoteListener;

    public NoteRvAdapter(ArrayList<Note> arrayList, IClickItemNoteListener listener) {
        this.arrayList = arrayList;
        this.iClickItemNoteListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView tvTitle;
        private final TextView tvDesc;
        private final CheckBox checkBoxDone;

        private final ImageButton btnDel;

        private final LinearLayout layoutItem;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.tvId = view.findViewById(R.id.tvId);
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvDesc = view.findViewById(R.id.tvDesc);
            this.checkBoxDone = view.findViewById(R.id.checkboxDone);
            this.layoutItem = view.findViewById(R.id.layoutNote);
            this.btnDel = view.findViewById(R.id.btnDel);

            checkBoxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    checkBoxDone.setChecked(isChecked);
                    arrayList.get(getAdapterPosition()).setDone(isChecked);
                }
            });

            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickItemNoteListener.onClickItemNote(arrayList.get(getAdapterPosition()));
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickItemNoteListener.onDeleteItemNote(arrayList.get(getAdapterPosition()));
                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = arrayList.get(position);
        if (note == null) {
            return;
        }

        holder.tvId.setText(note.getId());
        holder.tvTitle.setText(note.getTitle());
        holder.tvDesc.setText(note.getDesc());
        holder.checkBoxDone.setChecked(note.getDone());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
