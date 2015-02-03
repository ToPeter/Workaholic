package eu.alterway.workaholic;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import eu.alterway.workaholic.fragment.AboutFragment;
import eu.alterway.workaholic.fragment.AccountFragment;
import eu.alterway.workaholic.fragment.HomeFragment;
import eu.alterway.workaholic.fragment.JobsFragment;
import eu.alterway.workaholic.fragment.ShiftsFragment;
import eu.alterway.workaholic.fragment.SummariesFragment;
import eu.alterway.workaholic.util.NavigationDrawerFragment;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new SummariesFragment();
                break;
            case 2:
                fragment = new ShiftsFragment();
                break;
            case 3:
                fragment = new JobsFragment();
                break;
            case 4:
                fragment = new AccountFragment();
                break;
            case 5:
                fragment = new AboutFragment();
                break;
            default:
                break;
        }

        if (fragment != null)
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else
        {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
