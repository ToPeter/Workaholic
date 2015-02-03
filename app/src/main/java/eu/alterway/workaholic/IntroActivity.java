package eu.alterway.workaholic;

import eu.alterway.workaholic.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class IntroActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		new LoadData().execute();
	}
	
	private class LoadData extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) 
		{
			try
			{
				Thread.sleep(3000);
				
				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) 
		{
			try
			{
				if (success)
				{
					Intent continuing = new Intent(IntroActivity.this, LoginActivity.class);
					startActivity(continuing);
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
					finish();
				}
				else
				{
					throw new Exception();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

}

