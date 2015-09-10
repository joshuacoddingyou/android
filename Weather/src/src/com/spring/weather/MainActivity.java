package com.spring.weather;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
Context context;
String city,units, weatherurl, weathercurrenturl;

JSONParser jParser = new JSONParser();
DatabaseHandler db;
ProgressBar pb;
TextView srednja1, srednja2, srednja3, srednja4, dan1, dan2, dan3, grad, vjetar, tlak, vlaznost,stanje;
ImageView img_glavna, img_dan1, img_dan2, img_dan3;
DecimalFormat format;
SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		db = new DatabaseHandler(context);
		try {
			db.copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		format = new DecimalFormat();
		Typeface ubuntuFont = Typeface.createFromAsset(getAssets(), "Ubuntu-R.ttf");
		pb = (ProgressBar) findViewById(R.id.progressBar1);
        format.setDecimalSeparatorAlwaysShown(false);
		srednja1 = (TextView) findViewById(R.id.txt_stupnjevi);
		srednja1.setTypeface(ubuntuFont);
		srednja2 = (TextView) findViewById(R.id.txt_stupnjevi1);
		srednja2.setTypeface(ubuntuFont);
		srednja3 = (TextView) findViewById(R.id.txt_stupnjevi2);
		srednja3.setTypeface(ubuntuFont);
		srednja4 = (TextView) findViewById(R.id.txt_stupnjevi3);
		srednja4.setTypeface(ubuntuFont);
		dan1 = (TextView) findViewById(R.id.txt_dan1);
		dan1.setTypeface(ubuntuFont);
		dan2 = (TextView) findViewById(R.id.txt_dan2);
		dan2.setTypeface(ubuntuFont);
		dan3 = (TextView) findViewById(R.id.txt_dan3);
		dan3.setTypeface(ubuntuFont);
		grad = (TextView) findViewById(R.id.txt_grad);
		grad.setTypeface(ubuntuFont);
		tlak = (TextView) findViewById(R.id.txt_tlak);
		tlak.setTypeface(ubuntuFont);
		vlaznost = (TextView) findViewById(R.id.txt_vlaznost);
		vlaznost.setTypeface(ubuntuFont);
		vjetar = (TextView) findViewById(R.id.txt_brzina);
		vjetar.setTypeface(ubuntuFont);
		stanje = (TextView) findViewById(R.id.txt_stanje);
		stanje.setTypeface(ubuntuFont);
		grad.setVisibility(View.GONE);
		
		img_glavna = (ImageView) findViewById(R.id.img_glavna);
		img_dan1 = (ImageView) findViewById(R.id.img_dan1);
		img_dan2 = (ImageView) findViewById(R.id.img_dan2);
		img_dan3 = (ImageView) findViewById(R.id.img_dan3);

		
		units = "metric";
		
		srednja1.setVisibility(View.GONE);
        srednja2.setVisibility(View.GONE);
        srednja3.setVisibility(View.GONE);
        srednja4.setVisibility(View.GONE);
        dan1.setVisibility(View.GONE);
        dan2.setVisibility(View.GONE);
        dan3.setVisibility(View.GONE);
        grad.setVisibility(View.GONE); 
        vjetar.setVisibility(View.GONE); 
        tlak.setVisibility(View.GONE); 
        vlaznost.setVisibility(View.GONE); 
        stanje.setVisibility(View.GONE);
    	img_glavna.setVisibility(View.GONE); 
    	img_dan1.setVisibility(View.GONE); 
    	img_dan2.setVisibility(View.GONE); 
    	img_dan3.setVisibility(View.GONE);
    	
		getData();
		
	}
	private void getData() {
		if(Utils.isNetworkAvailable(MainActivity.this)){

			new GetWeather().execute();

		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			   builder.setMessage("Internet veza nije dostupna.")
			          .setTitle(getString(R.string.warning))
			          .setIcon(android.R.drawable.ic_dialog_alert)
			          .setCancelable(false)
			          .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
			              public void onClick(DialogInterface dialog, int id) {
			            	  Intent intent = new Intent(Intent.ACTION_MAIN);
			            	  intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
			            	  startActivity(intent);
			      		      dialog.dismiss();
			              }
			          })
		          .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int id) {

		      		      dialog.dismiss();
		              }
		          });
			   AlertDialog alert = builder.create();
			   alert.show();
		}
		
	}
	public class GetWeather extends AsyncTask<String, String, String> {

	    @Override
	    protected void onPreExecute() {
	    	
	    	srednja1.setVisibility(View.GONE);
            srednja2.setVisibility(View.GONE);
            srednja3.setVisibility(View.GONE);
            srednja4.setVisibility(View.GONE);
            dan1.setVisibility(View.GONE);
            dan2.setVisibility(View.GONE);
            dan3.setVisibility(View.GONE);
            grad.setVisibility(View.GONE); 
            vjetar.setVisibility(View.GONE); 
            tlak.setVisibility(View.GONE); 
            vlaznost.setVisibility(View.GONE); 
            stanje.setVisibility(View.GONE);
	    	img_glavna.setVisibility(View.GONE); 
	    	img_dan1.setVisibility(View.GONE); 
	    	img_dan2.setVisibility(View.GONE); 
	    	img_dan3.setVisibility(View.GONE);
	    	
	    	
	        super.onPreExecute();
	        db.openDataBase();
	        db.clear();
	        db.close();
	        pb.setVisibility(View.VISIBLE);
	    }

	    protected String doInBackground(String... params) {
	    	settings = getSharedPreferences("com.spring.weather",0);
			city = settings.getString("city", "London"); 
			
	    	String gradfix = city.replace(" ", "%20");
			weathercurrenturl = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&units="+units;
			weatherurl = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+gradfix+"&mode=json&units="+units+"&cnt=4";
	    	// getting JSON string from URL
	    	JSONObject json = jParser.getJSONFromUrl(weatherurl);
	    	try {
	            // Getting Array of Contacts
	    		JSONArray prognoza = null;
	    		prognoza = json.getJSONArray("list");
	             
	            // looping through All Contacts
	            for(int i = 0; i < prognoza.length(); i++){
	                JSONObject c = prognoza.getJSONObject(i);
	                 
	                String dt = c.getString("dt");
	                
	                JSONObject temp = c.getJSONObject("temp");
	                String day = temp.getString("day");
	                String min = temp.getString("min");
	                String max = temp.getString("max");
                    
	                String pressure = c.getString("pressure");
	                String humidity = c.getString("humidity");

                    String id = null;
                    String main = null;
                    String description = null;
                    String icon = null;
	                JSONArray weather = c.getJSONArray("weather");
	                for(int index = 0; index < weather.length(); index++){
	                JSONObject weatherObject = weather.getJSONObject(index);
	                id = weatherObject.getString("id");
	                main = weatherObject.getString("main");
	                description = weatherObject.getString("description");
	                icon = weatherObject.getString("icon");

	                }
	                
	                String speed = c.getString("speed");
	                String deg = c.getString("deg"); //smjer vjerta u stupnjevima
	                String clouds = c.getString("clouds");

	                String rain = "0";
					try {
						rain = c.getString("rain");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    db.openDataBase();
                    db.insertData(dt, day, min, max, pressure, humidity, id, main, description, icon, speed, deg, clouds, rain);
                    db.close();
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	    	JSONObject json2 = jParser.getJSONFromUrl(weathercurrenturl);
	    	String id = null,main = null,description = null,icon = null,temp = null,pressure = null,humidity = null,temp_min = null,temp_max = null,speed = null,deg = null;
    		try {
    			JSONArray prognoza = null;
				prognoza = json2.getJSONArray("weather");
				
				for(int i = 0; i < prognoza.length(); i++){
	                JSONObject c = prognoza.getJSONObject(i);
	                 
	                id = c.getString("id");
	                main = c.getString("main");
	                description = c.getString("description");
	                icon = c.getString("icon");
				}
				
				JSONObject temp2 = json2.getJSONObject("main");
				temp = temp2.getString("temp");
				pressure = temp2.getString("pressure");
				humidity = temp2.getString("humidity");
				temp_min = temp2.getString("temp_min");
				temp_max = temp2.getString("temp_max");
                
				JSONObject temp3 = json2.getJSONObject("wind");
				speed = temp3.getString("speed");
				deg = temp3.getString("deg");

				
				db.openDataBase();
                db.insertData("", temp, temp_min, temp_max, pressure, humidity, id, main, description, icon, speed, deg, "", "");
                db.close();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    	

	        return null;
	    }

	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog once got all details
	    	String gradfix = city.replace("%20", " ");
	    	grad.setText(gradfix);
	    	pb.setVisibility(View.GONE);
	    	grad.setVisibility(View.VISIBLE);
	    	db.openDataBase();
	    	final ArrayList<HashMap<String, Object>> lista = db.takeData(5);
			for (HashMap<String, Object> map : lista)
				for (Entry<String, Object> mapEntry : map.entrySet()) {

					double srednja = Double.parseDouble(map.get("day").toString());
                    double ssrednja2 = (double) Math.floor(srednja);
                    srednja1.setText(format.format(ssrednja2)+"°C");
                    tlak.setText(map.get("pressure").toString()+" hpa");
                    tlak.setVisibility(View.VISIBLE);
                    vlaznost.setText(map.get("humidity").toString()+" %");
                    vlaznost.setVisibility(View.VISIBLE);
                    vjetar.setText(map.get("speed").toString()+" m/s");
                    vjetar.setVisibility(View.VISIBLE);
                    String imgrestring = map.get("icon").toString();
                    Bitmap slika = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("img"+imgrestring, "drawable", getPackageName()));
                    img_glavna.setImageBitmap(slika);
                    Float stupnjevi = Float.valueOf(map.get("deg").toString());
                    Log.d("jea", String.valueOf(stupnjevi));
                    Bitmap bmpOriginal = BitmapFactory.decodeResource(context.getResources(), R.drawable.wind);
                    Bitmap bmResult = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas tempCanvas = new Canvas(bmResult); 
                    tempCanvas.rotate(stupnjevi, bmpOriginal.getWidth()/2, bmpOriginal.getHeight()/2);
                    tempCanvas.drawBitmap(bmpOriginal, 0, 0, null);
                    BitmapDrawable mDrawable = new BitmapDrawable(getResources(),bmResult);
                    
                    vjetar.setCompoundDrawablesWithIntrinsicBounds( null, null, mDrawable, null);
                    stanje.setText(map.get("main").toString());
				}
			
	    	final ArrayList<HashMap<String, Object>> lista2 = db.takeData(2);
			for (HashMap<String, Object> map : lista2)
				for (Entry<String, Object> mapEntry : map.entrySet()) {
					long timestamp = Long.parseLong(map.get("dt").toString()); //Example
					Date d = new Date(timestamp * 1000);
					String dan = d.toString();
					dan1.setText(dan);
					
					double max = Double.parseDouble(map.get("max").toString());
                    double max2 = (double) Math.floor(max);
                    double min = Double.parseDouble(map.get("min").toString());
                    double min2 = (double) Math.floor(min);
                    srednja2.setText(format.format(max2)+"°/"+format.format(min2)+"°");

                    String imgrestring = map.get("icon").toString();
                    Bitmap slika = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("img"+imgrestring, "drawable", getPackageName()));
                    img_dan1.setImageBitmap(slika);
				}
			
	    	final ArrayList<HashMap<String, Object>> lista3 = db.takeData(3);
			for (HashMap<String, Object> map : lista3)
				for (Entry<String, Object> mapEntry : map.entrySet()) {
					long timestamp = Long.parseLong(map.get("dt").toString()); //Example
					Date d = new Date(timestamp * 1000);
					String dan = d.toString();
					dan2.setText(dan);
					double max = Double.parseDouble(map.get("max").toString());
                    double max2 = (double) Math.floor(max);
                    double min = Double.parseDouble(map.get("min").toString());
                    double min2 = (double) Math.floor(min);
                    srednja3.setText(format.format(max2)+"°/"+format.format(min2)+"°");

                    String imgrestring = map.get("icon").toString();
                    Bitmap slika = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("img"+imgrestring, "drawable", getPackageName()));
                    img_dan2.setImageBitmap(slika);
				}
	    	final ArrayList<HashMap<String, Object>> lista4 = db.takeData(4);
			for (HashMap<String, Object> map : lista4)
				for (Entry<String, Object> mapEntry : map.entrySet()) {
					long timestamp = Long.parseLong(map.get("dt").toString()); //Example
					Date d = new Date(timestamp * 1000);
					String dan = d.toString();
					dan3.setText(dan);
					double max = Double.parseDouble(map.get("max").toString());
                    double max2 = (double) Math.floor(max);
                    double min = Double.parseDouble(map.get("min").toString());
                    double min2 = (double) Math.floor(min);
                    srednja4.setText(format.format(max2)+"°/"+format.format(min2)+"°");
                    String imgrestring = map.get("icon").toString();
                    Bitmap slika = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("img"+imgrestring, "drawable", getPackageName()));
                    img_dan3.setImageBitmap(slika);
				}
			
			db.close();
			srednja1.setVisibility(View.VISIBLE);
            srednja2.setVisibility(View.VISIBLE);
            srednja3.setVisibility(View.VISIBLE);
            srednja4.setVisibility(View.VISIBLE);
            dan1.setVisibility(View.VISIBLE);
            dan2.setVisibility(View.VISIBLE);
            dan3.setVisibility(View.VISIBLE);
            grad.setVisibility(View.VISIBLE); 
            vjetar.setVisibility(View.VISIBLE); 
            tlak.setVisibility(View.VISIBLE); 
            vlaznost.setVisibility(View.VISIBLE); 
            stanje.setVisibility(View.VISIBLE);
	    	img_glavna.setVisibility(View.VISIBLE); 
	    	img_dan1.setVisibility(View.VISIBLE); 
	    	img_dan2.setVisibility(View.VISIBLE); 
	    	img_dan3.setVisibility(View.VISIBLE);

	    }
	}
	// Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
     
    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.menu_refresh:
        	getData();
            return true;
 
        case R.id.menu_city:
        	final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_city);
			dialog.setTitle(getString(R.string.enter_city_name));
			dialog.setCancelable(true);
			final EditText city = (EditText) dialog.findViewById(R.id.editText1);
			LinearLayout ldialog = (LinearLayout) dialog.findViewById(R.id.LinearDialog);
			Button btClose = (Button) ldialog.findViewById(R.id.btClose);
			Button btSave = (Button) ldialog.findViewById(R.id.btSave);
			btClose.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
				
			});
			btSave.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String newCity = city.getText().toString();
					String gradfix = newCity.replace(" ", "%20");
					settings.edit().putString("city", gradfix).commit();
					getData();
					dialog.dismiss();
				}
				
			});
			dialog.show();
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
}
