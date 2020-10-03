package com.encycode.onetimecontact.entity;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "contact_table")
@Keep
public class Contact implements Serializable {
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

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
