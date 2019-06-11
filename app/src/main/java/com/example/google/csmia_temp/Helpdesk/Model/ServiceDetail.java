package com.example.google.csmia_temp.Helpdesk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceDetail {
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Subcategory")
    @Expose
    private String subcategory;
    @SerializedName("Issue")
    @Expose
    private String issue;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public ServiceDetail(String category, String subcategory, String issue) {
        this.category = category;
        this.subcategory = subcategory;
        this.issue = issue;
    }

    public ServiceDetail() {
    }

    @Override
    public String toString() {
        return "ServiceDetail{" +
                "category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", issue='" + issue + '\'' +
                '}';
    }
}
