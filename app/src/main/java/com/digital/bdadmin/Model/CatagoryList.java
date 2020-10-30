package com.digital.bdadmin.Model;

public class CatagoryList {

    private String CategoryName, CategoryTitle;

    public CatagoryList() {
    }

    public CatagoryList(String categoryName, String categoryTitle) {
        CategoryName = categoryName;
        CategoryTitle = categoryTitle;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        CategoryTitle = categoryTitle;
    }
}
