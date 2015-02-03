package eu.alterway.workaholic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.alterway.workaholic.R;

public class AboutFragment extends Fragment 
{
	private TextView workaholicText;
	private TextView alterwayText;
	
    public AboutFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        
        workaholicText = (TextView) rootView.findViewById(R.id.about_workaholic_text);
        alterwayText = (TextView) rootView.findViewById(R.id.about_alterway_text);
        
        Spanned workaholicTextSpan = Html.fromHtml(getString(R.string.about_workaholic_text));
        workaholicText.setText(workaholicTextSpan);
        
        Spanned alterwayTextSpan = Html.fromHtml(getString(R.string.about_alterway_text));
        alterwayText.setText(alterwayTextSpan);
          
        return rootView;
    }
}
