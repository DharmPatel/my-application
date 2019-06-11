package com.example.google.csmia_temp.Helpdesk.Model;

public class OptionData {

    int id;
    String FieldOption;
    boolean checked;

    public OptionData(int id, String fieldOption, boolean checked) {
        this.id = id;
        FieldOption = fieldOption;
        this.checked = checked;
    }

    public OptionData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFieldOption() {
        return FieldOption;
    }

    public void setFieldOption(String fieldOption) {
        FieldOption = fieldOption;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
