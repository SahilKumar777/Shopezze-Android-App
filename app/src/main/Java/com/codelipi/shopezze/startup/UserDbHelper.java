package com.codelipi.ecomm.startup;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Manish on 8/21/2017.
 */

public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="USERINFO.DB";
    private static final int DATABASE_VERSION=1;
private static final String CREATE_QUERY="Create table "+ Contact.NewUserInfo.TABLE_NAME+"("+ Contact.NewUserInfo.User_NAME+" TEXT," +
        ""+Contact.NewUserInfo.User_MOB+" TEXT,"+Contact.NewUserInfo.User_EMAIL+" TEXT,"+Contact.NewUserInfo.User_LASTNAME+" TEXT" +
        ","+Contact.NewUserInfo.User_Pass+" TEXT);";

    private static final String CREATE_QUERY1="Create table "+ Contact.Address.TABLE_NAME+"("+ Contact.Address.User_Address+" TEXT," +
            ""+Contact.Address.User_EMAIL+" TEXT,"+Contact.Address.User_Street+" TEXT,"+Contact.Address.User_City+" TEXT" +
            ","+Contact.Address.User_State+" TEXT,"+Contact.Address.User_Pincode+" TEXT);";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("Database operations","DatabaseCreated / Opened....");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_QUERY);
        Log.e("Database Operations","Table Created");
        db.execSQL(CREATE_QUERY1);
        Log.e("Database Operations1","Table Created");
    }
    public  void addInformations(String name, String mob, String email, String pass, String lastname, SQLiteDatabase db)
    {
    ContentValues contentValues=new ContentValues();
    contentValues.put(Contact.NewUserInfo.User_NAME,name);
    contentValues.put(Contact.NewUserInfo.User_MOB,mob);
    contentValues.put(Contact.NewUserInfo.User_EMAIL,email);
        contentValues.put(Contact.NewUserInfo.User_LASTNAME,lastname);
        contentValues.put(Contact.NewUserInfo.User_Pass,pass);
    db.insert(Contact.NewUserInfo.TABLE_NAME,null,contentValues);
    Log.e("Database operations","ONE rOW INSERTEd....");

    }

    public  void addAddress(String email, String address, String street, String city, String state, String pin, SQLiteDatabase db)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(Contact.Address.User_Address,address);
        contentValues.put(Contact.Address.User_City,city);
        contentValues.put(Contact.Address.User_Pincode,pin);
        contentValues.put(Contact.Address.User_State,state);
        contentValues.put(Contact.Address.User_Street,street);
        contentValues.put(Contact.Address.User_EMAIL,email);
        db.insert(Contact.Address.TABLE_NAME,null,contentValues);
        Log.e("Database operations","ONE rOW INSERTEd....");

    }

    public Cursor checkInformations(String user_name, String password, SQLiteDatabase sqLiteDatabase)
    {
        String[] projections={Contact.NewUserInfo.User_EMAIL, Contact.NewUserInfo.User_Pass,Contact.NewUserInfo.User_NAME};
        String selection=Contact.NewUserInfo.User_EMAIL+" LIKE ? AND "+Contact.NewUserInfo.User_Pass+" LIKE ?";
        //String selection=Contact.NewUserInfo.User_NAME+" = ? AND "+Contact.NewUserInfo.User_PASS+" = ?";
        String[] selection_args={user_name,password};
        Cursor cursor=sqLiteDatabase.query(Contact.NewUserInfo.TABLE_NAME,projections,selection,selection_args,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getInformations(SQLiteDatabase db)
    {
        Cursor cursor;
        String[] projections={Contact.NewUserInfo.User_NAME, Contact.NewUserInfo.User_MOB, Contact.NewUserInfo.User_EMAIL,Contact.NewUserInfo.User_LASTNAME,Contact.NewUserInfo.User_Pass};
        cursor=db.query(Contact.NewUserInfo.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;

    }
    public Cursor getContact(String user_name, SQLiteDatabase sqLiteDatabase)
    {
        String[] projections={Contact.NewUserInfo.User_NAME, Contact.NewUserInfo.User_MOB, Contact.NewUserInfo.User_EMAIL,Contact.NewUserInfo.User_LASTNAME,Contact.NewUserInfo.User_Pass};
        String selection=Contact.NewUserInfo.User_EMAIL+" LIKE ?";
        String[] selection_args={user_name};
        Cursor cursor=sqLiteDatabase.query(Contact.NewUserInfo.TABLE_NAME,projections,selection,selection_args,null,null,null);
        return cursor;
    }
    public Cursor getAddress(String user_name, SQLiteDatabase sqLiteDatabase)
    {
        String[] projections={Contact.Address.User_Pincode, Contact.Address.User_State, Contact.Address.User_City,Contact.Address.User_Street,Contact.Address.User_EMAIL,Contact.Address.User_Address};
        String selection=Contact.Address.User_EMAIL+" LIKE ?";
        String[] selection_args={user_name};
        Cursor cursor=sqLiteDatabase.query(Contact.Address.TABLE_NAME,projections,selection,selection_args,null,null,null);
        return cursor;
    }
    public void deleteInformation(String username, SQLiteDatabase sqLiteDatabase)
    {
        String selection=Contact.NewUserInfo.User_NAME+" LIKE ?";
        String[] selection_args={username};
        sqLiteDatabase.delete(Contact.NewUserInfo.TABLE_NAME,selection,selection_args);

    }
    public Cursor getContact1(String user_name, SQLiteDatabase db)
    {
        String[] projections={Contact.NewUserInfo.User_NAME,Contact.NewUserInfo.User_MOB, Contact.NewUserInfo.User_EMAIL};
        String selection=Contact.NewUserInfo.User_NAME+" LIKE ?";
        String[] selection_args={user_name};
        Cursor cursor=db.query(Contact.NewUserInfo.TABLE_NAME,projections,selection,selection_args,null,null,null);
        return cursor;
    }
    public int updateInformation(String email, String pass, String last, String mob, String fname, SQLiteDatabase sqLiteDatabase)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(Contact.NewUserInfo.User_NAME,fname);
        contentValues.put(Contact.NewUserInfo.User_MOB,mob);
        contentValues.put(Contact.NewUserInfo.User_Pass,pass);
        contentValues.put(Contact.NewUserInfo.User_LASTNAME,last);
        String selection =Contact.NewUserInfo.User_EMAIL+" LIKE ?";
        String[] selection_args={email};
       int count = sqLiteDatabase.update(Contact.NewUserInfo.TABLE_NAME,contentValues,selection,selection_args);
return count;
    }
}
