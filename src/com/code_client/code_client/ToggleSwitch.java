package code_client.code_client;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ToggleSwitch extends HBox {
    	
    	/* Thanks to TheItachiUchiha on GitHub */
    	/* This was modified from one of his gists */
    	
    	public boolean value = false;
    	
    	private String msgTrue, msgFalse;
    	
    	private final Label label = new Label();
    	private final Button button = new Button();
    	
    	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
    	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
    	
    	public void setSize(double d, double e) {
    		this.setHeight(d);
    		this.setWidth(e);
    	}
    	
    	private void init() {
    		
    		label.setText("64 bit");
    		label.setFont(Font.font(label.getFont().getName(), FontWeight.EXTRA_BOLD, label.getFont().getSize()));
    		
    		getChildren().addAll(label, button);	
    		button.setOnAction((e) -> {
    			switchedOn.set(!switchedOn.get());
    		});
    		label.setOnMouseClicked((e) -> {
    			switchedOn.set(!switchedOn.get());
    			System.out.println(switchedOn.toString());
    		});
    		setStyle();
    		bindProperties();
    	}
    	
    	private void setStyle() {
    		//Default Width
    		setWidth(100);
    		label.setAlignment(Pos.CENTER);
    		setStyle("-fx-background-color: blue; -fx-text-fill:black; -fx-background-radius: 4;");
    		setAlignment(Pos.CENTER_LEFT);
    	}
    	
    	private void bindProperties() {
    		label.prefWidthProperty().bind(widthProperty().divide(2));
    		label.prefHeightProperty().bind(heightProperty());
    		button.prefWidthProperty().bind(widthProperty().divide(2));
    		button.prefHeightProperty().bind(heightProperty());
    	}
    	
    	public ToggleSwitch(String msgFalse, String msgTrue) {
    		this.msgFalse = msgFalse;
    		this.msgTrue = msgTrue;
    		init();
    		switchedOn.addListener((a,b,c) -> {
    			if (c) {
                    		label.setText(msgFalse);
                    		this.value = false;
                    		setStyle("-fx-background-color: orange; -fx-background-radius: 4;");
                    		label.toFront();
                		}
                		else {
                			label.setText(msgTrue);
                			this.value = true;
                			setStyle("-fx-background-color: blue; -fx-background-radius: 4;");
                    		button.toFront();
                		}
    		});
    	}
    }