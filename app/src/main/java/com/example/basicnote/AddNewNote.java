package com.example.basicnote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.basicnote.database.NoteDatabase;
import com.example.basicnote.models.Note;

import java.util.List;

public class AddNewNote extends AppCompatActivity {
    private EditText edtTitle, edtDesc;
    private CheckBox checkBoxDone;

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        ImageButton imbBack = findViewById(R.id.action_bar_back);
        imbBack.setOnClickListener(v -> finish());

        //invisible app bar button
        ImageButton imbSearch = findViewById(R.id.action_bar_search);
        imbSearch.setVisibility(View.INVISIBLE);
        ImageButton imbDeleteAll = findViewById(R.id.action_bar_delete);
        imbDeleteAll.setVisibility(View.INVISIBLE);


        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDescription);
        checkBoxDone = findViewById(R.id.checkboxDone2);
        Button btnAdd = findViewById(R.id.btnAdd);

        note = (Note) getIntent().getSerializableExtra("object_note");
        System.out.println(note);
        if (note != null) {
            edtTitle.setText(note.getTitle());
            edtDesc.setText(note.getDesc());
            checkBoxDone.setChecked(note.getDone());
            btnAdd.setText("Edit");
        }

        btnAdd.setOnClickListener(view -> {
            // validate
            if (edtTitle.getText().toString().trim().length() == 0) {
                edtTitle.setError("Title is required!");
                return;
            }
            if (edtTitle.getText().toString().trim().length() > 50) {
                edtTitle.setError("The length of the title must not exceed 50 characters!");
                return;
            }
            if (edtDesc.getText().toString().trim().length() == 0) {
                edtDesc.setError("Description is required!");
                return;
            }

            Intent intent = new Intent();

            String title = edtTitle.getText().toString().replaceAll("\\s+", " "); //remove duplicate whitespaces
            String desc = edtDesc.getText().toString().replaceAll("\\s+", " ");

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
                return;
            }

            if (note == null) { //add
                Note note = new Note(title, desc, checkBoxDone.isChecked());
                if (isNoteExist(note)) {
                    Toast.makeText(AddNewNote.this, "Note exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                NoteDatabase.getInstance(AddNewNote.this).noteDAO().insertNote(note);
                Toast.makeText(AddNewNote.this, "Add note successfully", Toast.LENGTH_SHORT).show();
            } else { //update
                note.setTitle(title);
                note.setDesc(desc);
                note.setDone(checkBoxDone.isChecked());
                NoteDatabase.getInstance(AddNewNote.this).noteDAO().updateNote(note);
                Toast.makeText(AddNewNote.this, "Update note successfully", Toast.LENGTH_SHORT).show();
            }

            edtTitle.setText("");
            edtDesc.setText("");

            hideSoftKeyboard();

            setResult(Activity.RESULT_OK, intent);
            AddNewNote.super.onBackPressed();
        });
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isNoteExist(Note note) {
        List<Note> notes = NoteDatabase.getInstance(AddNewNote.this).noteDAO().checkNote(note.getTitle(), note.getDesc());
        return notes != null && !notes.isEmpty();
    }
}