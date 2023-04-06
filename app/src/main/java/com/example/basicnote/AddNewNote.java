package com.example.basicnote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewNote extends AppCompatActivity {
    private EditText edtTitle, edtDesc;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDescription);
        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", edtTitle.getText().toString());
                intent.putExtra("desc", edtDesc.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                AddNewNote.super.onBackPressed();
            }
        });
    }
}