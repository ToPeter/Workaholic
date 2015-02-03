package eu.alterway.workaholic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.languages.English;
import eu.alterway.workaholic.model.Job;
import eu.alterway.workaholic.util.Wording;

public class JobsDetailsAdapter extends BaseAdapter implements English
{
	private Job job;
	private LayoutInflater inflater;
	
	public JobsDetailsAdapter(Context context, Job job)
	{
		inflater = LayoutInflater.from(context);
		this.job = job;
	}

	@Override
	public int getCount() 
	{
		return 6;
	}

	@Override
	public Object getItem(int position) 
	{
		if (job == null)
		{
			return null;
		}
		
		switch(position)
		{
		case 0:
			return job.getWorkplace();
		case 1: 
			return job.getPosition();
		case 2:
			return job.getSalary();
		case 3:
			return job.getTax();
		case 4:
			return job.getDeadline();
		case 5:
			return job.getDescription();
		default:
			return null;
		}
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.job_detail, parent, false);
			
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.value = (TextView) convertView.findViewById(R.id.value);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		try
		{

			if (job != null)
			{
				switch(position)
				{
				case 0:
					holder.label.setText(WORKPLACE_LABEL);
					holder.value.setText(job.getWorkplace());
					break;
				case 1: 
					holder.label.setText(POSITION_LABEL);
					holder.value.setText(job.getPosition());
					break;
				case 2:
					holder.label.setText(SALARY_LABEL);
					holder.value.setText(String.valueOf(job.getSalary()));
					break;
				case 3:
					holder.label.setText(TAX_LABEL);
					holder.value.setText(String.valueOf(job.getTax())+" %");
					break;
				case 4:
					holder.label.setText(DEADLINE_LABEL);
					holder.value.setText(Wording.getDeadlineString(job.getDeadline()));
					break;
				case 5:
					holder.label.setText(DESCRIPTION_LABEL);
					holder.value.setText(job.getDescription());
					break;
				default:
					throw new Exception();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return convertView;
	}

	private class ViewHolder
	{
		TextView label;
		TextView value;
	}

}
