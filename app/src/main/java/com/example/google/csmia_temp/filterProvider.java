package com.example.google.csmia_temp;

public class filterProvider {
    private String name;
    private boolean selected;

    public filterProvider(String name) { this.name = name; }

    public String getName() { return name; }

    public boolean isSelected() { return selected; }

    public void setSelected(boolean selected) { this.selected = selected; }
}
