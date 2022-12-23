package com.application.base.layout.explorer;

import com.application.base.actions.Action;
import javafx.scene.image.ImageView;

public record ListViewContent(int contentID, String text, ImageView icon, Action action) {}
