package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sushinski.pogodka.DL.DBReaderContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CityReaderDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBReaderContract.CityRecord.TABLE_NAME + " (" +
                    DBReaderContract.CityRecord._ID + " INTEGER PRIMARY KEY," +
                    DBReaderContract.CityRecord.COLUMN_CITY_TITLE + TEXT_TYPE + COMMA_SEP +
                    DBReaderContract.CityRecord.COLUMN_CITY_CODE + TEXT_TYPE + COMMA_SEP +
                    DBReaderContract.CityRecord.COLUMN_CITY_SELECTED + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBReaderContract.CityRecord.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.sushinski.pogodka/databases/";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pogodka.db";

    public CityReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }
    public void onCreate(SQLiteDatabase db) {
        // don`t need this really, using pre-populated database
        try {
            deleteDatabase();
            createDataBase();
        } catch (IOException e) {
            throw new RuntimeException("Error copying database");
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // using pre-popuated file means simply delete used db file and start over
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // leaving for future releases
        throw new RuntimeException("Error feature not implemented");
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    private void createDataBase() throws IOException{
        if(!checkDataBase()){
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) { // todo: assign real exception
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Deletes used database file
     */
    private boolean deleteDatabase() throws IOException{
        try {
            String dir = myContext.getFilesDir().getAbsolutePath();
            File f0 = new File(dir, "myFile");
            return f0.delete();
        }catch(Exception e){
            throw new IOException("Can`t delete database file");
        }
    }

    /**
     * Check if the database already exist to avoid
     * re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
}