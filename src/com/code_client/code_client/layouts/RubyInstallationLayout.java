package com.code_client.code_client.layouts;

import com.code_client.code_client.installers.RubyInstaller;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RubyInstallationLayout {
	static VBox layout = new VBox();
	
	public static VBox display() {
		Label label = new Label("Let's install ruby.");
		
		layout.getChildren().addAll(label, RubyInstaller.init(layout));
		layout.setAlignment(Pos.CENTER);
		
		return layout;
	}
	
}
