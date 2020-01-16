package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {
    private StringDuplicator mStringDuplicator;
    @Before
    public void setUp() throws Exception {
        mStringDuplicator = new StringDuplicator();
    }
    @Test
    public void duplicate_emptyString_shouldReturnEmptyString(){
        String string = "";
        String result = mStringDuplicator.duplicate(string);
        Assert.assertEquals(string,result);
    }
    @Test
    public void duplicate_singleCharacter_shouldReturnDoubleCharacter(){
        String string = "a";
        String result = mStringDuplicator.duplicate(string);
        Assert.assertEquals(string+string,result);
    }
    @Test
    public void duplicate_multipleCharacter_shouldRetunrItsDouble(){
        String string="hady";
        String result = mStringDuplicator.duplicate(string);
        Assert.assertEquals(string+string,result);
    }
}