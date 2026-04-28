package com.tombranfield.scaleconverter;

import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class ModelRailwayScaleConverter extends Application 
{
    ComboBox<String> scaleComboBox;
    ComboBox<Integer> precisionComboBox;
    Label inputInstructionLabel;
    ComboBox<String> inputUnitBox;
    TextField inputValueField;
    RadioButton realToModelButton;
    RadioButton modelToRealButton;
    Label outputInstructionLabel;
    ComboBox<String> outputUnitBox;
    Button calculateButton;
    Label resultsLabel;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage)
    {
        // The height and width of the scene
        final double WIDTH = 400;
        final double HEIGHT = 450;

        // Create header stating purpose of the program and set its font, size, and colour
        Label headerLabel = new Label("Model Railway Scale Converter");
        headerLabel.setFont(Font.font("Verdana", 20));
        headerLabel.setPadding(new Insets(10));

        // ComboBox to choose the scale and a descriptive Label next to it.
        // Am only doing N, OO, TT120, and O for my own use. Other scales exist.
        Label chooseScaleLabel = new Label("Select scale: ");
        GridPane.setHalignment(chooseScaleLabel, HPos.RIGHT);
        scaleComboBox = new ComboBox<>();
        scaleComboBox.getItems().addAll("N", "O", "OO", "TT120");

        // ComboBox to choose precision (number of decimal points)
        Label choosePrecisionLabel = new Label("Number of decimal points: ");
        precisionComboBox = new ComboBox<>();
        precisionComboBox.getItems().addAll(0, 1, 2, 3);

        // Label and radio buttons to choose whether converting from real to model measurements, or from
        // model to real measurements
        realToModelButton = new RadioButton("Real Measurement -> Model Measurement");
        modelToRealButton = new RadioButton("Model Measurement -> Real Measurement");

        // add the radio buttons to a toggle group
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(realToModelButton, modelToRealButton);

        // GridPane to hold the selection labels, ComboBoxes and radio buttons
        GridPane selectionGrid = new GridPane();
        selectionGrid.setPadding(new Insets(15, 15, 15, 15));
        selectionGrid.setAlignment(Pos.CENTER);
        selectionGrid.setVgap(15);
        selectionGrid.setHgap(10);

        // Add the scale selection label and combo box, and the precision selection label and combo box to the grid
        selectionGrid.addRow(0, chooseScaleLabel, scaleComboBox);
        selectionGrid.addRow(1, choosePrecisionLabel, precisionComboBox);

        // Add the radio buttons to the grid and make them each span two columns
        selectionGrid.addRow(2, realToModelButton);
        selectionGrid.addRow(3, modelToRealButton);
        GridPane.setColumnSpan(realToModelButton, 2);
        GridPane.setColumnSpan(modelToRealButton, 2);

        // Create a label for brief instruction
        inputInstructionLabel = new Label("");

        // Create a TextField for entering values for the input length
        inputValueField = new TextField();
        inputValueField.setMinWidth(90);
        inputValueField.setMaxWidth(90);
        inputValueField.setPromptText("Enter value");
        GridPane.setHalignment(inputValueField, HPos.RIGHT);

        // Create a ComboBox for selecting input units
        inputUnitBox = new ComboBox<>();
        inputUnitBox.setMinWidth(90);
        inputUnitBox.setMaxWidth(90);
        inputUnitBox.getItems().addAll("mm", "cm", "m", "km", "inches", "feet", "miles");

        // Label for instructions on choosing the output unit
        outputInstructionLabel = new Label("Enter unit to convert to: ");
        GridPane.setHalignment(outputInstructionLabel, HPos.RIGHT);

        // Create a ComboBox for selecting output units
        outputUnitBox = new ComboBox<>();
        outputUnitBox.setMinWidth(90);
        outputUnitBox.setMaxWidth(90);
        outputUnitBox.getItems().addAll("mm", "cm", "m", "km", "inches", "feet", "miles");

        // Add the input selection label, TextField and ComboBox to the selection grid pane
        selectionGrid.addRow(4, inputInstructionLabel);
        GridPane.setColumnSpan(inputInstructionLabel, 2);
        selectionGrid.addRow(5, inputValueField, inputUnitBox);

        // Add the unit output label and ComboBox to the selection grid pane
        selectionGrid.addRow(7, outputInstructionLabel, outputUnitBox);

        // Add a button to perform the calculation and display the results
        calculateButton = new Button("Calculate");
        calculateButton.setMinWidth(100);
        calculateButton.setMaxWidth(100);
        calculateButton.setDisable(true);

        // Add a results label to display the results
        resultsLabel = new Label();
        resultsLabel.setText("");
        resultsLabel.setVisible(false);
        resultsLabel.setId("results");

        // Input combo boxes and text fields should be invisible until what kind of calculation is selected
        makeInputValueAndUnitsVisible(false);


        // -------------Set the root Vbox, the scene, and the stage ----------------

        // Root VBox that holds the other containers
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.getChildren().addAll(headerLabel, selectionGrid, calculateButton, resultsLabel);

        // Set the scene and the stage
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("Stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Model Railway Scale Converter");
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.show();

        // ------------ Event handlers -------------------------

        // Whenever value in a ComboBox or value entered in field, checks if valid input
        scaleComboBox.setOnAction(e -> validateInput());
        precisionComboBox.setOnAction(e -> validateInput());
        inputUnitBox.setOnAction(e -> validateInput());
        outputUnitBox.setOnAction(e -> validateInput());
        inputValueField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());

        // connect radio buttons to event handler for switching between real -> model and model -> real display
        realToModelButton.setOnAction(e -> 
            {
                // Make the input instructions and selection objects visible to the user
                makeInputValueAndUnitsVisible(true);
                inputInstructionLabel.setText("Enter real-world measurements:");
                inputUnitBox.setValue("m");
                inputValueField.clear();
                outputUnitBox.setValue("mm");
            }
        );

       
        modelToRealButton.setOnAction(e ->
            {
                 // Make the input instructions and selection objects visible to the user
                makeInputValueAndUnitsVisible(true);
                inputInstructionLabel.setText("Enter model measurements:");
                inputUnitBox.setValue("mm");
                inputValueField.clear();
                outputUnitBox.setValue("m");
            }
        );

        // Event handler for the calculate button
        calculateButton.setOnAction(e ->
            {
                String resultString = createResultsString();
                resultsLabel.setText("\n" + resultString);
                resultsLabel.setVisible(true);
            }
        );
    }

    @SuppressWarnings("unused")
    private void validateInput()
    {
        // Changing an input option should remove the results
        resultsLabel.setVisible(false);
        
        // Verify that all the input fields are filled in and have valid values
        boolean isValid = isInputValid();

        // If inputs valid, make the calculate button active (i.e. usable)
        if (isValid) {
            calculateButton.setDisable(false);
        } else {
            calculateButton.setDisable(true);
        }
    }

    @SuppressWarnings("unused")
    private boolean isInputValid()
    {
        boolean isValid = true;

        // Does every combo box have a value selected?
        if (scaleComboBox.getValue() == null || precisionComboBox.getValue() == null
            || inputUnitBox.getValue() == null || outputUnitBox.getValue() == null)
        {
            isValid = false;
        }

        // Is the number entered in the value entry field numeric?
        try {
            double userSuppliedNumber = Double.parseDouble(inputValueField.getText());
        } catch(Exception e) {
            isValid = false;
        }
        return isValid;
    }

    // Use to make the entry forms, instruction labels and combo boxes visible when one of the
    // real -> model or model -> real radio buttons is selected
    private void makeInputValueAndUnitsVisible(boolean isVisible)
    {
        inputInstructionLabel.setVisible(isVisible);
        inputUnitBox.setVisible(isVisible);
        inputValueField.setVisible(isVisible);
        outputInstructionLabel.setVisible(isVisible);
        outputUnitBox.setVisible(isVisible);
        calculateButton.setVisible(isVisible);
    }

    // Convert from a model length in a supplied sacle to a real-world length (in the same length unit) 
    private double convertModelToRealValue(double value, String scale)
    {
        double scaleFactor = getScaleFactor(scale);
        return value * scaleFactor;
    }

    // Convert from a real length to a model length in the supplied scale (in the same length unit) 
    private double convertRealToModelValue(double value, String scale)
    {
        double scaleFactor = 1.0 / getScaleFactor(scale);
        return value * scaleFactor;
    }

    // Get the scale factor for the different model railway scales (N, OO, TT120 etc)
    private double getScaleFactor(String scale)
    {
        Map<String, Double> scaleFactors = new HashMap<>();
        scaleFactors.put("N", 148.0);
        scaleFactors.put("OO", 76.0);
        scaleFactors.put("TT120", 120.0);
        scaleFactors.put("O", 43.5);
        return scaleFactors.get(scale);
    }

    // Create the results string that is displayed on the result label after the calculate button is clicked
    private String createResultsString()
    {
        if (!isInputValid())
        {
            return "";
        }

         // Create string to be displayed   
        String resultString = inputValueField.getText() + inputUnitBox.getValue();
        if (realToModelButton.isSelected()) {
            resultString += " in the real-world = ";
        }
        if (modelToRealButton.isSelected()) {
            resultString += " in the model = ";
        }
        double inputValue = Double.parseDouble(inputValueField.getText());
        double outputValue = LengthScaleConverter.convert(inputValue, inputUnitBox.getValue(), outputUnitBox.getValue());

        // Get the scale and use its number to perform the actual conversion
        if (realToModelButton.isSelected()) {
            outputValue = convertRealToModelValue(outputValue, scaleComboBox.getValue());
        }
        if (modelToRealButton.isSelected()) {
            outputValue = convertModelToRealValue(outputValue, scaleComboBox.getValue());
        }

        // Need number of hashes after decimal point to equal the number in precision COmboBox
        String decimalFormatString = "#.";
        for (int i = 0; i < precisionComboBox.getValue(); i++) {
            decimalFormatString += "#";
        }
        DecimalFormat df = new DecimalFormat(decimalFormatString);
        resultString += df.format(outputValue) + outputUnitBox.getValue();

        if (realToModelButton.isSelected()) {
            resultString += " in the model";
        }
        if (modelToRealButton.isSelected()) {
            resultString += " in the real-world";
        }
        return resultString;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
    
}
