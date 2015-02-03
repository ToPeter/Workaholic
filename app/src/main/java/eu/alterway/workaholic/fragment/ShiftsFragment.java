package eu.alterway.workaholic.fragment;

import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.alterway.workaholic.R;
import eu.alterway.workaholic.adapter.ShiftDetailsAdapter;

public class ShiftsFragment
        extends Fragment implements WeekView.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener
{
    private ListView shiftsDetails;
    private WeekView weekView;
    private FloatingActionButton menuButton;
    private FloatingActionButton addButton;
    private FloatingActionButton calendarButton;
    private FloatingActionButton todayButton;
    private ViewGroup container;
    private boolean isMenuOpened;

    private ViewSwitcher switcher;
    private ShiftDetailsAdapter shiftDetailsAdapter;

    public ShiftsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shifts, container, false);

        this.container = container;

        switcher = (ViewSwitcher) rootView.findViewById(R.id.shift_switcher);
        switcher.setInAnimation(getActivity(),R.anim.slide_in_left);
        switcher.setOutAnimation(getActivity(),R.anim.slide_out_left);

        shiftsDetails = (ListView) rootView.findViewById(R.id.shift_details);

        // Get a reference for the week view in the layout.
        weekView = (WeekView) rootView.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        weekView.setOnEventClickListener(ShiftsFragment.this);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        weekView.setMonthChangeListener(ShiftsFragment.this);


        // Set long press listener for events.
        weekView.setEventLongPressListener(ShiftsFragment.this);

//        menuButton = new FloatingActionButton.Builder(getActivity())
//                .withDrawable(getResources().getDrawable(R.drawable.white_toggle))
//                .withButtonColor(getResources().getColor(R.color.dark_workaholic))
//                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
//                .withMargins(0, 0, 16, 16)
//                .create();

        isMenuOpened = false;

//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               openMenu();
//            }
//        });

        return rootView;
    }

    private void openMenu()
    {
        if (isMenuOpened)
        {
//            addButton.hideFloatingActionButton();
//            calendarButton.hideFloatingActionButton();
//            todayButton.hideFloatingActionButton();

            isMenuOpened = false;
        }
        else
        {
            if (addButton == null)
            {
//                addButton = new FloatingActionButton.Builder(getActivity())
//                        .withDrawable(getResources().getDrawable(R.drawable.white_plus))
//                        .withButtonColor(getResources().getColor(R.color.dark_workaholic))
//                        .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
//                        .withMargins(0, 0, 16, 64)
//                        .create();
//                addButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shiftDetailsAdapter = new ShiftDetailsAdapter(getActivity(),new Shift());
//                        shiftsDetails.setAdapter(shiftDetailsAdapter);
//                        if (switcher.getDisplayedChild() == 0) {
//                            switcher.showNext();
//                        }
//                        else
//                        {
//                            switcher.showPrevious();
//                        }
//                    }
//                });
            }
            else
            {
//                addButton.showFloatingActionButton();
//
//                addButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shiftDetailsAdapter = new ShiftDetailsAdapter(getActivity(),new Shift());
//                        shiftsDetails.setAdapter(shiftDetailsAdapter);
//                        if (switcher.getDisplayedChild() == 0) {
//                            switcher.showNext();
//                        }
//                        else
//                        {
//                            switcher.showPrevious();
//                        }
//                    }
//                });
            }

//            if (calendarButton == null)
//            {
//                calendarButton = new FloatingActionButton.Builder(getActivity())
//                        .withDrawable(getResources().getDrawable(R.drawable.white_calendar))
//                        .withButtonColor(getResources().getColor(R.color.dark_workaholic))
//                        .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
//                        .withMargins(0, 0, 16, 112)
//                        .create();
//            }
//            else
//            {
//                calendarButton.showFloatingActionButton();
//            }
//
//            if (todayButton == null)
//            {
//                todayButton = new FloatingActionButton.Builder(getActivity())
//                        .withDrawable(getResources().getDrawable(R.drawable.white_today))
//                        .withButtonColor(getResources().getColor(R.color.dark_workaholic))
//                        .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
//                        .withMargins(0, 0, 16, 160)
//                        .create();
//            }
//            else
//            {
//                todayButton.showFloatingActionButton();
//            }

            isMenuOpened = true;
        }
    }

    @Override
    public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF)
    {
//        addButton.hideFloatingActionButton();
//        calendarButton.hideFloatingActionButton();
        weekView.computeScroll();
    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF)
    {
//        addButton.showFloatingActionButton();
//        calendarButton.hideFloatingActionButton();
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth)
    {

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
            Calendar startTime = Calendar.getInstance();


            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, 1);
            endTime.set(Calendar.MONTH, newMonth-1);
            WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 30);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 4);
            endTime.set(Calendar.MINUTE, 30);
            endTime.set(Calendar.MONTH, newMonth-1);
            event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_02));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 4);
            startTime.set(Calendar.MINUTE, 20);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 5);
            endTime.set(Calendar.MINUTE, 0);
            event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_03));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 5);
            startTime.set(Calendar.MINUTE, 30);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 2);
            endTime.set(Calendar.MONTH, newMonth-1);
            event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_02));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 5);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            startTime.add(Calendar.DATE, 1);
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 3);
            endTime.set(Calendar.MONTH, newMonth - 1);
            event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_03));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_MONTH, 15);
            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 3);
            event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_04));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_MONTH, 1);
            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 3);
            event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);
            startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
            startTime.set(Calendar.HOUR_OF_DAY, 15);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 3);
            event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_02));
            events.add(event);


        return events;
    }



    private class LoadShifts extends AsyncTask<View, Boolean, Integer>
    {
        @Override
        protected Integer doInBackground(View... roots)
        {



            return null;
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
//            try {
//                FloatingActionButton fabButton = new FloatingActionButton.Builder(getActivity())
//                        .withDrawable(getResources().getDrawable(R.drawable.white_plus))
//                        .withButtonColor(getResources().getColor(R.color.dark_workaholic))
//                        .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
//                        .withMargins(0, 0, 16, 16)
//                        .create();
//            } catch (Resources.NotFoundException e) {
//                e.printStackTrace();
//            }
        }
    }
}

