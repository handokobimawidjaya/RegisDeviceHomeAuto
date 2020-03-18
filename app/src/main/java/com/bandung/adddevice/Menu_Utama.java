package com.bandung.adddevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_Utama extends AppCompatActivity {


    Button Regisdevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__utama);
        Regisdevice=(Button)findViewById(R.id.Klik);
        Regisdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Menu_Utama.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
