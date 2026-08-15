package com.maryam.womensafetyapp.data.model;

/**
 * Maps to: /emergencyContacts/{womanId}/{contactId}
 */
public class EmergencyContact {

    private String contactId;
    private String name;
    private String phone;
    private String relationship;

    public EmergencyContact() {
        // Required empty constructor for Firebase
    }

    public EmergencyContact(String contactId, String name, String phone, String relationship) {
        this.contactId = contactId;
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
    }

    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
}