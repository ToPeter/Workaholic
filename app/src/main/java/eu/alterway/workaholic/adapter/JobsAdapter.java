package eu.alterway.workaholic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.languages.English;
import eu.alterway.workaholic.model.Job;

public class JobsAdapter extends BaseExpandableListAdapter implements English,View.OnClickListener
{
	private List<Job> jobs;
	private LayoutInflater inflater;
    private GridView jobDetails;
    private JobsDetailsAdapter detailsAdapter;
    private Context context;
    private String shadeColor = "#bdc3c7";
	
	public JobsAdapter(Context context, List<Job> jobs)
	{
		inflater = LayoutInflater.from(context);
		this.jobs = jobs;
        this.context = context;
	}

    @Override
    public int getGroupCount()
    {
        return jobs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return jobs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        switch (childPosition)
        {
            case WORKPLACE:
                return jobs.get(groupPosition).getWorkplace();
            case POSITION:
                return jobs.get(groupPosition).getPosition();
            case SALARY:
                return jobs.get(groupPosition).getSalary();
            case TAX:
                return jobs.get(groupPosition).getTax();
            case DEADLINE:
                return jobs.get(groupPosition).getDeadline();
            case DESCRIPTION:
                return jobs.get(groupPosition).getDescription();
            default:
                return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return jobs.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        ViewGroupHolder groupHolder;

		if (convertView == null)
		{
            groupHolder = new ViewGroupHolder();

			convertView = inflater.inflate(R.layout.job_list, parent, false);

            groupHolder.workplace = (TextView) convertView.findViewById(R.id.workplace);
            groupHolder.position = (TextView) convertView.findViewById(R.id.position);
            groupHolder.salary = (TextView) convertView.findViewById(R.id.salary);
            groupHolder.trash = (ImageButton) convertView.findViewById(R.id.delete_job);

			convertView.setTag(groupHolder);
		}
		else
		{
            groupHolder = (ViewGroupHolder) convertView.getTag();
		}

		Job job = jobs.get(groupPosition);

        groupHolder.workplace.setText(job.getWorkplace());
        groupHolder.position.setText(job.getPosition());
        groupHolder.salary.setText(String.valueOf(job.getSalary()));

		return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.job_details, parent, false);

            jobDetails = (GridView) convertView.findViewById(R.id.job_details);

            final int rowHeightDp = 68;

            final float rowHeight = context.getResources().getDisplayMetrics().density * rowHeightDp;

            jobDetails.getLayoutParams().height = Math.round(3 * rowHeight);

            convertView.setTag(jobDetails);
        }
        else
        {
            jobDetails = (GridView) convertView.getTag();
        }

        Job job = jobs.get(groupPosition);

        detailsAdapter = new JobsDetailsAdapter(context,job);
        jobDetails.setAdapter(detailsAdapter);

        return convertView;
    }

    public void showTrash(View savedView)
    {
        ViewGroupHolder groupHolder = (ViewGroupHolder) savedView.getTag();
        groupHolder.trash.setVisibility(View.VISIBLE);
        groupHolder.position.setTextColor(Color.WHITE);
    }

    public void hideTrash(View savedView)
    {
        ViewGroupHolder groupHolder = (ViewGroupHolder) savedView.getTag();
        groupHolder.trash.setVisibility(View.GONE);
        groupHolder.position.setTextColor(Color.parseColor(shadeColor));
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.delete_job:

        }
    }

    private class ViewGroupHolder
	{
		TextView workplace;
		TextView position;
		TextView salary;
        ImageButton trash;
	}

}
