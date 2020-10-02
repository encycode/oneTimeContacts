package com.encycode.onetimecontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class EditContact extends AppCompatActivity {

    TextView fName,lName,mobile,email,company,job;
    Button save,discard;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.close);
        setTitle("Edit Contact");

        fName = findViewById(R.id.firstNameET);
        lName = findViewById(R.id.lastNameET);
        mobile = findViewById(R.id.mobileNumberET);
        email = findViewById(R.id.emailIdET);
        company = findViewById(R.id.companyET);
        job = findViewById(R.id.jobET);

        save = findViewById(R.id.saveBtn);
        discard = findViewById(R.id.discardBtn);

        fName.setText(getIntent().getStringExtra("fName"));
        lName.setText(getIntent().getStringExtra("lName"));
        mobile.setText(getIntent().getStringExtra("mobile"));
        email.setText(getIntent().getStringExtra("email"));
        company.setText(getIntent().getStringExtra("company"));
        job.setText(getIntent().getStringExtra("job"));

        id = (int)getIntent().getIntExtra("id",-1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    private void saveContact()
    {
        String fNameStr = fName.getText().toString();
        String lNameStr = lName.getText().toString();
        String mobileStr = mobile.getText().toString();
        String emailStr = email.getText().toString();
        String companyStr = company.getText().toString();
        String jobStr = job.getText().toString();

        if(fNameStr.trim().isEmpty() || mobileStr.trim().isEmpty())
        {
            Toast.makeText(this, "Please inserts Mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra("id",id);
        data.putExtra("fName",fNameStr);
        data.putExtra("lName",lNameStr);
        data.putExtra("mobile",mobileStr);
        data.putExtra("email",emailStr);
        data.putExtra("company",companyStr);
        data.putExtra("jobTitle",jobStr);

        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveContact:
                saveContact();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}