package com.example.google.csmia_temp.Helpdesk.Model;

public class catSubCat {
    private String OptionValue;
    private String issue;
    public String getOptionValue() {
        return OptionValue;
    }

    public void setOptionValue(String optionValue) {
        OptionValue = optionValue;
    }

    public catSubCat(String optionValue, String issue ) {
        OptionValue = optionValue;
        this.issue=issue;
    }

    @Override
    public String toString() {
        return "catSubCat{" +
                "OptionValue='" + OptionValue + '\'' +
                '}';
    }
}
