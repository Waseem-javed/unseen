package com.example.unseenmessage.whtcon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.unseenmessage.R;
import com.example.unseenmessage.models.ParentTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhatsappContacts extends AppCompatActivity {

    private ArrayList<Map<String, String>> contacts;
    private ListView contactsListView;
    private ArrayList<String> photoList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_contacts);

        getPermissionToReadUserContacts();


        contactsListView = (ListView) findViewById(R.id.listWhatsAppContacts);


        String[] from = {"ContactName", "ContactNumber"};
        int[] to = {R.id.txtName, R.id.txtNumber};

        contacts = fetchWhatsAppContacts();

        SimpleAdapter adapter = new SimpleAdapter(this, contacts, R.layout.list_whatsapp, from, to);

//        arrayAdapter = new ArrayAdapter(this, R.layout.list_whatsapp, photoList);
//        contactsListView.setAdapter(arrayAdapter);


        contactsListView.setAdapter(adapter);

        Uri u = getPhotoUri(Long.parseLong("+923149613277"));
        ImageView pi = findViewById(R.id.pi);
        Log.d("llllllllll", "onCreate: " + u);

    }

    public Uri getPhotoUri(long contactId) {


        androidx.lifecycle.Observer<List<ParentTable>> parentOberve = new Observer<List<ParentTable>>() {

            @Override
            public void onChanged(@Nullable List<ParentTable> parentTables) {

            }
        };

        Uri photoUri = null;

        ContentResolver contentResolver = this.getContentResolver();
        Cursor photoCur = contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI, null,
                ContactsContract.RawContacts.CONTACT_ID + "=" + contactId + " AND " +
                        ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"}, ContactsContract.RawContacts.CONTACT_ID);

        if (photoCur != null && photoCur.moveToFirst()) {
            Uri photoId = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactId);
            photoUri = Uri.withAppendedPath(photoId, ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
        }

        return photoUri;
    }

    private HashMap<String, String> pushData(String ContactName, String ContactNumber) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("ContactName", ContactName);
        item.put("ContactNumber", ContactNumber);
        return item;
    }

    /**
     * @return ArrayList<Map < String, String>>()
     */
    @SuppressLint("Range")
    private ArrayList<Map<String, String>> fetchWhatsAppContacts() {


        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        final String[] projection = {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.MIMETYPE,
                "account_type",
                ContactsContract.Data.DATA1,
                ContactsContract.Data.PHOTO_URI,
        };
        final String selection = ContactsContract.Data.MIMETYPE + " =? and account_type=?";
        final String[] selectionArgs = {
                "vnd.android.cursor.item/vnd.com.whatsapp.profile",
                "com.whatsapp"
        };
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        while (c.moveToNext()) {
            @SuppressLint("Range") String id = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
            @SuppressLint("Range") String numberW = c.getString(c.getColumnIndex(ContactsContract.Data.DATA1));
            String[] parts = numberW.split("@");
            String numberPhone = parts[0];
            String number = "Tel : + " + numberPhone.substring(0, 2) + " " + numberPhone.substring(2, numberPhone.length());
            @SuppressLint("Range") String image_uri = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            photoList.add(image_uri);

            String name = "";
            Cursor mCursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                    ContactsContract.Contacts._ID + " =?",
                    new String[]{id},
                    null);
            while (mCursor.moveToNext()) {
                name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            mCursor.close();
            list.add(pushData(name, number));

        }
        return list;
    }


    // Identifier for the permission request
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    /**
     * Called when the user is performing an action which requires the app to read the user's contacts
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {

            }
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults Callback with the request from calling requestPermissions(...)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}