package com.encycode.onetimecontact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.encycode.onetimecontact.entity.Contact;
import com.encycode.onetimecontact.viewModel.ContactViewModel;

import java.util.Objects;

public class EditContact extends AppCompatActivity {

    TextView fName, lName, mobile, email, company, job;
    Button save, discard;
    TextView call, msg, mail, wtsp;
    Contact contact;
    int id;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.close);
        setTitle("Edit Contact");


        call = findViewById(R.id.callBtn);
        msg = findViewById(R.id.msgBtn);
        mail = findViewById(R.id.mailBtn);
        wtsp = findViewById(R.id.wtspBtn);

        fName = findViewById(R.id.firstNameET);
        lName = findViewById(R.id.lastNameET);
        mobile = findViewById(R.id.mobileNumberET);
        email = findViewById(R.id.emailIdET);
        company = findViewById(R.id.companyET);
        job = findViewById(R.id.jobET);


        contact = (Contact) getIntent().getSerializableExtra("contactObj");

        final String phone = getIntent().getStringExtra("mobile");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + phone));
                startActivity(sendIntent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = getIntent().getStringExtra("email");
                if (emailStr.isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(EditContact.this, "Enter Email to use this function", Toast.LENGTH_SHORT).show();
                } else {
                    if (emailStr.isEmpty()) {
                        emailStr = email.getText().toString();
                    }
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("mailto:" + emailStr));
                    startActivity(sendIntent);
                }
            }
        });

        wtsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=91" + phone;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        save = findViewById(R.id.saveBtn);
        discard = findViewById(R.id.discardBtn);

        fName.setText(contact.getFirstName());
        lName.setText(contact.getLastName());
        mobile.setText(contact.getMobileNumber());
        email.setText(contact.getEmailId());
        company.setText(contact.getCompanyName());
        job.setText(contact.getJobTitle());


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void saveContact() {
        Contact contact = (Contact) getIntent().getSerializableExtra("contactObj");
        contact.setFirstName(fName.getText().toString());
        contact.setLastName(lName.getText().toString());
        contact.setCompanyName(company.getText().toString());
        contact.setEmailId(email.getText().toString());
        contact.setMobileNumber(mobile.getText().toString());
        contact.setJobTitle(job.getText().toString());


        if (fName.getText().toString().trim().isEmpty() || mobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please inserts Mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new ContactViewModel(getApplication()).update(contact);
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