package com.encycode.onetimecontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.encycode.onetimecontact.entity.Contact;
import com.encycode.onetimecontact.viewModel.ContactViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddContact extends AppCompatActivity {

//    private EditText fName, lName, mobile, email, company, jobTitle;
//    private MaterialButton save, discard;

    TextInputLayout tl_fname,tl_mobile;
    TextInputEditText fName, lName, mobile, email, company, jobTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.close);
        setTitle("Add Contact");

        fName = findViewById(R.id.tv_fname);
        lName = findViewById(R.id.tv_lname);
        mobile = findViewById(R.id.tv_mobile);
        email = findViewById(R.id.tv_email);
        company = findViewById(R.id.tv_company);
        jobTitle = findViewById(R.id.tv_jobtitle);

        tl_fname = findViewById(R.id.tl_fname);
        tl_mobile = findViewById(R.id.tl_mobile);

        if (getIntent().hasExtra("number")) {
            mobile.setText(getIntent().getStringExtra("number").toString());
        }
    }
    @Override
    public void onBackPressed(){

        if (fName.getText().toString().equals("") && lName.getText().toString().equals("") && mobile.getText().toString().equals("")&& email.getText().toString().equals("")&& company.getText().toString().equals("")&& jobTitle.getText().toString().equals("")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            showDialog();
        }
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(AddContact.this);
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

    private   void saveContact() {
        String fNameStr = fName.getText().toString();
        String lNameStr = lName.getText().toString();
        String mobileStr = mobile.getText().toString();
        String emailStr = email.getText().toString();
        String companyStr = company.getText().toString();
        String jobStr = jobTitle.getText().toString();

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

        new ContactViewModel(getApplication()).insert(new Contact(fNameStr, lNameStr, companyStr, jobStr, emailStr, mobileStr));
        Intent i = new Intent(AddContact.this, MainActivity.class);
        startActivity(i);
        AddContact.this.finish();
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