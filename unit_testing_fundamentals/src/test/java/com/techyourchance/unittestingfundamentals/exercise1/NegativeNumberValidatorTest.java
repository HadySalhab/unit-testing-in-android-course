package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {
    private NegativeNumberValidator mNegativeNumberValidator;
    @Before
    public void setup(){
        mNegativeNumberValidator = new NegativeNumberValidator();
    }
    @Test
    public void whenGivenPositiveNumber_shouldReturnFalse(){
        boolean result = mNegativeNumberValidator.isNegative(1);
        Assert.assertFalse(result);
    }
    @Test
    public void whenGiveNegativeNumber_shouldReturnTrue(){
        boolean result = mNegativeNumberValidator.isNegative(-1);
        Assert.assertTrue(result);
    }
    @Test
    public void whenGivenZero_shouldReturnFalse(){
        boolean result = mNegativeNumberValidator.isNegative(0);
        Assert.assertFalse(result);
    }

}