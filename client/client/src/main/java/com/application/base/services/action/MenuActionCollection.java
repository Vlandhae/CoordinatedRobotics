package com.application.base.services.action;

import com.application.base.actions.Action;

import java.util.ArrayList;

public class MenuActionCollection extends ArrayList<Action> {

    private String menuTitle;

    public MenuActionCollection() {
        this.menuTitle = "";
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuTitle() {
        return menuTitle;
    }
}
