package com.example.add_contactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<caontactmodel> arrayList=new ArrayList<caontactmodel>();
    Mainadapter adapter;

    AutoCompleteTextView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyler_view);
        editText=findViewById(R.id.searchbarId);

        //checkPermission();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText() != null) {
                    checkPermission();
                    adapter.filter(charSequence);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS},100);
        }else {
            getContactlist();
        }
    }

    private void getContactlist() {

        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        Cursor cursor= getContentResolver().query(
                uri,null,null,null,sort
        );

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){

                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                ));

                @SuppressLint("Range") String name= cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));

                Uri uriPhone= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                String selection= ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        +"=?";

                Cursor phoneCursor = getContentResolver().query(
                        uriPhone,null,selection
                        ,new String[]{id},null
                );

                if (phoneCursor.moveToNext()){
                    @SuppressLint("Range") String number =phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));

                    caontactmodel model=new caontactmodel();
                    model.setName(name);
                    model.setNumber(number);

                    //add model in array  list

                    arrayList.add(model);
                    phoneCursor.close();
                }

            }

            cursor.close();
        }
        //set layoutManager

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter =new Mainadapter(this,arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getContactlist();
        }else {
            Toast.makeText(MainActivity.this, "permission Denied", Toast.LENGTH_SHORT).show();

            checkPermission();
        }
    }
}