package com.example.kma_application.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Prescription implements Serializable {
    private String _id;
    private String phone;
    private String name;
    private String _class;
    private String startDate;
    private ArrayList<Medicine> medicines;
    private String __v;

    public Prescription(String phone, String name, String _class, String startDate, ArrayList<Medicine> medicines) {
        this.phone = phone;
        this.name = name;
        this._class = _class;
        this.startDate = startDate;
        this.medicines = medicines;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }

    public static class Medicine implements Serializable{
        private String _id;
        private String childName;
        private String medicineName;
        private String dosage;
        private String time;

        public Medicine(String childName, String medicineName, String dosage, String time) {
            this.childName = childName;
            this.medicineName = medicineName;
            this.dosage = dosage;
            this.time = time;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String toJsonString() {
            return "{\"childName\":\"" + childName + "\","
                    +"\"medicineName\":\"" + medicineName +"\","
                    +"\"dosage\":\"" + dosage +"\","
                    +"\"time\":\"" + time +"\"},";
        }
        public String toStringForTeacherUsed() {
            return childName +"\n"+
                    "\t\t"+ medicineName +".  Li???u d??ng: "+ dosage;
        }
        @Override
        public String toString() {
            return time +"   "+ medicineName +".  Li???u d??ng: "+ dosage;
        }
    }
}
