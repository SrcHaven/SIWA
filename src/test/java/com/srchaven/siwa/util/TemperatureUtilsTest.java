package com.srchaven.siwa.util;

import org.junit.Test;

import com.srchaven.siwa.util.TemperatureUtils;

import static org.junit.Assert.*;

public class TemperatureUtilsTest
{

	@Test
	public void testToFahrenheit_doubleNormal()
	{
		double expected = 32D;
		double actual = TemperatureUtils.toFahrenheit(0D);
		assertEquals(expected, actual, 1D);
	}

	/**
	 * Test a value so negative that it won't fit, even in scientific notation.
	 */
	@Test
	public void testToFahrenheit_Negative_Extreme()
	{
		double value = -1.23456789e123;
		int maxLength = 7;
		String expected = "NegInf";
        
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a value so negative that it must be converted to scientific notation.
	 */
	@Test
	public void testToFahrenheit_Negative_ScientificNotation()
	{
		double value = -123456789123456789f;
		int maxLength = 7;
		String expected = "-2.2E17";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a negative value that has too many decimal places and will be truncated, but the resulting string ends up
	 * in the form "-123457." meaning that the decimal place is useless and should be truncated as well.
	 */
	@Test
	public void testToFahrenheit_Negative_TruncateDecimalPoint()
	{
		double value = -68604.88277777778;
		int maxLength = 7;
		String expected = "-123457";
        
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}

	/**
	 * Test a negative value that has too many decimal places and must be truncated.
	 */
	@Test
	public void testToFahrenheit_Negative_Truncate()
	{
		double value = -20.051055;
		int maxLength = 7;
		String expected = "-4.0919";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a negative value that is within the expected length
	 */
	@Test
	public void testToFahrenheit_Negative_WithinExpectedLength()
	{
		double value = -20.051;
		int maxLength = 7;
		String expected = "-4.0918";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a positive value so large that it won't fit, even in scientific notation.
	 */
	@Test
	public void testToFahrenheit_Positive_Extreme()
	{
		double value = 123456789e123;
		int maxLength = 7;
		String expected = "PosInf";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a positive value so large that it won't fit, even in scientific notation.
	 */
	@Test
	public void testToFahrenheit_Positive_ScientificNotation()
	{
		double value = 123456789123456789f;
		int maxLength = 7;
		String expected = "2.22E17";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a positive value that has too many decimal places and will be truncated, but the resulting string ends up
	 * in the form "123456." meaning that the decimal place is useless and should be truncated as well.
	 */
	@Test
	public void testToFahrenheit_Positive_TruncateDecimalPoint()
	{
		double value = 68569.32722222224;
		int maxLength = 7;
		String expected = "123457";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a positive value that has too many decimal places and must be truncated.
	 */
	@Test
	public void testToFahrenheit_Positive_Truncate()
	{
		double value = -15.50445;
		int maxLength = 7;
		String expected = "4.09199";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test a negative value that is within the expected length
	 */
	@Test
	public void testToFahrenheit_Positive_WithinExpectedLength()
	{
		double value = -15.50445;
		int maxLength = 7;
		String expected = "4.09199";
		
		String actual = TemperatureUtils.toFahrenheit(value, maxLength);
		assertEquals(expected, actual);
	}
	
}
