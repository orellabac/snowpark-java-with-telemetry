package org.example.model;


public class Classification {
    private String name;
    private String classification;

    public Classification() {}

    public Classification(String name, String classification) {
        this.name = name;
        this.classification = classification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Classification{name='" + name + "', classification='" + classification + "'}";
    }
}

