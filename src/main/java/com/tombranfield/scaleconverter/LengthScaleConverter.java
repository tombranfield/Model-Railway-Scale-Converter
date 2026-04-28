package com.tombranfield.scaleconverter;

import java.util.Map;
import java.util.HashMap;

public class LengthScaleConverter 
{
    // Calculates how long a given length is in another length scale
    public static double convert(double inputValue, String userInputUnit, String userOutputUnit)
    {
        String inputUnit = getValidUnit(userInputUnit);
        String outputUnit = getValidUnit(userOutputUnit);
        double inputInMetres = inputValue * convertUnitToMetres(inputUnit);
        return inputInMetres * convertMetresToUnit(outputUnit);
    }

    private static double convertUnitToMetres(String inputUnit)
    {
        Map<String, Double> unitsMap = new HashMap<>();
        unitsMap.put("mm", 0.001);
        unitsMap.put("cm", 0.01);
        unitsMap.put("m", 1.0);
        unitsMap.put("km", 1000.0);
        unitsMap.put("inch", 0.0254);
        unitsMap.put("foot", 0.3048);
        unitsMap.put("mile", 1609.34);

        return unitsMap.get(inputUnit);
    }
        
    private static double convertMetresToUnit(String outputUnit)
    {
        return 1.0 / convertUnitToMetres(outputUnit);
    }

    private static String getValidUnit(String userInputUnit)
    {
        String inputUnit = userInputUnit;
        if (userInputUnit.equals("feet") || userInputUnit.equals("ft")) {
            inputUnit = "foot";
        }
        if (userInputUnit.equals("inches")) {
            inputUnit = "inch";
        }
        if (userInputUnit.equals("miles")) {
            inputUnit = "mile";
        }
        return inputUnit;
    }
}
