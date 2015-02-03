package eu.alterway.workaholic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.database.DatabaseAdapter;
import eu.alterway.workaholic.model.User;

public class AccountFragment extends Fragment 
{    
	private DatabaseAdapter dbAdapter;
	
	private User user;
	
	private TextView firstName;
	private TextView lastName;
	private TextView email;
	private TextView numberOfJobs;
	
    public AccountFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        
        dbAdapter = new DatabaseAdapter(getActivity());
        
        firstName = (TextView) rootView.findViewById(R.id.account_firstN_value);
        lastName = (TextView) rootView.findViewById(R.id.account_lastN_value);
        email = (TextView) rootView.findViewById(R.id.account_email_value);
        numberOfJobs = (TextView) rootView.findViewById(R.id.account_nOfJobs_value);
          
        return rootView;
    }
    
    @Override
	public void onPause() {
		if (dbAdapter != null)
		{
            dbAdapter.close();
		}
        super.onPause();
	}
	
	@Override
	public void onDestroy() 
	{
		if (dbAdapter != null)
		{
            dbAdapter.close();
		}
        super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		dbAdapter.open();
		
		user = dbAdapter.getUser();
		
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		email.setText(user.getEmail());
		numberOfJobs.setText(String.valueOf(dbAdapter.getNumberOfJobs()));
	}
}
