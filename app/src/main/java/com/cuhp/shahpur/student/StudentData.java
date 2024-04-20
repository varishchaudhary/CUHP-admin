package com.cuhp.shahpur.student;

public class StudentData {
    private String name, registrationNumber, fatherName, email, image, key;

    public StudentData() {
    }

    public StudentData(String name, String registrationNumber, String fatherName, String email, String image, String key) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.fatherName = fatherName;
        this.email = email;
        this.image = image;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
