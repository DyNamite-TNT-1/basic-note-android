package com.example.basicnote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddNewNote extends AppCompatActivity {
    private EditText edtTitle, edtDesc;
    private Button btnAdd;
    private CheckBox checkBoxDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDescription);
        btnAdd = findViewById(R.id.btnAdd);
        checkBoxDone = findViewById(R.id.checkboxDone2);

        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        boolean isDone = getIntent().getBooleanExtra("done", false);
        if (id != null) {
            edtTitle.setText(title);
            edtDesc.setText(desc);
            btnAdd.setText("Edit");
            checkBoxDone.setChecked(isDone);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (id != null) {
                    intent.putExtra("id", id);
                }
                intent.putExtra("title", edtTitle.getText().toString());
                intent.putExtra("desc", edtDesc.getText().toString());
                intent.putExtra("done", checkBoxDone.isChecked());
                setResult(Activity.RESULT_OK, intent);
                AddNewNote.super.onBackPressed();
            }
        });
    }
}