package br.com.bradesco.lelis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "bradesco";
	private static final String URL = "http://pastebin.com/raw.php?i=vYcLPwUA";
	private Activity activity;
	private ProgressDialog progressDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        activity = this;
        
        new JsonLoader().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	PackageManager pm = getPackageManager();
    	Intent intent;
    	
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_cv:
			intent = pm.getLaunchIntentForPackage("com.adobe.reader");
			intent.setDataAndType(Uri.parse(getString(R.string.cvpath)),"application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			break;
        case R.id.menu_email:
        	intent = new Intent(Intent.ACTION_VIEW);
        	Uri data = Uri.parse("mailto:?subject=" + TAG);
        	intent.setData(data);
        	startActivity(intent);
        	break;
        case R.id.menu_mob:
        	intent = new Intent(Intent.ACTION_DIAL);
        	intent.setData(Uri.parse("tel:11985456109"));
        	startActivity(intent);
        	break;
        case R.id.menu_skype:
    		intent = new Intent("android.intent.action.VIEW");
    		intent.setData(Uri.parse("skype:Lelisfo"));
            startActivity(intent);
        	break;
        default:
            return super.onOptionsItemSelected(item);
        }
		return super.onOptionsItemSelected(item);
	}

	public class JsonLoader extends AsyncTask<Void, Void, Void> {
    	TrendAdapter adapter;
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Processing... Please Wait...");
			progressDialog.show();
			
			adapter = new TrendAdapter(activity);
		}		
    	
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			ListView lv = (ListView) findViewById(R.id.lv_trendname);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {					
					Trend t = (Trend) parent.getItemAtPosition(position);
					Toast.makeText(activity, "List Item " + t.rulesUrl, Toast.LENGTH_LONG).show();		
					
					Uri uri = Uri.parse(t.rulesUrl);
					
					PackageManager pm = getPackageManager();
					Intent intent = pm.getLaunchIntentForPackage("com.adobe.reader");
					intent.setDataAndType(uri,"application/pdf");
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

					try {
					    startActivity(intent);
					} catch (ActivityNotFoundException e) {
					    // Instruct the user to install adobe PDF reader
			            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			            builder.setTitle("Adobe Reader não encontrado.");
			            builder.setMessage("Fazer download a partir do Android Market?");
			            builder.setPositiveButton("Sim, por favor",
			                    new DialogInterface.OnClickListener() {
			                        @Override
			                        public void onClick(DialogInterface dialog, int which) {
			                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
			                            marketIntent
			                                    .setData(Uri
			                                            .parse("market://details?id=com.adobe.reader"));
			                            startActivity(marketIntent);
			                        }
			                    });
			            builder.setNegativeButton("Não, obrigado", null);
			            builder.create().show();
					}
				}
			});
			
			lv.setAdapter(adapter);
			
			progressDialog.dismiss();
		}    	
    	
		@Override
		protected Void doInBackground(Void... params) {
			try {
				JSONArray trades = new JSONArray(Json.getJson(URL).getString("result"));
				for (int i=0; i<trades.length(); i++) {
					
					Trend item = new Trend("", "");
					
					JSONObject trade = trades.getJSONObject(i);
					item.trendname = trade.getString("tradename");
					
					JSONArray campaigns = new JSONArray(trade.getString("campaigns"));
					for(int j=0; j<campaigns.length(); j++) {
						
						JSONObject campaign = campaigns.getJSONObject(j);
						
						try {
							if(item.rulesUrl !="") {
								item.rulesUrl += "\n" + campaign.getString("rules");
							} else {
								item.rulesUrl += campaign.getString("rules");
							}
						} catch (JSONException e) {
							// ...
						}
					}
					adapter.add(item);
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
			}
			return null;
		}
    }
}
