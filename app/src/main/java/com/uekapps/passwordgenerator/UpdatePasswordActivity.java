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

public class UpdatePasswordActivity extends AppCompatActivity {

    EditText title, password;
    Button updateBtn, randomBtn;
    String id;
    SeekBar seekBar;
    TextView length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        //init
        title = findViewById(R.id.title);
        password = findViewById(R.id.password);
        seekBar = findViewById(R.id.seekBar);
        length = findViewById(R.id.length);
        updateBtn = findViewById(R.id.updatePassword);
        randomBtn = findViewById(R.id.randomBtn);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        password.setText(intent.getStringExtra("password"));
        id = intent.getStringExtra("id");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                length.setText("Length: " + i);
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    DatabaseClass db = new DatabaseClass(UpdatePasswordActivity.this);
                    db.updatePassword(title.getText().toString(), password.getText().toString(), id);

                    Intent i = new Intent(UpdatePasswordActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(UpdatePasswordActivity.this, "Both field required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}