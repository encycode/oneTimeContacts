package com.encycode.onetimecontact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(i, 1);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
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
                    new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this Contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    contactViewModel.delete(adapter.getContactAt(viewHolder.getAdapterPosition()));
                                    Toast.makeText(MainActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
//                                urlAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Contact not Deleted", Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    }).setIcon(R.drawable.delete).show();
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
                i.putExtra("id", contact.getId());
                i.putExtra("fName", contact.getFirstName());
                i.putExtra("lName", contact.getLastName());
                i.putExtra("mobile", contact.getMobileNumber());
                i.putExtra("email", contact.getEmailId());
                i.putExtra("company", contact.getCompanyName());
                i.putExtra("job", contact.getJobTitle());
                startActivityForResult(i, 2);
            }
        });
    }

    public void deleteContact(Contact contact) {
        contactViewModel.delete(contact);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        String fName = data.getStringExtra("fName");
        String lName = data.getStringExtra("lName");
        String mobile = data.getStringExtra("mobile");
        String email = data.getStringExtra("email");
        String company = data.getStringExtra("company");
        String jobTitle = data.getStringExtra("jobTitle");
        Contact contact = new Contact(fName, lName, company, jobTitle, email, mobile);

        if (resultCode == RESULT_OK && requestCode == 1) {
            contactViewModel.insert(contact);
            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == 2){

            int id = data.getIntExtra("id",-1);
            if (id == -1) {
                Toast.makeText(this, "Contact could not be updated", Toast.LENGTH_SHORT).show();
                return;
            } else {
                contact.setId(id);
                contactViewModel.update(contact);
                Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Contact saving error", Toast.LENGTH_SHORT).show();
        }
    }
}