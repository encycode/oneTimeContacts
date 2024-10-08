package com.encycode.onetimecontact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.encycode.onetimecontact.adapter.ContactAdapter;
import com.encycode.onetimecontact.entity.Contact;
import com.encycode.onetimecontact.viewModel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    boolean floatSwitch = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FloatingActionButton mainBtn = findViewById(R.id.mainBtn);
        final FloatingActionButton contactBtn = findViewById(R.id.addContactBtn);
        final FloatingActionButton dialpadBtn = findViewById(R.id.dialpadBtn);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);


        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatSwitch = !floatSwitch;
                //Toast.makeText(MainActivity.this, ""+floatSwitch, Toast.LENGTH_SHORT).show();
                if (floatSwitch) {
                    mainBtn.startAnimation(rotate_forward);
                    contactBtn.startAnimation(fab_open);
                    dialpadBtn.startAnimation(fab_open);
                    contactBtn.setClickable(true);
                    dialpadBtn.setClickable(true);
                    floatSwitch = true;

                } else {
                    mainBtn.startAnimation(rotate_backward);
                    contactBtn.startAnimation(fab_close);
                    dialpadBtn.startAnimation(fab_close);
                    contactBtn.setClickable(false);
                    dialpadBtn.setClickable(false);
                    floatSwitch= false;
                }
            }
        });


        dialpadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DialerActivity.class);
                startActivity(i);
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddContact.class);
                startActivity(i);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.contactsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final ContactAdapter adapter = new ContactAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);


        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        });

        EditText search = findViewById(R.id.search_bar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(MainActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(s.toString());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {

                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.delete_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView no = dialog.findViewById(R.id.no);
                    TextView yes = dialog.findViewById(R.id.yes);

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Contact not Deleted", Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            dialog.cancel();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            contactViewModel.delete(adapter.getContactAt(viewHolder.getAdapterPosition()));
                            Toast.makeText(MainActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    String url = "https://api.whatsapp.com/send?phone=91" + adapter.getContactAt(viewHolder.getAdapterPosition()).getMobileNumber();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.delete_bg)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.lightgreen))
                        .addSwipeRightActionIcon(R.drawable.whatsapp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                Intent i = new Intent(MainActivity.this, EditContact.class);
                i.putExtra("contactObj", contact);
                startActivity(i);
            }
        });
    }

    public void deleteContact(Contact contact) {
        contactViewModel.delete(contact);
    }


}