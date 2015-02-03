package eu.alterway.workaholic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.languages.English;
import eu.alterway.workaholic.model.Shift;

/**
 * Created by marekrigan on 12/11/14.
 */
public class ShiftDetailsAdapter extends BaseAdapter implements English
{
    private LayoutInflater inflater;
    private Context context;
    private Shift shift;

    public ShiftDetailsAdapter(Context context, Shift shift)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.shift = shift;
    }

    @Override
    public int getCount()
    {
        return 5;
    }

    @Override
    public Object getItem(int position)
    {
        switch(position)
        {
            case 0:
                return shift.getJobId();
            case 1:
            case 2:
                return shift.getStart();
            case 3:
                return shift.getEnd();
            case 4:
                return shift.getPause();
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

        if (position == 0 || position == 4)
        {
            if (convertView == null)
            {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.shift_detail_spinner, parent, false);

                holder.label = (TextView) convertView.findViewById(R.id.label);
                holder.spinner = (Spinner) convertView.findViewById(R.id.select_job);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            try
            {
                ArrayAdapter<String> spinnerAdapter;
                switch (position)
                {
                    case 0:
                        holder.label.setText(JOB_LABEL);

                        spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, deadlines);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        holder.spinner.setAdapter(spinnerAdapter);
                        break;
                    case 4:
                        holder.label.setText(PAUSE_TIME_LABEL);

                        spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pauses);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        holder.spinner.setAdapter(spinnerAdapter);
                        break;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if (convertView == null)
            {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.shift_detail, parent, false);

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
                SimpleDateFormat formatter;
                switch (position)
                {
                    case 1:
                        holder.label.setText(START_DAY_LABEL);
                        formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
                        holder.value.setText(formatter.format(new Date(shift.getStart())));
                        break;
                    case 2:
                        holder.label.setText(START_TIME_LABEL);
                        formatter = new SimpleDateFormat("HH:mm a");
                        holder.value.setText(formatter.format(new Date(shift.getStart())));
                        break;
                    case 3:
                        holder.label.setText(END_TIME_LABEL);
                        formatter = new SimpleDateFormat("HH:mm a");
                        holder.value.setText(formatter.format(new Date(shift.getEnd())));
                        break;
                    default:
                        throw new Exception();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    private class ViewHolder
    {
        TextView label;
        TextView value;
        Spinner spinner;
    }

}
