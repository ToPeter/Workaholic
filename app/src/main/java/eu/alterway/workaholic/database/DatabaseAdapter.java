package eu.alterway.workaholic.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import eu.alterway.workaholic.model.Job;
import eu.alterway.workaholic.model.Shift;
import eu.alterway.workaholic.model.User;

public class DatabaseAdapter 
{	
	// Database Info
    public static final int DATABASE_VERSION = 25;
    public static final String DATABASE_NAME = "workaholicManager";
	
	// Table Names
	private static final String TABLE_USER = "user";
	private static final String TABLE_JOB = "jobs";
	private static final String TABLE_SHIFT = "shifts";
	
	// Common column names
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	
	// USER Table - column names
	public static final String USER_FIRST_NAME = "first_name";
	public static final String USER_EMAIL = "email";
	public static final String USER_PASSWORD = "password";
	
	// JOB Table - column names
	public static final String JOB_WORKPLACE = "workplace";
	public static final String JOB_POSITION = "position";
	public static final String JOB_DESCRIPTION = "description";
	public static final String JOB_SALARY = "salary";
	public static final String JOB_TAX = "tax";
	public static final String JOB_DEADLINE = "deadline";
	public static final String JOB_ACTIVE = "active";
	
	// SHIFT Table - column names
	public static final String SHIFT_START = "start";
	public static final String SHIFT_END = "end";
	public static final String SHIFT_PAUSE = "pause";
	public static final String KEY_JOB_ID = "job_id";
	
	// Table CREATE Statements
	private static final String CREATE_TABLE_USER = "CREATE TABLE "+TABLE_USER+
													"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
													   +USER_EMAIL+" TEXT,"
													   +USER_FIRST_NAME+" TEXT,"
													   +KEY_NAME+" TEXT,"
													   +USER_PASSWORD+" TEXT)";
	
	private static final String CREATE_TABLE_JOB = "CREATE TABLE "+TABLE_JOB+
												   "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
												   	  +JOB_WORKPLACE+" TEXT,"
												   	  +JOB_POSITION+" TEXT,"
												   	  +JOB_SALARY+" REAL,"
												   	  +JOB_TAX+" REAL,"
												   	  +JOB_DEADLINE+" INTEGER,"
												   	  +JOB_ACTIVE+" INTEGER,"
												   	  +JOB_DESCRIPTION+" TEXT)";
	
