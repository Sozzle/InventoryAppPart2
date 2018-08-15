package com.example.android.inventoryapppart2;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.inventoryapppart2.data.BookContract;
import com.example.android.inventoryapppart2.data.BookDbHelper;
import com.example.android.inventoryapppart2.data.BookContract.BookEntry;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    BookDbHelper mDbHelper;

    /** EditText field to enter the book's name */
    private EditText ProductNameEditText;

    private EditText PriceEditText;

    private EditText QuantityEditText;

    private EditText SupplierNameEditText;

    private EditText SupplierPhoneNumberEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }




    //Examine the intent that was used to launch this activity,
    //in order to figure out if we are creating a new pet or editing an existing one
    Intent intent = getIntent();
    Uri currentBookUri = intent.getData();






    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

