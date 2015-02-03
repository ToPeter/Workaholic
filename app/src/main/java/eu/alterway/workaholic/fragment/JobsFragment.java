package eu.alterway.workaholic.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ViewFlipper;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.adapter.JobsAdapter;
import eu.alterway.workaholic.adapter.JobsDetailsAdapter;
import eu.alterway.workaholic.database.DatabaseAdapter;
import eu.alterway.workaholic.languages.English;
import eu.alterway.workaholic.model.Job;

public class JobsFragment extends Fragment implements English, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener
// implements OnItemClickListener,OnClickListener,TextWatcher,OnItemSelectedListener,English
{
	private DatabaseAdapter dbAdapter;
	private List<Job> jobs;
	private Job selectedJob;
	
	private ExpandableListView jobsList;
	
	private ViewFlipper valueToEdit;
	
	private JobsAdapter jobsAdapter;
	private JobsDetailsAdapter jobsDetailsAdapter;
	
//	private Job defaultJob;

    private FloatingActionsMenu addJobMenu;
	private View selectedView;
//	private int selectedPosition;
	private String highlightColor = "#bdc3c7";
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        
        dbAdapter = new DatabaseAdapter(getActivity());
        
        new LoadJobs().execute(rootView);
          
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
	
	private void selectItemInList(View view)
	{
		unselectItemInList();
		
		selectedView = view;
		selectedView.setBackgroundColor(Color.parseColor(highlightColor));
	}
	
	private void unselectItemInList()
	{
		if (selectedView != null)
		{
			selectedView.setBackgroundColor(Color.WHITE);
			selectedView = null;
		}
	}
	
	private void doneProcessEditing()
	{
		valueToEdit.setDisplayedChild(0);
		unselectItemInList();
	}

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
    {

        if (jobsList.isGroupExpanded(groupPosition))
        {
            jobsList.collapseGroup(groupPosition);
            jobsAdapter.hideTrash(v);
            unselectItemInList();
        }
        else
        {
            selectItemInList(v);
            jobsList.expandGroup(groupPosition,true);
            jobsAdapter.showTrash(v);
        }


        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
        return false;
    }

    private class LoadJobs extends AsyncTask<View, String, Boolean>
	{

		@Override
		protected Boolean doInBackground(View... roots) 
		{
			try
			{
//				jobs = dbAdapter.getAllJobs();
				
				jobs = new ArrayList<Job>();
				jobs.add(new Job(1, "Palace Hotel", "Kitchen Helper", 120, 36, 20, true, "Dishes, Kitchen, Hotel"));
				jobs.add(new Job(2, "ALTERway", "Mobile Developer", 20, 50, 0, true, "Working on this application"));
				
				selectedJob = null;
				
				valueToEdit = (ViewFlipper) roots[0].findViewById(R.id.value_to_edit);
				jobsList = (ExpandableListView) roots[0].findViewById(R.id.jobs_list);

                addJobMenu = (FloatingActionsMenu) roots[0].findViewById(R.id.add_job_floating);

				jobsAdapter = new JobsAdapter(getActivity(), jobs);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			try
			{
				jobsList.setAdapter(jobsAdapter);
                jobsList.setOnGroupClickListener(JobsFragment.this);

				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
	}

}
