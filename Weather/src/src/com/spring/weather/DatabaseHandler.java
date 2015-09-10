package com.spring.weather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
	String DB_PATH;
    private final static String DB_NAME = "db.sqlite";
	public final static int DB_VERSION = 1;
    public static SQLiteDatabase db; 

    public final Context context;
    Resources res;
    public DatabaseHandler(Context context) {

    	super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        //DB_PATH = context.getString(R.string.db_pathh);
        DB_PATH = Environment.getDataDirectory()+"/data/com.spring.weather/";
    }	
    public void deleteDataBase(){
	File dbFile = new File(DB_PATH + DB_NAME);
	
	dbFile.delete();
}

public boolean checkDataBase(){

	File dbFile = new File(DB_PATH + DB_NAME);

	return dbFile.exists();
	
}


public void copyDataBase() throws IOException{
	
	InputStream myInput = this.context.getAssets().open(DB_NAME);

	String outFileName = DB_PATH + DB_NAME;

	OutputStream myOutput = new FileOutputStream(outFileName);
	
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer))>0){
		myOutput.write(buffer, 0, length);
	}

	myOutput.flush();
	myOutput.close();
	myInput.close();

}

    public void openDataBase() throws SQLException{
    	String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    @Override
	public void close() {
    	db.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	public void clear() {
		String selectQuery = "DELETE FROM data;";
        db.execSQL(selectQuery);
	    }
	/** detalji o proizvodu */
	public ArrayList<HashMap<String, Object>> takeData(int id) {
		ArrayList<HashMap<String, Object>> wordList;
		wordList = new ArrayList<HashMap<String, Object>>();
		String selectQuery = "SELECT * FROM data WHERE id = "+id;
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, Object> map = new HashMap<String, Object>();
	        	map.put("id", cursor.getString(0));
	        	map.put("dt", cursor.getString(1));
	        	map.put("day", cursor.getString(2));
	        	map.put("min", cursor.getString(3));
	        	map.put("max", cursor.getString(4));
	        	map.put("pressure", cursor.getString(5));
	        	map.put("humidity", cursor.getString(6));
	        	map.put("wid", cursor.getString(7));
	        	map.put("main", cursor.getString(8));
	        	map.put("description", cursor.getString(9));
	        	map.put("icon", cursor.getString(10));
	        	map.put("speed", cursor.getString(11));
	        	map.put("deg", cursor.getString(12));
	        	map.put("clouds", cursor.getString(13));
	        	map.put("rain", cursor.getString(14));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return wordList;
	}
 	String checkStringContent(String temp){
 		if(temp == null){
 			return "";
 		}
 		else return temp;
 	}
	/** Unos podataka o prognozi
	 * @param rain 
	 * @param clouds 
	 * @param deg 
	 * @param speed 
	 * @param icon 
	 * @param description 
	 * @param main 
	 * @param id 
	 * @param humidity 
	 * @param pressure 
	 * @param max 
	 * @param min 
	 * @param day 
	 * @param dt 
	 * @param rain 
	 * @param clouds 
	 * @param deg 
	 * @param speed 
	 * @param icon 
	 * @param description 
	 * @param main */
	public void insertData(String dt, String day, String min, String max, String pressure, String humidity, String id, String main, String description, String icon, String speed, String deg, String clouds, String rain) {
	
		ContentValues insert = new ContentValues();
		insert.put("dt", dt);
		insert.put("day", day);
		insert.put("min", min);
		insert.put("max", max);
		insert.put("pressure", pressure);
		insert.put("humidity", humidity);
		insert.put("wid", id);
		insert.put("main", main);
		insert.put("description", description);
		insert.put("icon", icon);
		insert.put("speed", speed);
		insert.put("deg", deg);
		insert.put("clouds", clouds);
		insert.put("rain", rain);
		db.insert("data", null, insert);

	    }
}