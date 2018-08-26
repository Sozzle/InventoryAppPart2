package com.example.android.inventoryapppart2;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapppart2.data.BookDbHelper;
import com.example.android.inventoryapppart2.data.BookContract.BookEntry;

public class AddBookActivity extends AppCompatActivity {

    BookCursorAdapter mCursorAdapter;

    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        // Find the ListView which will be populated with the book data
        ListView bookListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        //Set up an Adapter to create a list item for each row of book data in the Cursor
        //There is  no book data yet (until the loader finishes) so pass in null for the Cursor
        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        //Set up item click listener
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Create new intent to go to (@link EditActivity)
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);

                //Form the Content URI that represents the specific book that was clicked on,
                //by appending the "id" (passed as input to this method) onto the
                // (@link BookEntry#CONTENT_URI).
                //For example, the URI would be "content://com.example.android.books/books/2"
                //if the book with ID 2 was clicked on.
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                //Set the URI on the data field of the intent
                intent.setData(currentBookUri);

                //Launch the (@link MainActivity)
                startActivity(intent);


            }
        });

        private void insertBook(); {
            // Create a ContentValues object where column names are the keys,
            // and book attributes from the edit are the values.
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_PRODUCT_NAME, "Moonstar");
            values.put(BookEntry.COLUMN_PRICE, 12);
            values.put(BookEntry.COLUMN_QUANTITY, 8);
            values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Waterstones");
            values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "01389763344");
            // Use the {@link BookEntry#CONTENT_URI} to indicate that we want to insert
            // into the books database table.
            // Receive the new content URI that will allow us to access Moonstar's data in the future.
            getContentResolver().insert(BookEntry.CONTENT_URI, values);

        }

        private void saveBook(); {
            // Create a ContentValues object where column names are the keys,
            // and book attributes from the edit are the values.
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_PRODUCT_NAME, "Fairies");
            values.put(BookEntry.COLUMN_PRICE, 14);
            values.put(BookEntry.COLUMN_QUANTITY, 7);
            values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Waterstones");
            values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "01389763344");
            // Use the {@link BookEntry#CONTENT_URI} to indicate that we want to insert
            // into the books database table.
            // Receive the new content URI that will allow us to access Fairies data in the future.
            getContentResolver().insert(BookEntry.CONTENT_URI, values);

        }
    }
}
