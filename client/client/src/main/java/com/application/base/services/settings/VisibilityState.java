package com.application.base.services.settings;

public enum VisibilityState {
    VISIBLE,
    NOT_VISIBLE,
    MINIMIZED;

    @Override
    public String toString() {
        switch (this) {
            case VISIBLE -> {
                return "VISIBLE";
            }
            case NOT_VISIBLE -> {
                return "NOT_VISIBLE";
            }
            case MINIMIZED -> {
                return "MINIMIZED";
            }
        }
        return "";
    }
}
