package com.uekapps.passwordgenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseClass extends SQLiteOpenHelper {
    Context context;
    private static final String DatabaseName = "MyPassword";
    private static final int DatabaseVersion = 1;

    private static final String TableName = "mypwd";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnPassword = "password";


    public DatabaseClass(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TableName +
                " (" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + " TEXT, " +
                ColumnPassword + " TEXT);";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(sqLiteDatabase);
    }

    void addPassword(String title, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnPassword, password);

        long resultValue = db.insert(TableName, null, cv);

        if (resultValue == -1) {
            Toast.makeText(context, "Data not added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void updatePassword(String title, String password, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnPassword, password);

        long resultValue = db.update(TableName, cv, "id=?", new String[]{id});

        if (resultValue == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteSingleItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long resultValue = db.delete(TableName, "id=?", new String[]{id});

        if (resultValue == -1) {
            Toast.makeText(context, "Item not deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteAllPasswords() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TableName;
        db.execSQL(query);
    }
}
