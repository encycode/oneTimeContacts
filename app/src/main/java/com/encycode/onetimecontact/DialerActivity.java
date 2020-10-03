package com.encycode.onetimecontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DialerActivity extends AppCompatActivity {


    Button one, two, three, four, five, six, seven, eight, nine, zero, star, hash;
    ImageButton backSpace, call, message, wtsp;
    TextView numberField, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);


        one = findViewById(R.id.btn1);
        two = findViewById(R.id.btn2);
        three = findViewById(R.id.btn3);
        four = findViewById(R.id.btn4);
        five = findViewById(R.id.btn5);
        six = findViewById(R.id.btn6);
        seven = findViewById(R.id.btn7);
        eight = findViewById(R.id.btn8);
        nine = findViewById(R.id.btn9);
        zero = findViewById(R.id.btn0);
        star = findViewById(R.id.btnStar);
        hash = findViewById(R.id.btnHash);


        wtsp = findViewById(R.id.dialWtspBtn);
        call = findViewById(R.id.dialCallBtn);
        message = findViewById(R.id.dialMsgBtn);

        backSpace = findViewById(R.id.backspaceBtn);
        numberField = findViewById(R.id.numberfield);

        add = findViewById(R.id.addToContactTV);

        addVisible();


        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s1", numberField.getText().toString()));
                addVisible();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s2", numberField.getText().toString()));
                addVisible();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s3", numberField.getText().toString()));
                addVisible();
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s4", numberField.getText().toString()));
                addVisible();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s5", numberField.getText().toString()));
                addVisible();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s6", numberField.getText().toString()));
                addVisible();
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s7", numberField.getText().toString()));
                addVisible();
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s8", numberField.getText().toString()));
                addVisible();
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s9", numberField.getText().toString()));
                addVisible();
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s0", numberField.getText().toString()));
                addVisible();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s*", numberField.getText().toString()));
                addVisible();
            }
        });

        hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(String.format("%s#", numberField.getText().toString()));
                addVisible();
            }
        });

        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberField.setText(removeLastChars(numberField.getText().toString(), numberField.getText().toString().isEmpty()));
                addVisible();
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numberField.getText().toString(), null));
                startActivity(intent);
            }
        });

        wtsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberField.getText().toString().length() == 10) {
                    String url = "https://api.whatsapp.com/send?phone=91" + numberField.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(DialerActivity.this, "Invalid Whatsapp Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + numberField.getText().toString()));
                startActivity(sendIntent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberField.getText().toString().isEmpty()) {
                    Toast.makeText(DialerActivity.this, "Enter Number to proceed", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(DialerActivity.this, AddContact.class);
                    i.putExtra("number", numberField.getText().toString());
                    startActivity(i);
                }
            }
        });


    }

    public static String removeLastChars(String str, boolean empty) {
        if (empty)
            return "";
        String data = str.substring(0, str.length() - 1);
        return data;
    }

    public void addVisible()
    {
        if(numberField.getText().toString().isEmpty())
            add.setVisibility(View.GONE);
        else
            add.setVisibility(View.VISIBLE);
    }
}