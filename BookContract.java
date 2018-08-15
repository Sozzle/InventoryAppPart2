package com.example.android.inventoryapppart2.data;

import android.provider.BaseColumns;

public class BookContract {

    public BookContract() {
    }

    public static final class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "product";

        public final static String COLUMN_PRICE = "price";

        public final static String COLUMN_QUANTITY = "quantity";

        public final static String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";

        public final static String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

    }
}
