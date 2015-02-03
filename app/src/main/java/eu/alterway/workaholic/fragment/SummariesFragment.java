package eu.alterway.workaholic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.alterway.workaholic.R;

public class SummariesFragment extends Fragment {
    
    public SummariesFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_summaries, container, false);
          
        return rootView;
    }
}