	private static final String CREATE_TABLE_SHIFT = "CREATE TABLE "+TABLE_SHIFT+
												     "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
												     	+KEY_JOB_ID+" INTEGER,"
												     	+SHIFT_START+" INTEGER,"
												     	+SHIFT_END+" INTEGER,"
												     	+SHIFT_PAUSE+" INTEGER)";
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public DatabaseAdapter(Context context)
	{
		this.dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException 
	{
	    database = dbHelper.getWritableDatabase();
	}

    public void close() 
    {
        if (dbHelper != null)
        {
            dbHelper.close();
        }
    }
	
	public long createUser(User user)
	{
		try
		{
			ContentValues values = new ContentValues();
			
			values.put(USER_EMAIL, user.getEmail());
			values.put(USER_FIRST_NAME, user.getFirstName());
			values.put(KEY_NAME, user.getLastName());
			values.put(USER_PASSWORD, user.getPassword());
			
			long user_id = database.insert(TABLE_USER, null, values);
			
			return user_id;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public long createJob(Job job)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(JOB_WORKPLACE, job.getWorkplace());
			values.put(JOB_POSITION, job.getPosition());
			values.put(JOB_SALARY, job.getSalary());
			values.put(JOB_TAX, job.getTax());
			values.put(JOB_DEADLINE, job.getDeadline());
			if (job.IsActive())
			{
				values.put(JOB_ACTIVE, 1);
			}
			else
			{
				values.put(JOB_ACTIVE, 0);
			}
			values.put(JOB_DESCRIPTION, job.getDescription());
			
			long job_id = database.insert(TABLE_JOB, null, values);
			
			return job_id;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public long createShift(Shift shift)
	{
		try
		{
			ContentValues values = new ContentValues();
			
			values.put(KEY_JOB_ID, shift.getJobId());
			values.put(SHIFT_START, shift.getStart());
			values.put(SHIFT_END, shift.getEnd());
			values.put(SHIFT_PAUSE, shift.getPause());
			
			long shift_id = database.insert(TABLE_SHIFT, null, values);
			
			shift.setId(shift_id);
			
			return shift_id;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public long updateUser(User user)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(USER_EMAIL, user.getEmail());
			values.put(USER_FIRST_NAME, user.getFirstName());
			values.put(KEY_NAME, user.getLastName());
			values.put(USER_PASSWORD, user.getPassword());
			
			long user_id = database.update(TABLE_USER, values, KEY_ID+" = ?", new String[] {String.valueOf(user.getId())});
			
			return user_id;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public long updateJob(Job job)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(JOB_WORKPLACE, job.getWorkplace());
			values.put(JOB_POSITION, job.getPosition());
			values.put(JOB_SALARY, job.getSalary());
			values.put(JOB_TAX, job.getTax());
			values.put(JOB_DEADLINE, job.getDeadline());
			if (job.IsActive())
			{
				values.put(JOB_ACTIVE, 1);
			}
			else
			{
				values.put(JOB_ACTIVE, 0);
			}
			
			values.put(JOB_DESCRIPTION, job.getDescription());
			
			long job_id = database.update(TABLE_JOB, values, KEY_ID+" = ?", new String[] {String.valueOf(job.getId())});
			
			return job_id;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public long updateShift(Shift shift)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(KEY_JOB_ID, shift.getJobId());
			values.put(SHIFT_START, shift.getStart());
			values.put(SHIFT_END, shift.getEnd());
			values.put(SHIFT_PAUSE, shift.getPause());
			
			long shift_id = database.update(TABLE_SHIFT, values, KEY_ID+" = ?", new String[] {String.valueOf(shift.getId())});
			
			return shift_id;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public User getUser()
	{
		User user = null;
		
		try
		{
			Cursor cursor = database.query(true, TABLE_USER, null, KEY_ID+" = ?", new String[]{String.valueOf(1)}, null, null, null, null);
			if (cursor.moveToFirst())
			{
				do
				{
					user = new User();
					user.setId(cursor.getLong(0));
					user.setEmail(cursor.getString(1));
					user.setFirstName(cursor.getString(2));
					user.setLastName(cursor.getString(3));
					user.setPassword(cursor.getString(4));
				} while(cursor.moveToNext());
				
				cursor.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return user;
	}
	
	public Job getJob(long job_id)
	{
		Job job = null;
		
		try
		{
			Cursor cursor = database.query(true, TABLE_JOB, null, KEY_ID+" = ?", new String[]{String.valueOf(job_id)}, null, null, null, null);
			if (cursor.moveToFirst())
			{
				do
				{
					job = new Job();
					job.setId(cursor.getLong(0));
					job.setWorkplace(cursor.getString(1));
					job.setPosition(cursor.getString(2));
					job.setSalary(cursor.getDouble(3));
					job.setTax(cursor.getDouble(4));
					job.setDeadline(cursor.getInt(5));
					job.setIsActive(cursor.getInt(6) == 1);
					job.setDescription(cursor.getString(7));
				} while(cursor.moveToNext());
				
				cursor.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return job;
	}
	
	public Shift getShift(long shift_id, long job_id)
	{
		Shift shift = null;
		
		try
		{
			Cursor cursor = database.query(true, TABLE_SHIFT, null, KEY_ID+" = ? AND "+KEY_JOB_ID+" = ?", new String[]{String.valueOf(shift_id),String.valueOf(job_id)}, null, null, null, null);
			if (cursor.moveToFirst())
			{
				do
				{
					shift = new Shift();
					shift.setId(cursor.getLong(0));
					shift.setJobId(cursor.getLong(1));
					shift.setStart(cursor.getLong(2));
					shift.setEnd(cursor.getLong(3));
					shift.setPause(cursor.getLong(4));
				} while(cursor.moveToNext());
				
				cursor.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return shift;
	}
	
	public boolean deleteUser()
	{	
		try
		{
			deleteAllJobsAndShifts();
			
			Log.d("Database Message", "Delete user");
            int result = database.delete(TABLE_USER, KEY_ID + "=?", new String[]{String.valueOf(1)});
            Log.d("Database Message", "result = " + result);
            
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteJob(Job job)
	{
		try
		{	
			Log.d("Database Message", "Delete job");
			int result = database.delete(TABLE_JOB, KEY_ID+" = ?", new String[]{String.valueOf(job.getId())});
			Log.d("Database Message", "result = " + result);
			
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteShift(Shift shift)
	{
		try
		{
			Log.d("Database Message", "Delete shift");
			int result = database.delete(TABLE_SHIFT, KEY_ID+" = ?", new String[]{String.valueOf(shift.getId())});
			Log.d("Database Message", "result = " + result);
			
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public long getNumberOfJobs()
	{
		try
		{
			return DatabaseUtils.queryNumEntries(database, TABLE_JOB);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public long getNumberOfActiveJobs()
	{
		try
		{
			return DatabaseUtils.queryNumEntries(database, TABLE_JOB, JOB_ACTIVE+" = ?", new String[]{String.valueOf(1)});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<Job> getAllJobs()
	{
		try
		{
			List<Job> jobs = new ArrayList<Job>();
			
			Cursor cursor = database.query(true, TABLE_JOB, null, null, null, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					Job job = new Job();
					job.setId(cursor.getLong(0));
					job.setWorkplace(cursor.getString(1));
					job.setPosition(cursor.getString(2));
					job.setSalary(cursor.getDouble(3));
					job.setTax(cursor.getDouble(4));
					job.setDeadline(cursor.getInt(5));
					job.setIsActive(cursor.getInt(6) == 1);
					job.setDescription(cursor.getString(7));
					
					jobs.add(job);
				} while(cursor.moveToNext());
				
				cursor.close();
			}
			
			return jobs;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Job> getAllActiveJobs()
	{
		try
		{
			List<Job> activeJobs = new ArrayList<Job>();
			
			Cursor cursor = database.query(true, TABLE_JOB, null, JOB_ACTIVE+" = ?", new String[]{String.valueOf(1)}, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					Job job = new Job();
					job.setId(cursor.getLong(0));
					job.setWorkplace(cursor.getString(1));
					job.setPosition(cursor.getString(2));
					job.setSalary(cursor.getDouble(3));
					job.setTax(cursor.getDouble(4));
					job.setDeadline(cursor.getInt(5));
					job.setIsActive(cursor.getInt(6) == 1);
					job.setDescription(cursor.getString(7));
					
					activeJobs.add(job);
				} while(cursor.moveToNext());
				
				cursor.close();
			}
			
			return activeJobs;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Shift> getAllShifts()
	{
		try
		{
			List<Shift> shifts = new ArrayList<Shift>();
			
			Cursor cursor = database.query(true, TABLE_SHIFT, null, null, null, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					Shift shift = new Shift();
					shift.setId(cursor.getLong(0));
					shift.setJobId(cursor.getLong(1));
					shift.setStart(cursor.getLong(2));
					shift.setEnd(cursor.getLong(3));
					shift.setPause(cursor.getLong(4));
					
					shifts.add(shift);
				} while(cursor.moveToNext());
				
				cursor.close();
			}
			
			return shifts;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Shift> getShifts(Job job)
	{
		try
		{
			List<Shift> shifts = new ArrayList<Shift>();
			
			Cursor cursor = database.query(true, TABLE_SHIFT, null, KEY_JOB_ID+" = ?", new String[]{String.valueOf(job.getId())}, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					Shift shift = new Shift();
					shift.setId(cursor.getLong(0));
					shift.setJobId(cursor.getLong(1));
					shift.setStart(cursor.getLong(2));
					shift.setEnd(cursor.getLong(3));
					shift.setPause(cursor.getLong(4));
					
					shifts.add(shift);
				} while(cursor.moveToNext());
				
				cursor.close();
			}
		
			return shifts;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Shift> getShiftsOnDate(long date)
	{
		try
		{
			List<Shift> shifts = new ArrayList<Shift>();
			
			Cursor cursor = database.query(true, TABLE_SHIFT, null, null, null, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					Shift shift = new Shift();
					shift.setId(cursor.getLong(0));
					shift.setJobId(cursor.getLong(1));
					shift.setStart(cursor.getLong(2));
					shift.setEnd(cursor.getLong(3));
					shift.setPause(cursor.getLong(4));
					
					if(isOnSelectedDate(shift.getStart(), date))
					{
						shifts.add(shift);
					}
				} while(cursor.moveToNext());
				
				cursor.close();
			}
			
			return shifts;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean isOnSelectedDate(long shiftsDate, long selectedDate)
	{
		try
		{
			Calendar shiftCalendar = Calendar.getInstance();
			shiftCalendar.setTimeInMillis(shiftsDate);
			
			Calendar selectedCalendar = Calendar.getInstance();
			selectedCalendar.setTimeInMillis(selectedDate);
			
			boolean sameDay = shiftCalendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
	                  shiftCalendar.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR);
			
			return sameDay;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Shift> getShiftsForMonthForJob(Job selectedJob, int month, int year)
	{
		try
		{
			List<Shift> shiftsForJob = new ArrayList<Shift>();
			
			Cursor cursor = database.query(true, TABLE_SHIFT, null, KEY_JOB_ID+" = ?", new String[]{String.valueOf(selectedJob.getId())}, null, null, null, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					Shift shift = new Shift();
					shift.setId(cursor.getLong(0));
					shift.setJobId(cursor.getLong(1));
					shift.setStart(cursor.getLong(2));
					shift.setEnd(cursor.getLong(3));
					shift.setPause(cursor.getLong(4));
					
					if(isBeforeDeadline(shift, month, year, selectedJob.getDeadline()))
					{
						shiftsForJob.add(shift);
					}
				} while(cursor.moveToNext());
				
				cursor.close();
			}
			
			return shiftsForJob;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean isBeforeDeadline(Shift shift, int month,int year, int deadline)
	{
		try
		{
			Calendar shiftCalendar = Calendar.getInstance();
			shiftCalendar.setTimeInMillis(shift.getStart());
			
			if (deadline<=0)
			{
				int endOfMonth = shiftCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				System.out.println("ACTUAL MAXIMUM: "+ endOfMonth);
				deadline = endOfMonth + deadline;
			}
			
			if (shiftCalendar.get(Calendar.MONTH) == month && shiftCalendar.get(Calendar.YEAR) == year)
			{
				// before
				return shiftCalendar.get(Calendar.DAY_OF_MONTH) <= deadline;
			}
			else if (shiftCalendar.get(Calendar.MONTH) == month-1 && shiftCalendar.get(Calendar.YEAR) == year)
			{
				// after
				return shiftCalendar.get(Calendar.DAY_OF_MONTH) > deadline;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void deleteAllJobsAndShifts() throws SQLException
	{
		try
		{
			database.delete(TABLE_JOB, null, null);
			database.delete(TABLE_SHIFT, null, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new SQLException();
		}
	}
	
	public void deleteAllShiftsInJob(Job job) throws SQLException
	{
		try
		{
			database.delete(TABLE_SHIFT, KEY_JOB_ID+" = ?", new String[]{String.valueOf(job.getId())});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new SQLException();
		}
	}
	
	/*
	 * Calculating HELPERS:
	 */
	
	public double getAllHoursForMonth(int month, int year)
	{
		try
		{
			double hours = 0;
			
			for (Shift shift : getAllShiftsForMonth(month, year))
			{
				if ((shift.getEnd() - shift.getStart()) >= shift.getPause() || (shift.getStart() - shift.getEnd()) >= shift.getPause())
				{
					if (shift.getEnd() < shift.getStart())
					{
						hours += ((86400000 - shift.getStart()) + shift.getEnd()) -shift.getPause();
					}
					else
					{
						hours += (shift.getEnd() - shift.getStart() - shift.getPause());
					}
				}
			}
			
			return hours/3600000;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public double getHoursForMonthForJob(Job selectedJob, int month, int year)
	{
		try
		{
			double hours = 0;
			
			for (Shift shift : getShiftsForMonthForJob(selectedJob, month, year))
			{
				if ((shift.getEnd() - shift.getStart()) >= shift.getPause() || (shift.getStart() - shift.getEnd()) >= shift.getPause())
				{
					if (shift.getEnd() < shift.getStart())
					{
						hours += ((86400000 - shift.getStart()) + shift.getEnd()) -shift.getPause();
					}
					else
					{
						hours += (shift.getEnd() - shift.getStart() - shift.getPause());
					}
				}
			}
			
			return hours/3600000;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<Shift> getAllShiftsForMonth(int month, int year)
	{
		try
		{
			List<Shift> shifts = new ArrayList<Shift>();

			for (Job job : getAllJobs())
			{
				shifts.addAll(getShiftsForMonthForJob(job, month, year));
			}
			return shifts;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public double countPaycheckBefore(Job job, double hours)
	{
		try
		{
			return hours*job.getSalary();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public double countPaycheckAfter(Job job, double hours)
	{
		try
		{
			return hours*job.getSalary()*((100-job.getTax())/100);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(CREATE_TABLE_USER);
			db.execSQL(CREATE_TABLE_JOB);
			db.execSQL(CREATE_TABLE_SHIFT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_JOB);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_SHIFT);
			
			onCreate(db);
		}
		
	}
	
}

