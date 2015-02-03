package eu.alterway.workaholic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{
	private long id;
	private String firstName,lastName,password,email;
	
	public User() {}
	
	public User(String email, String firstName, String lastName, String password)
	{
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	
	public User(long id, String email, String firstName, String lastName, String password)
	{
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	
	public User(Parcel in)
	{
		id = in.readLong();
		email = in.readString();
		this.firstName = in.readString();
		this.lastName = in.readString();
		password = in.readString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", password=" + password + ", email=" + email + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeLong(id);
		out.writeString(email);
		out.writeString(firstName);
		out.writeString(lastName);
		out.writeString(password);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() 
			{
				@Override
				public User createFromParcel(Parcel in) 
				{
					return new User(in);
				}

				@Override
				public User[] newArray(int size) 
				{
					return new User[size];
				}
				
			};

}
