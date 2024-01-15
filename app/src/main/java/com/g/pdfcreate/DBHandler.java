package com.g.pdfcreate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_details.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "user_details";


    public static final String NAME = "NAME";
    public static final String NUMBER = "NUMBER";
    public static final String CREDIT = "CREDIT";
    public static final String DEBIT = "DEBIT";
    public static final String DATE_TIME = "DATE_TIME";
    private Context context;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE " + TABLE_NAME +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+"TEXT,"+NUMBER+"TEXT,"+CREDIT+"TEXT,"+DEBIT+"TEXT)");
        String query = "CREATE TABLE " + TABLE_NAME + " ("

                + NAME + " TEXT,"
                + NUMBER + " TEXT,"
                + CREDIT + " TEXT,"
                + DEBIT + " TEXT,"
                + DATE_TIME + " TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addUser(String Name, String Number, String Credit, String Debit, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, Name);
        values.put(NUMBER, Number);
        values.put(CREDIT, Credit);
        values.put(DEBIT, Debit);
        values.put(DATE_TIME, dateTime);
        long res = db.insert(TABLE_NAME, null, values);
        if (res == -1)
            return false;
        else
            return true;

    }

    public boolean updateData1(String name, String number, String credit, String debit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NUMBER, number);
        values.put(CREDIT, credit);
        values.put(DEBIT, debit);

        // Update the data
        long result =  db.update(TABLE_NAME, values, "NAME=?", new String[]{name});

        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public boolean delData(String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Use placeholders in the WHERE clause
        String whereClause = "NAME = ?";

        // Provide the values through selectionArgs
        String[] whereArgs = new String[]{Name};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        return true;
    }

    public Cursor readData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        }
        return cursor;
    }


}
