package com.application.base.layout;

import com.application.base.actions.Action;
import javafx.scene.control.MenuItem;

public class CustomMenuItem extends MenuItem {

    private Action action;

    public CustomMenuItem(String title) {
        super(title);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
