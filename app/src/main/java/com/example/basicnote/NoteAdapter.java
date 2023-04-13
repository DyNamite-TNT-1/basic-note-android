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

import java.util.List;

class NoteRvAdapter extends RecyclerView.Adapter<NoteRvAdapter.ViewHolder> {

    private List<Note> notes;

    public void setData(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    private IClickItemNoteListener iClickItemNoteListener;

    public NoteRvAdapter(List<Note> notes, IClickItemNoteListener listener) {
        this.notes = notes;
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
        Note note = notes.get(position);
        if (note == null) {
            return;
        }

        holder.tvId.setText(Integer.toString(note.getId()));
        holder.tvTitle.setText(note.getTitle());
        holder.tvDesc.setText(note.getDesc());
        holder.checkBoxDone.setChecked(note.getDone());

        holder.checkBoxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                holder.checkBoxDone.setChecked(isChecked);
                note.setDone(isChecked);
            }
        });

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemNoteListener.onClickItemNote(note);
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemNoteListener.onDeleteItemNote(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
