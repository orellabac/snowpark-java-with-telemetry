package org.example.model;


public class Classification {
    private String name;
    private Long   key;
    private String classification;

    public Classification() {}

    public Classification(String name, Long key, String classification) {
        this.name = name;
        this.key = key;
        this.classification = classification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Classification{name='" + name + "', key='" + key + "', classification='" + classification + "'}";
    }
}

