package eu.alterway.workaholic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.database.DatabaseAdapter;

public class HomeFragment extends Fragment implements OnClickListener
{	
    private DatabaseAdapter dbAdapter;
    
    public HomeFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
   
        dbAdapter = new DatabaseAdapter(getActivity());
          
        return rootView;
    }

	@Override
	public void onPause() 
	{
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
	public void onResume() 
	{
		super.onResume();
		dbAdapter.open();
	}
	
	@Override
	public void onClick(View v) 
	{
		try
		{
			switch(v.getId())
			{
			default:
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
