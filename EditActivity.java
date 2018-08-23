package com.example.android.inventoryapppart2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    private EditText mProductNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneNumberEditText;

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

        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText)(findViewById(R.id.edit_quantity));
        mSupplierNameEditText = (EditText)(findViewById(R.id.edit_supplier_name));
        mSupplierPhoneNumberEditText = (EditText) (findViewById(R.id.supplier_phone_number));

        // Setup OnTouchListeners on all the input fields, so we can determine if the user

// has touched or modified them. This will let us know if there are unsaved changes

// or not, if the user tries to leave the editor without saving.

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);

    }


    // Get user input from editor and save new book into database.
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierNameEditText.getText().toString().trim();
        String phoneString = mSupplierPhoneNumberEditText.getText().toString().trim();

    // Check if this is supposed to be a new pet

// and check if all the fields in the editor are blank

        if (mCurrentBookUri == null &&

                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&

                TextUtils.isEmpty(quantityString) ) {

// Since no fields were modified, we can return early without creating a new BOOK.

// No need to create ContentValues and no need to do any ContentProvider operations.

            return;

        }

        // Create a ContentValues object where column names are the keys,
        // and book attributes from the edit are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookEntry.COLUMN_PRICE, priceString);
        values.put(BookEntry.COLUMN_QUANTITY, quantityString);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierString);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, phoneString);
        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not

        if (mCurrentBookUri == null) {

    // This is a NEW pet, so insert a new book into the provider,

    // returning the content URI for the new book.

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

        } else {

// Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri

// and pass in the new ContentValues. Pass in null for the selection and selection args

// because mCurrentBookUri will already identify the correct row in the database that

// we want to modify.

            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);



// Show a toast message depending on whether or not the update was successful.

            if (rowsAffected == 0) {

// If no rows were affected, then there was an error with the update.

                Toast.makeText(this, getString(R.string.edit_update_book_failed),

                        Toast.LENGTH_SHORT).show();

            } else {

// Otherwise, the update was successful and we can display a toast.

                Toast.makeText(this, getString(R.string.edit_update_book_successful),

                        Toast.LENGTH_SHORT).show();

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_edit.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    /**

     * This method is called after invalidateOptionsMenu(), so that the

     * menu can be updated (some menu items can be hidden or made visible).

     */
    @Override

    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

// If this is a new pet, hide the "Delete" menu item.

        if (mCurrentBookUri == null) {

            MenuItem menuItem = menu.findItem(R.id.action_delete);

            menuItem.setVisible(false);

        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                saveBook();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity

                // which is the {@link MainActivity}.

                if (!mBookHasChanged) {

                    NavUtils.navigateUpFromSameTask(EditActivity.this);

                    return true;

                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }




        @Override
        public Loader<Cursor> onCreateLoader ( int i, Bundle bundle){
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
        public void onLoadFinished (Loader < Cursor > loader, Cursor cursor){
            // Bail early if the cursor is null or there is less than 1 row in the cursor

            if (cursor == null || cursor.getCount() < 1) {

                return;
            }

// Proceed with moving to the first row of the cursor and reading data from it
// (This should be the only row in the cursor)

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

                mProductNameEditText.setText(productName);
                mPriceEditText.setText(price);
                mQuantityEditText.setText(quantity);
                mSupplierNameEditText.setText(supplierName);
                mSupplierPhoneNumberEditText.setText(supplierPhoneNumber);

            }
        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            // If the loader is invalidated, clear out all the data from the input fields.
            mProductNameEditText.setText("");
            mPriceEditText.setText("");
            mQuantityEditText.setText("");
            mSupplierNameEditText.setText("");
            mSupplierPhoneNumberEditText.setText("");

        }
    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }});
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteBook() {

// Only perform the delete if this is an existing pet.

                if (mCurrentBookUri != null) {

// Call the ContentResolver to delete the pet at the given content URI.

// Pass in null for the selection and selection args because the mCurrentPetUri

// content URI already identifies the pet that we want.

                    int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);


// Show a toast message depending on whether or not the delete was successful.

                    if (rowsDeleted == 0) {

// If no rows were deleted, then there was an error with the delete.

                        Toast.makeText(this, getString(R.string.edit_delete_book_failed),

                                Toast.LENGTH_SHORT).show();

                    } else {

// Otherwise, the delete was successful and we can display a toast.

                        Toast.makeText(this, getString(R.string.edit_delete_book_successful),

                                Toast.LENGTH_SHORT).show();

                    }

                }


// Close the activity

                finish();

            }

        }

