package com.accurajackets.patientidentification.model;

import com.accurajackets.patientidentification.helper.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by kushal on 8/23/14.
 */
public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private int cellNumber;
    private String dateOfBirth;
    private int age;
    private String address;
    private String email;
    private String sex;
    private String emergencyContact;
    private int height;
    private int weight;
    private String eyeColor;
    private int driverLicenseNumber;

    public Patient(){

    }

    public Patient(int id, String firstName, String lastName, int cellNumber, String dateOfBirth, int age, String address, String email, String sex, String emergencyContact, int height, int weight, String eyeColor, int driverLicenseNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellNumber = cellNumber;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.address = address;
        this.email = email;
        this.sex = sex;
        this.emergencyContact = emergencyContact;
        this.height = height;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public Patient getPatient(final String URI){
        JSONObject result = null;
        try {
            result = HttpHelper.httpGet(URI);
        } catch (Exception e) {
            return null;
        }
        try {
            id = result.getInt("id");
            //firstName = result.getString("first_name");
            lastName = result.getString("last_name");
            //cellNumber = result.getInt("cell_no");
            //dateOfBirth = result.getString("dob");
            age = result.getInt("age");
            //address = result.getString("address");
            //email = result.getString("email");
            sex = result.getString("sex");
            //emergencyContact = result.getString("emergency_contact");
            //height = result.getInt("height");
            //weight = result.getInt("weight");
            //eyeColor = result.getString("eye_color");
            //driverLicenseNumber = result.getInt("driver_license_no");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public int getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(int driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }
}
