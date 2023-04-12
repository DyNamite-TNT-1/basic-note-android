package com.example.basicnote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Objects;

public class AddNewNote extends AppCompatActivity {
    private EditText edtTitle, edtDesc;
    private Button btnAdd;
    private CheckBox checkBoxDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back) ;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDescription);
        btnAdd = findViewById(R.id.btnAdd);
        checkBoxDone = findViewById(R.id.checkboxDone2);

        int position = getIntent().getIntExtra("position", -1);
        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        boolean isDone = getIntent().getBooleanExtra("done", false);
        if (position != -1) {
            edtTitle.setText(title);
            edtDesc.setText(desc);
            btnAdd.setText("Edit");
            checkBoxDone.setChecked(isDone);
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate
                if (edtTitle.getText().toString().trim().length() == 0) {
                    edtTitle.setError("Title is required!");
                    return;
                }

                Intent intent = new Intent();
                if (position != -1) {
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
                }

                String title = edtTitle.getText().toString().replaceAll("\\s+", " "); //remove duplicate whitespaces
                String desc = edtDesc.getText().toString().replaceAll("\\s+", " ");
                intent.putExtra("title", title);
                intent.putExtra("desc", desc);
                intent.putExtra("done", checkBoxDone.isChecked());
                setResult(Activity.RESULT_OK, intent);
                AddNewNote.super.onBackPressed();
            }
        });
    }
}