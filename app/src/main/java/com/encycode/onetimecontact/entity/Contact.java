package com.encycode.onetimecontact.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "f_name")
    private String firstName;

    @ColumnInfo(name = "l_name")
    private String lastName;

    @ColumnInfo(name = "company_name")
    private String companyName;

    @ColumnInfo(name = "job_title")
    private String jobTitle;

    @ColumnInfo(name = "email")
    private String emailId;

    @ColumnInfo(name = "mobile_number")
    private String mobileNumber;

    public Contact(String firstName, String lastName, String companyName, String jobTitle, String emailId, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
