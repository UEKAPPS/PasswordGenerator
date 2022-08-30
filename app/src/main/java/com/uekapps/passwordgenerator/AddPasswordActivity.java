package com.uekapps.passwordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddPasswordActivity extends AppCompatActivity {

    EditText title, password;
    Button addBtn, randomBtn;
    SeekBar seekBar;
    TextView length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        //init
        title = findViewById(R.id.title);
        password = findViewById(R.id.password);
        addBtn = findViewById(R.id.addPassword);
        randomBtn = findViewById(R.id.randomBtn);
        seekBar = findViewById(R.id.seekBar);
        length = findViewById(R.id.length);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                length.setText("Length: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = PasswordGeneratorHelper.process(seekBar.getProgress());
                password.setText(pass);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    DatabaseClass db = new DatabaseClass(AddPasswordActivity.this);
                    db.addPassword(title.getText().toString(), password.getText().toString());

                    Intent intent = new Intent(AddPasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddPasswordActivity.this, "Both field required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}