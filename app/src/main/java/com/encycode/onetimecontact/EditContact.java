package com.encycode.onetimecontact;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.encycode.onetimecontact.entity.Contact;
import com.encycode.onetimecontact.viewModel.ContactViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class EditContact extends AppCompatActivity {

    TextInputLayout tl_fname,tl_mobile;
    TextInputEditText fName, lName, mobile, email, company, job;

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

        tl_fname = findViewById(R.id.tl_fname);
        tl_mobile = findViewById(R.id.tl_mobile);


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

        fName.setText(contact.getFirstName());
        lName.setText(contact.getLastName());
        mobile.setText(contact.getMobileNumber());
        email.setText(contact.getEmailId());
        company.setText(contact.getCompanyName());
        job.setText(contact.getJobTitle());

    }

    @Override
    public void onBackPressed(){

        if (fName.getText().toString().equals(contact.getFirstName()) && lName.getText().toString().equals(contact.getLastName()) &&
                mobile.getText().toString().equals(contact.getMobileNumber())&& email.getText().toString().equals(contact.getEmailId())&&
                company.getText().toString().equals(contact.getCompanyName())&& job.getText().toString().equals(contact.getJobTitle())){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            showDialog();
        }
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(EditContact.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView discard = dialog.findViewById(R.id.discard);
        TextView save = dialog.findViewById(R.id.save);

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void saveContact() {
        Contact contact = (Contact) getIntent().getSerializableExtra("contactObj");
        contact.setFirstName(fName.getText().toString());
        contact.setLastName(lName.getText().toString());
        contact.setCompanyName(company.getText().toString());
        contact.setEmailId(email.getText().toString());
        contact.setMobileNumber(mobile.getText().toString());
        contact.setJobTitle(job.getText().toString());

        if (fName.getText().toString().equals("")){
            tl_fname.setError("First name is required");
            return;
        }
        else{
            tl_fname.setErrorEnabled(false);
        }
        if(mobile.getText().toString().equals("")) {
            tl_mobile.setError("Mobile number is required");
            return;
        }else{
            tl_mobile.setErrorEnabled(false);
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