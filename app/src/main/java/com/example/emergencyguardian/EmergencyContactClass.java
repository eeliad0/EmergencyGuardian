package com.example.emergencyguardian;

public class EmergencyContactClass {
    private String name;
    private String phone;
    private String relationship;
    private String id;


    public EmergencyContactClass() {
        // Default constructor required for calls to DataSnapshot.getValue(EmergencyContact.class)
    }

    public EmergencyContactClass(String name, String phone, String relationship) {
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}