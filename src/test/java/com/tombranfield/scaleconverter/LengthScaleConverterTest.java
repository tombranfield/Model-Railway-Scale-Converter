package com.tombranfield.scaleconverter;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LengthScaleConverterTest {

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void enteringZeroReturnsZero()
    {
       double result = LengthScaleConverter.convert(0.0, "m", "m");
       assert(result == 0.0);
    }

    @Test
    public void usingNegativeReturnsNegativeSuccessfully()
    {
        double result = LengthScaleConverter.convert(-1, "m", "m");
        assert(result == -1.0);
    }

    @Test
    public void nonStandardUnitsUsedSuccessfully()
    {
        double result1 = LengthScaleConverter.convert(1.0, "feet", "feet");
        double result2 = LengthScaleConverter.convert(1.0, "miles", "miles");
        double result3 = LengthScaleConverter.convert(1.0, "inches", "inches");
        double eps = 0.00000001;

        System.out.println(result1);
        System.out.flush();
        assert((1 - eps) < result1 && result1 < (1 + eps));
        assert((1 - eps) < result2 && result2 < (1 + eps));
        assert((1 - eps) < result3 && result3 < (1 + eps));
    }

    @Test 
    public void convertsToDifferentUnitsSuccessfully()
    {
        double result1 = LengthScaleConverter.convert(2.0, "m", "inches");
        double result2 = LengthScaleConverter.convert(2.0, "m", "feet");
        double result3 = LengthScaleConverter.convert(2.0, "m", "km");
        double result4 = LengthScaleConverter.convert(2.0, "m", "mm");
        double result5 = LengthScaleConverter.convert(2.0, "m", "miles");

        double expected1 = 78.7402;
        double expected2 = 6.56168333333;
        double expected3 = 0.002;
        double expected4 = 2000.0;
        double expected5 = 0.00124274;
        double eps = 0.001;

        assert((expected1 - eps) < result1 && result1 < (expected1 + eps));
        assert((expected2 - eps) < result2 && result2 < (expected2 + eps));
        assert(result3 == expected3);
        assert(result4 == expected4);
        assert((expected5 - eps) < result5 && result5 < (expected5 + eps));
    }



}
