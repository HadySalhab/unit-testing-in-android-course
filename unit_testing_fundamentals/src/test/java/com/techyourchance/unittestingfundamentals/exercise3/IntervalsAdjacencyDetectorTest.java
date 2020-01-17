package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {
    private IntervalsAdjacencyDetector SUT;
    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }
    //Interval 1 is before Interval 2
    @Test
    public void isAdjacent_Interval1BeforeInterval2_returnFalse(){
        Interval interval1 = new Interval(-5,-1);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }
    //Interval 1 is before and adjacent Interval 2
    @Test
    public void isAdjacent_Interval1BeforeAndAdjacentInterval2_returnTrue(){
        Interval interval1 = new Interval(-5,0);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertTrue(result);
    }
    //Interval 1 is before and overlapping Interval 2
    @Test
    public void isAdjacent_Interval1BeforeAndOverlappingInterval2_returnFalse(){
        Interval interval1 = new Interval(-5,2);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }

    //Interval 1 is inside Interval 2
    @Test
    public void isAdjacent_Interval1InsideInterval2_returnFalse(){
        Interval interval1 = new Interval(1,2);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }
    //Interval 2 is inside Interval 1
    @Test
    public void isAdjacent_Interval2InsideInterval1_returnFalse(){
        Interval interval2 = new Interval(1,2);
        Interval interval1 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }

    //Interval 1 is equal to  Interval 2
    @Test
    public void isAdjacent_Interval1EqualInterval2_returnTrue(){
        Interval interval1 = new Interval(0,5);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }

    //Interval 1 is after and overlapping Interval 2
    @Test
    public void isAdjacent_Interval1AfterAndOverlappingInterval2_returnFalse(){
        Interval interval1 = new Interval(3,6);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }
    //Interval 1 is after and adjacent Interval 2
    @Test
    public void isAdjacent_Interval1AfterAndAdjacentInterval2_returnTrue(){
        Interval interval1 = new Interval(5,6);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertTrue(result);
    }
    //Interval 1 is after Interval 2
    @Test
    public void isAdjacent_Interval1AfterInterval2_returnFalse(){
        Interval interval1 = new Interval(6,10);
        Interval interval2 = new Interval(0,5);
        boolean result = SUT.isAdjacent(interval1,interval2);
        Assert.assertFalse(result);
    }


}