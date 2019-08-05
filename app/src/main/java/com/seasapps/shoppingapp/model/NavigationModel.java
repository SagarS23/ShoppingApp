package com.seasapps.shoppingapp.model;


public class NavigationModel {
    int Icon;
    String Menu;

    public NavigationModel(int icon, String menu) {
        Icon = icon;
        Menu = menu;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }
}
