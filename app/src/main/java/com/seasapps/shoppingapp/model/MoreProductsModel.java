package com.seasapps.shoppingapp.model;


public class MoreProductsModel {
    int Icon;
    String Name;

    public MoreProductsModel(int icon, String name) {
        Icon = icon;
        Name = name;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String menu) {
        Name = menu;
    }
}
