package com.example.android.inventoryapppart2;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapppart2.data.BookContract.BookEntry;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the book data loader */

    private static final int EXISTING_BOOK_LOADER = 0;



    BookCursorAdapter mCursorAdapter;
    /**
     * EditText field to enter the book's name
     */
    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierEditText;

    private EditText mPhoneEditText;

   private Uri mCurrentBookUri;


    /** Boolean flag that keeps track of whether the book has been edited (true) or not (false) */

    private boolean mBookHasChanged = false;

    /**

     * OnTouchListener that listens for any user touches on a View, implying that they are modifying

     * the view, and we change the mBookHasChanged boolean to true.

     */

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override

        public boolean onTouch(View view, MotionEvent motionEvent) {

            mBookHasChanged = true;

            return false;

        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

    //Examine the intent that was used to launch this activity,
    //in order to figure out if we are creating a new book or editing an existing one
    Intent intent = getIntent();
    Uri CurrentBookUri = intent.getData();

        // If the intent DOES NOT contain a book content URI, then we know that we are

// creating a new book.

        if (mCurrentBookUri == null) {

// This is a new book, so change the app bar to say "Add a Book"

            setTitle(getString(R.string.edit_activity_title_new_book));

// Invalidate the options menu, so the "Delete" menu option can be hidden.

// (It doesn't make sense to delete a book that hasn't been created yet.)

            invalidateOptionsMenu();

        } else {

// Otherwise this is an existing book, so change app bar to say "Edit Book"

            setTitle(getString(R.string.edit_activity_title_edit_book));

// Initialize a loader to read the pet data from the database

// and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from

        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText)(findViewById(R.id.edit_quantity));
        mSupplierEditText = (EditText)(findViewById(R.id.edit_supplier_name));
        mPhoneEditText = (EditText) (findViewById(R.id.supplier_phone_number));
    }


    // Get user input from editor and save new book into database.
    private void insertBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();


        // Create a ContentValues object where column names are the keys,
        // and book attributes from the edit are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookEntry.COLUMN_PRICE, priceString);
        values.put(BookEntry.COLUMN_QUANTITY, quantityString);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierString);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, phoneString);
        // Insert a new book into the provider, returning the content URI for the new book.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.edit_insert_book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.edit_insert_book_successful),
                    Toast.LENGTH_SHORT).show();
        }


        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertBook();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (MainActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define a projection that specifies the column from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER};

        //This loader will execute the Content Provider's query method on a background thread.
        return new CursorLoader(this,  //Parent activity context
                mCurrentBookUri, //Query the Content URI for the current book
                projection,  // Columns to include in the resulting Cursor
                null,       // No selection clause
                null,       // no selection arguments
                null);      // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Proceed with moving to the first row of the cursor and reading data from it
            // (This should be the only row in the cursor)
            // Find the columns of pet attributes that we're interested in
            int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);

// Update the views on the screen with the values from the database

            mNameEditText.setText(productName);
            mPriceEditText.setText(price);
            mQuantityEditText.setText(quantity);
            mSupplierEditText.setText(supplierName);
            mPhoneEditText.setText(supplierPhoneNumber);

            }
        }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mPhoneEditText.setText("");

    }
}
