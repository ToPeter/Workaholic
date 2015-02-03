package eu.alterway.workaholic;

import java.security.MessageDigest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import eu.alterway.workaholic.database.DatabaseAdapter;
import eu.alterway.workaholic.languages.English;
import eu.alterway.workaholic.model.User;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements OnClickListener,OnEditorActionListener,English
{
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask authenticationTask = null;
	private UserRegistrationTask registrationTask = null;

	// Values for email and password at the time of the login attempt.
	private DatabaseAdapter dbAdapter;
	private User existingUser;
	private SharedPreferences preferences;
	
	private String email;
	private String password;
	
	private String firstName;
	private String lastName;

	// UI references.
	private MenuItem recoverPassword;
	private MenuItem infoAbout;
	
	private View loginFormView;
	private TextView userName;
	private EditText passwordLoginView;
	private CheckBox rememberSwitch;
	private Button signInButton;
	private Button registerButton;
	
	private View registerFormView;
	private EditText firstNameRegisterView;
	private EditText lastNameRegisterView;
	private EditText emailRegisterView;
	private EditText passwordRegisterView;
	private EditText repeatPasswordView;
	private Button registeringButton;
	
	private View loginStatusView;
	private ImageView clock;
	private ImageView hour;
	private TextView loginStatusMessage;
	
	private boolean recovering;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		recovering = false;
		
		dbAdapter = new DatabaseAdapter(LoginActivity.this);
		dbAdapter.open();
		
		existingUser = dbAdapter.getUser();
		
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if (preferences.getBoolean("rememberPassword", false))
		{
			Intent login = new Intent(LoginActivity.this, MainActivity.class);
			login.putExtra(SIGNED_USER, existingUser);
			startActivity(login);
			finish();
		}
		
		// Set up the login form.
		loginFormView = findViewById(R.id.login_form);
			signInButton = (Button) findViewById(R.id.sign_in_button);
			userName = (TextView) findViewById(R.id.full_name);
			passwordLoginView = (EditText) findViewById(R.id.password);
			rememberSwitch = (CheckBox) findViewById(R.id.remember_switch);
			registerButton = (Button) findViewById(R.id.register_button);
			
			signInButton.setOnClickListener(this);
			registerButton.setOnClickListener(this);
		
		registerFormView = findViewById(R.id.register_form);
			firstNameRegisterView = (EditText) findViewById(R.id.account_firstN);
			lastNameRegisterView = (EditText) findViewById(R.id.surname);
			emailRegisterView = (EditText) findViewById(R.id.email_register);
			passwordRegisterView = (EditText) findViewById(R.id.password_register);
			repeatPasswordView = (EditText) findViewById(R.id.repeat_password);
			registeringButton = (Button) findViewById(R.id.registering_button);
			
			email = getIntent().getStringExtra(EXTRA_EMAIL);
			emailRegisterView.setText(email);
			
			registeringButton.setOnClickListener(this);
		
		loginStatusView = findViewById(R.id.login_status);
			clock = (ImageView) findViewById(R.id.clock);
			hour = (ImageView) findViewById(R.id.hour);
			loginStatusMessage = (TextView) findViewById(R.id.login_status_message);
			
		if (existingUser != null)
		{
			userName.setText(existingUser.getFirstName()+" "+existingUser.getLastName());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		recoverPassword = menu.findItem(R.id.forgot_password);
		infoAbout = menu.findItem(R.id.about);
		return true;
	}
	
	@Override
	protected void onPause() 
	{
	    if (dbAdapter != null)
	    {
	        dbAdapter.close();
	    }
	    super.onPause();
	}
	
	@Override
    protected void onResume() 
	{
        super.onResume();
        dbAdapter.open();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		try
		{
			switch(item.getItemId())
			{
			case android.R.id.home:
				slideToLogin();
				return true;
			case R.id.about:
				Intent toAbout = new Intent(LoginActivity.this, AboutActivity.class);
				startActivity(toAbout);
				return true;
			case R.id.forgot_password:
				
				return true;
			default:
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			return super.onOptionsItemSelected(item);
		}
	}

	private void clearRegisterForm()
	{
		firstNameRegisterView.setText(NOTHING);
		lastNameRegisterView.setText(NOTHING);
		emailRegisterView.setText(NOTHING);
		passwordRegisterView.setText(NOTHING);
		repeatPasswordView.setText(NOTHING);
		firstNameRegisterView.setError(null);
		lastNameRegisterView.setError(null);
		emailRegisterView.setError(null);
		passwordRegisterView.setError(null);
		repeatPasswordView.setError(null);
		registeringButton.setText("Register");
	}
	
	private void slideToRegister()
	{
		Animation leftSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_out_left);
		Animation leftSlide = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_in_left);
		loginFormView.startAnimation(leftSwipe);
	    registerFormView.startAnimation(leftSlide);
	    loginFormView.setVisibility(View.GONE);
	    recoverPassword.setVisible(false);
	    registerFormView.setVisibility(View.VISIBLE);
	    passwordLoginView.setText(NOTHING);
	    passwordLoginView.setError(null);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	}
	
	private void slideToLogin()
	{
		Animation rightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_out_right);
		Animation rightSlide = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_in_right);
		registerFormView.startAnimation(rightSwipe);
		loginFormView.startAnimation(rightSlide);
		registerFormView.setVisibility(View.GONE);
		recoverPassword.setVisible(true);
		loginFormView.setVisibility(View.VISIBLE);
		clearRegisterForm();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLogin() 
	{
		if (authenticationTask != null) 
		{
			return;
		}

		// Reset errors.
		passwordLoginView.setError(null);

		// Store values at the time of the login attempt.
		password = passwordLoginView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(password)) 
		{
			passwordLoginView.setError(getString(R.string.error_field_required));
			focusView = passwordLoginView;
			cancel = true;
		} 
		else if (password.length() < 4) 
		{
			passwordLoginView.setError(getString(R.string.error_invalid_password));
			focusView = passwordLoginView;
			cancel = true;
		}

		if (cancel) 
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} 
		else 
		{
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			loginStatusMessage.setText(R.string.login_progress_signing_in);
			showProgress(true,loginFormView);
			authenticationTask = new UserLoginTask();
			authenticationTask.execute((Void) null);
		}
	}
	
	private void attemptRegister()
	{
		
		if (registrationTask != null)
		{
			return;
		}
		
		// Reset errors.
		firstNameRegisterView.setError(null);
		lastNameRegisterView.setError(null);
		emailRegisterView.setError(null);
		passwordRegisterView.setError(null);
		repeatPasswordView.setError(null);

		// Store values at the time of the login attempt.
		firstName = firstNameRegisterView.getText().toString();
		lastName = lastNameRegisterView.getText().toString();
		email = emailRegisterView.getText().toString();
		password = passwordRegisterView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(firstName))
		{
			firstNameRegisterView.setError(getString(R.string.error_field_required));
			focusView = firstNameRegisterView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(lastName))
		{
			lastNameRegisterView.setError(getString(R.string.error_field_required));
			focusView = lastNameRegisterView;
			cancel = true;
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) 
		{
			passwordRegisterView.setError(getString(R.string.error_field_required));
			focusView = passwordRegisterView;
			cancel = true;
		} 
		else if (password.length() < 4) 
		{
			passwordRegisterView.setError(getString(R.string.error_invalid_password));
			focusView = passwordRegisterView;
			cancel = true;
		}
		else if (!password.equals(repeatPasswordView.getText().toString()))
		{
			passwordRegisterView.setError(getString(R.string.error_different_values_password));
			focusView = passwordRegisterView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) 
		{
			emailRegisterView.setError(getString(R.string.error_field_required));
			focusView = emailRegisterView;
			cancel = true;
		} 
		else if (!email.contains("@")) 
		{
			emailRegisterView.setError(getString(R.string.error_invalid_email));
			focusView = emailRegisterView;
			cancel = true;
		}

		if (cancel) 
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} 
		else 
		{
			if (recovering)
			{
				loginStatusMessage.setText(R.string.changing_password_progress);
			}
			else
			{
				loginStatusMessage.setText(R.string.registration_progress);
			}
			// Register
			showProgress(true, registerFormView);
			registrationTask = new UserRegistrationTask();
			registrationTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show, final View showingView) 
	{
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) 
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			
			//get the clock turn animation
			final Animation clockTurn = AnimationUtils.loadAnimation(this, R.anim.clock_turn);
			final Animation hourTurn = AnimationUtils.loadAnimation(this, R.anim.hour_turn);
			
			if (show)
			{
				clock.startAnimation(clockTurn);
				hour.startAnimation(hourTurn);
			}

			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							recoverPassword.setEnabled(show ? false : true);
							infoAbout.setEnabled(show ? false : true);
							
							loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			showingView.setVisibility(View.VISIBLE);
			showingView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) 
						{
							showingView.setVisibility(show ? View.GONE
									: View.VISIBLE);
							if (!show)
							{
								clock.clearAnimation();
								hour.clearAnimation();
							}
						}
					});
		} 
		else 
		{
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			showingView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Listeners:
	 * @author marekrigan
	 */
	@Override
	public void onClick(View v) 
	{
		try
		{
			switch (v.getId())
			{
			case R.id.sign_in_button:
				attemptLogin();
				break;
			case R.id.register_button:
				if (existingUser == null)
				{
					slideToRegister();
				}
				else
				{
					// custom dialog
					final Dialog dialog = new Dialog(this,R.style.Theme_Workaholic_Dialog);
					dialog.setContentView(R.layout.old_account);
					
					TextView dialogText = (TextView) dialog.findViewById(R.id.existing_account_text);
					dialogText.setText(Html.fromHtml(EXISTING_USER));
					
					final EditText input = (EditText) dialog.findViewById(R.id.password_to_delete);
		 
					Button deleteOld = (Button) dialog.findViewById(R.id.register_new);
					Button cancel = (Button) dialog.findViewById(R.id.cancel);
					// if button is clicked, close the custom dialog
					cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					deleteOld.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							try
					    	{
						    	String passwordForDeleting = input.getText().toString();
						    	
						    	MessageDigest md = MessageDigest.getInstance("SHA-256");
								md.update(passwordForDeleting.getBytes());
								byte[] digest = md.digest();
								StringBuffer sb = new StringBuffer();
								for (int i=0; i<digest.length; i++)
								{
									sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
								}
								String hashedPassword = sb.toString();
								
								md.update(hashedPassword.getBytes());
								digest = md.digest();
								sb = new StringBuffer();
								for (int i=0; i<digest.length; i++)
								{
									sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
								}
								hashedPassword = sb.toString();
								
								if (hashedPassword.equals(existingUser.getPassword()))
								{
									dbAdapter.deleteUser();
									existingUser = null;
									userName.setText(NOTHING);
									InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
									dialog.dismiss();
						        	slideToRegister();
								}
								else
								{
									InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
									dialog.dismiss();
								
									// custom dialog
									final Dialog incorrectDialog = new Dialog(LoginActivity.this,R.style.Theme_Workaholic_Dialog);
									incorrectDialog.setContentView(R.layout.registration_successful);
									
									TextView title = (TextView) incorrectDialog.findViewById(R.id.title_confirmation);
									title.setVisibility(View.GONE);
									
									TextView text = (TextView) incorrectDialog.findViewById(R.id.text_confirmation);
									text.setText(Html.fromHtml(INCORRECT_PASSWORD));
									
									Button dismiss = (Button) incorrectDialog.findViewById(R.id.dismiss_confirmation);
									// if button is clicked, close the custom dialog
									dismiss.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											incorrectDialog.dismiss();
										}
									});
									
									incorrectDialog.show();
									
								}
						    }
						    catch (Exception e)
					    	{
					    		dialog.cancel();
					    	}
						}
					});
					
					dialog.show();
				}
				break;
			case R.id.registering_button:
				attemptRegister();
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int id, KeyEvent event) 
	{
		if (id == R.id.login || id == EditorInfo.IME_NULL) 
		{
			attemptLogin();
			return true;
		}
		return false;
	}

	/**
	 * AsyncTasks:
	 * @author marekrigan
	 */
	
	/*
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> 
	{
		
		@Override
		protected void onPreExecute() 
		{
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(passwordLoginView.getWindowToken(), 0);
		}

		@Override
		protected Boolean doInBackground(Void... params) 
		{	
			try 
			{
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes());
				byte[] digest = md.digest();
				StringBuffer sb = new StringBuffer();
				for (int i=0; i<digest.length; i++)
				{
					sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
				}
				String hashedPassword = sb.toString();
				
				md.update(hashedPassword.getBytes());
				digest = md.digest();
				sb = new StringBuffer();
				for (int i=0; i<digest.length; i++)
				{
					sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
				}
				hashedPassword = sb.toString();
				
				// Simulate network access.
				Thread.sleep(2000);
				
				if (hashedPassword.equals(existingUser.getPassword()))
				{
					return true;
				}
				else
				{
					return false;
				}
			} 
			catch(InterruptedException e) 
			{
				return false;
			}
			catch(Exception e)
			{
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) 
		{
			authenticationTask = null;

			if (success) 
			{
				if (rememberSwitch.isChecked())
				{
					Editor edit = preferences.edit();
					edit.putBoolean("rememberPassword", true);
					edit.apply();
				}
				Intent login = new Intent(LoginActivity.this, MainActivity.class);
				login.putExtra(SIGNED_USER, existingUser);
				startActivity(login);
				finish();
			} 
			else 
			{
				showProgress(false,loginFormView);
				passwordLoginView.setError(getString(R.string.error_incorrect_password));
				passwordLoginView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() 
		{
			authenticationTask = null;
			showProgress(false,loginFormView);
		}
	}
	
	public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected void onPreExecute() 
		{
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(passwordLoginView.getWindowToken(), 0);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) 
		{	
			try 
			{
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes());
				byte[] digest = md.digest();
				StringBuffer sb = new StringBuffer();
				for (int i=0; i<digest.length; i++)
				{
					sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
				}
				String hashedPassword = sb.toString();
				
				md = MessageDigest.getInstance("SHA-256");
				md.update(hashedPassword.getBytes());
				digest = md.digest();
				sb = new StringBuffer();
				for (int i=0; i<digest.length; i++)
				{
					sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
				}
				hashedPassword = sb.toString();
				
				if (recovering)
				{
					existingUser.setFirstName(firstName);
					existingUser.setLastName(lastName);
					existingUser.setEmail(email);
					existingUser.setPassword(hashedPassword);
					dbAdapter.updateUser(existingUser);
				}
				else
				{
					existingUser = new User(email, firstName, lastName, hashedPassword);
					existingUser.setId(dbAdapter.createUser(existingUser));
				}
				
				// Simulate network access.
				Thread.sleep(2000);
				
				return true;
			} 
			catch(InterruptedException e) 
			{
				return false;
			}
			catch(Exception e)
			{
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) 
		{
			userName.setText(existingUser.getFirstName()+" "+existingUser.getLastName());
			
			registrationTask = null;
			showProgress(false,registerFormView);

			if (success) 
			{
				// custom dialog
				final Dialog dialog = new Dialog(LoginActivity.this,R.style.Theme_Workaholic_Dialog);
				dialog.setContentView(R.layout.registration_successful);
				
				TextView dialogTitle = (TextView) dialog.findViewById(R.id.title_confirmation);
				TextView dialogText = (TextView) dialog.findViewById(R.id.text_confirmation);
				if (recovering)
				{
					dialogTitle.setText(PASSWORD_CHANGED_TITLE);
					dialogText.setText(Html.fromHtml(PASSWORD_CHANGED));
					recovering = false;
				}
				else
				{
					dialogText.setText(Html.fromHtml(SUCCESSFUL_REGISTRATION));
				}
				
				
				Button dismiss = (Button) dialog.findViewById(R.id.dismiss_confirmation);
				// if button is clicked, close the custom dialog
				dismiss.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) 
					{
						dialog.dismiss();
						slideToLogin();
						clearRegisterForm();
					}
				});
				
				dialog.show();
				
			} 
			else 
			{
				// custom dialog
				final Dialog dialog = new Dialog(LoginActivity.this,R.style.Theme_Workaholic_Dialog);
				dialog.setContentView(R.layout.registration_successful);
				
				TextView dialogTitle = (TextView) dialog.findViewById(R.id.title_confirmation);
				dialogTitle.setText(ERROR_REGISTRATION_TITLE);
				
				TextView dialogText = (TextView) dialog.findViewById(R.id.text_confirmation);
				dialogText.setText(Html.fromHtml(ERROR_REGISTRATION));
				
				Button dismiss = (Button) dialog.findViewById(R.id.dismiss_confirmation);
				// if button is clicked, close the custom dialog
				dismiss.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) 
					{
						dialog.dismiss();
						slideToLogin();
						clearRegisterForm();
					}
				});
				
				dialog.show();
			}
		}

		@Override
		protected void onCancelled() 
		{
			authenticationTask = null;
			showProgress(false,registerFormView);
		}
	}
}
