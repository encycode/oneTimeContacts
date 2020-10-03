package com.encycode.onetimecontact.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.encycode.onetimecontact.dao.ContactDao;
import com.encycode.onetimecontact.database.ContactDatabase;
import com.encycode.onetimecontact.entity.Contact;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;

    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactDatabase contactDatabase = ContactDatabase.getInstance(application);
        contactDao = contactDatabase.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    public void insert(Contact contact) {
        new InsertAsyncTask(contactDao).execute(contact);
    }

    public void update(Contact contact) {
        new UpdateAsyncTask(contactDao).execute(contact);
    }

    public void delete(Contact contact) {
        new DeleteAsyncTask(contactDao).execute(contact);
    }

    public void deleteAllContacts() {
        new DeleteAllContactsAsyncTask(contactDao).execute();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    private static class InsertAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao contactDao;

        private InsertAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.insert(contacts[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao contactDao;

        private UpdateAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.update(contacts[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao contactDao;

        private DeleteAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.delete(contacts[0]);
            return null;
        }
    }

    private static class DeleteAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ContactDao contactDao;

        private DeleteAllContactsAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.deleteAllContacts();
            return null;
        }
    }
}
