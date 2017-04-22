package com.merltech.money;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moneydb";

    // Table Layout for transactions
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_CATEGORIES = "categories";

    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TIMESTAMP = "timestamp";

    private static final String[] COLUMNS = {KEY_ID, KEY_CATEGORY, KEY_AMOUNT, KEY_DESCRIPTION, KEY_TIMESTAMP};

    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category TEXT, " +
                "amount INTEGER, " +
                "description TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " ( " +
                "category TEXT PRIMARY KEY " +
                ");";

        db.execSQL(CREATE_TRANSACTION_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS categories");

        this.onCreate(db);
    }

    public void addTransaction(Transaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORY, transaction.category);
        values.put(KEY_AMOUNT, transaction.amount);
        values.put(KEY_DESCRIPTION, transaction.description);

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactions()
    {
        List<Transaction> transactions = new LinkedList<Transaction>();

        String query = "SELECT * FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Transaction transaction = null;
        if(cursor.moveToFirst())
        {
            do
            {
                transaction = new Transaction();
                transaction.id = cursor.getInt(0);
                transaction.category = cursor.getString(1);
                transaction.amount = cursor.getInt(2);
                transaction.description = cursor.getString(3);
                transactions.add(transaction);
            } while(cursor.moveToNext());
        }

        db.close();

        return transactions;
    }

    public void updateTransaction(Transaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORY, transaction.category);
        values.put(KEY_AMOUNT, transaction.amount);
        values.put(KEY_DESCRIPTION, transaction.description);

        db.update(TABLE_TRANSACTIONS, values, KEY_ID + " = ?", new String[] { String.valueOf(transaction.id)});

        db.close();
    }

    public void deleteTransaction(Transaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRANSACTIONS, KEY_ID + " = ?", new String[] { String.valueOf(transaction.id)});

        db.close();
    }

    public void deleteAllTransactions()
    {
        String query = "DELETE FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);
        db.close();
    }

    public int getTotal()
    {
        String query = "SELECT sum(amount) FROM " + TABLE_TRANSACTIONS + ";";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        int total = 0;

        if(cursor.moveToFirst())
        {
            total = cursor.getInt(0);
        }

        db.close();

        return total;

    }

    public void addCategory(String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORY, category);

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public List<String> getCategories()
    {
        List<String> categories = new LinkedList<String>();

        String query = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                categories.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }

        db.close();

        return categories;
    }

    public List<String> getCategoriesOrderedByAmount()
    {
        List<String> categories = new LinkedList<String>();

        String query = "SELECT c.category, sum(t.amount) FROM categories c JOIN transactions t ON (c.category = t.category) GROUP BY c.category ORDER BY 2 DESC;";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                categories.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }

        db.close();

        return categories;
    }

    public List<String> getCategoriesOrderedByCount()
    {
        List<String> categories = new LinkedList<String>();

        String query = "SELECT c.category, count(t.category) FROM categories c JOIN transactions t ON (c.category = t.category) GROUP BY c.category ORDER BY 2 DESC;";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                categories.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }

        db.close();

        return categories;
    }

    void deleteAllCategories()
    {
        String query = "DELETE FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);

        db.close();
    }

    public int getCategoryTotal(String category)
    {
        String query = "SELECT sum(amount) FROM " + TABLE_TRANSACTIONS + " WHERE category=\"" + category + "\";";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        int total = 0;

        if(cursor.moveToFirst())
        {
            total = cursor.getInt(0);
        }

        db.close();

        return total;
    }
}
