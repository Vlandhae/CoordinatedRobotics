package com.application.base.actions;

import javafx.scene.input.KeyCode;

public record ShortCutData(boolean altKey, boolean controlKey, boolean shiftKey, KeyCode character) {}
