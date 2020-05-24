package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class AddActivity extends Activity {
    private Button btSave,btCancel;
    private EditText etName, etSurname, etMiddlenmae, etGroup;
    private Context context;
    private long MyMatchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName =(EditText)findViewById(R.id.name);
        etSurname =(EditText)findViewById(R.id.surname);
        etMiddlenmae =(EditText)findViewById(R.id.middlename);
        etGroup =(EditText)findViewById(R.id.group);
        btSave=(Button)findViewById(R.id.butSave);
        btCancel=(Button)findViewById(R.id.butCancel);

        if(getIntent().hasExtra("Matches")){
            Matches matches=(Matches)getIntent().getSerializableExtra("Matches");
            etName.setText(matches.getName());
            etSurname.setText(matches.getSurname());
            etMiddlenmae.setText(matches.getMiddlename());
            etGroup.setText(matches.getGroup());
            MyMatchID=matches.getId();
        }
        else
        {
            MyMatchID=-1;
        }
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matches matches=new Matches(MyMatchID, etName.getText().toString(), etSurname.getText().toString(), etMiddlenmae.getText().toString(), etGroup.getText().toString());
                Intent intent=getIntent();
                intent.putExtra("Matches",matches);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}