package com.srchaven.siwa.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

//TODO: Javadocs
public class TemperatureUtils
{

	public static double toFahrenheit(double degreesCelsius)
	{
		return degreesCelsius * 1.8 + 32;
	}

	public static String toFahrenheit(double degreesCelsius, int maxStringLength)
	{
		double degreesFahrenheit = toFahrenheit(degreesCelsius);
		
		if (degreesFahrenheit < 0)
		{
			return formatNegNumToStaticLengthString(degreesFahrenheit, maxStringLength);
		}
		else
		{
			return formatPosNumToStaticLengthString(degreesFahrenheit, maxStringLength);
		}
	}
	
    private static String formatPosNumToStaticLengthString(double value, int maxStringLength)
	{
		String valueString = Double.toString(value);
		
		if (valueString.length() <= maxStringLength)
		{
			return valueString;
		}
		//If the number is so large that we need to convert it to scientific notation
		else if (value >= Math.pow(10, maxStringLength))
		{
			//If the value is so large that the exponent would fill our entire string
			if ((10*(maxStringLength - 2)) <= Double.MAX_EXPONENT && value >= Math.pow(10, 10*(maxStringLength - 2)))
			{
				//We'll treat this as positive infinity for the sake of simplicity
				return "PosInf";
			}
			
			int exponentDigits = 1;
			while(Math.pow(10, (10 * exponentDigits)) < value)
			{
				++exponentDigits;
			}
			int numSigFigChars = maxStringLength - exponentDigits - 1;
			int decimalPlaces;
			//If we only have room for a single significant figure
			if(numSigFigChars == 1)
			{
				decimalPlaces = 0;
			}
			//Special case--if we only have room for 2 characters, the decimal place would take up one of them, so
			//there's no reason to bother outputting it
			else if (numSigFigChars == 2)
			{
				numSigFigChars = 1;
				decimalPlaces = 0;
			}
			else
			{
				decimalPlaces = numSigFigChars - 2;
			}
			
			StringBuilder formatterBuilder = new StringBuilder();
			
			formatterBuilder.append("0.");
			for (int i = 0; i < decimalPlaces; ++i)
			{
				formatterBuilder.append("#");
			}
			formatterBuilder.append("E0");
			
			NumberFormat formatter = new DecimalFormat(formatterBuilder.toString());
			return formatter.format(value);
		}
		//If the number is so close to zero that it fills up the string with a large number of decimal places, round it
		//off to the nearest valid number. (Note: Not ideal, but scientific notation seems like overkill here!)
		else if (value > 0 && value < 1)
		{
			StringBuilder formatterBuilder = new StringBuilder();
			
			int decimalPlaces = maxStringLength - 2;
			
			formatterBuilder.append("0.");
			for (int i = 0; i < decimalPlaces; ++i)
			{
				formatterBuilder.append("#");
			}
			
			NumberFormat formatter = new DecimalFormat(formatterBuilder.toString());
			
			return formatter.format(value);
		}
		//If the string is within the expected bounds, but just has too many decimal places
		else
		{
			int wholeNumbers = (int)Math.log10(value) + 1;
			int decimalPlaces;
			
			if (wholeNumbers == maxStringLength)
			{
				decimalPlaces = 0;
			}
			//Special case--no need to display the decimal point if there isn't any space left to display decimal places
			else if (wholeNumbers == maxStringLength - 1)
			{
				decimalPlaces = 0;
			}
			else
			{
				decimalPlaces = maxStringLength - (wholeNumbers + 1);
			}
			
			StringBuilder formatterBuilder = new StringBuilder();

			formatterBuilder.append("0");
			if (decimalPlaces > 0)
			{
				formatterBuilder.append(".");
			}
			for (int i = 0; i < decimalPlaces; ++i)
			{
				formatterBuilder.append("#");
			}
			
			NumberFormat formatter = new DecimalFormat(formatterBuilder.toString());
			return formatter.format(value);
		}
	}
    
    private static String formatNegNumToStaticLengthString(double value, int maxStringLength)
	{
		String valueString = Double.toString(value);
		
		if (valueString.length() <= maxStringLength)
		{
			return valueString;
		}
		//If the number is so large that we need to convert it to scientific notation
		else if (value <= -Math.pow(10, maxStringLength - 1))
		{
			//If the value is so large that the exponent would fill our entire string
			if ((10*(maxStringLength - 3)) <= Double.MAX_EXPONENT && value <= -Math.pow(10, 10*(maxStringLength - 3)))
			{
				//We'll treat this as negative infinity for the sake of simplicity
				return "NegInf";
			}
			
			int exponentDigits = 1;
			while(-Math.pow(10, (10 * exponentDigits)) > value)
			{
				++exponentDigits;
			}
			//Calculate the number of significant figure characters, leaving spaces for the 'e' and the '-'
			int numSigFigChars = maxStringLength - exponentDigits - 2;
			int decimalPlaces;
			//If we only have room for a single significant figure
			if(numSigFigChars == 1)
			{
				decimalPlaces = 0;
			}
			//Special case--if we only have room for 2 characters, the decimal place would take up one of them, so
			//there's no reason to bother outputting it
			else if (numSigFigChars == 2)
			{
				numSigFigChars = 1;
				decimalPlaces = 0;
			}
			else
			{
				decimalPlaces = numSigFigChars - 2;
			}
			
			StringBuilder formatterBuilder = new StringBuilder();
			
			formatterBuilder.append("0.");
			for (int i = 0; i < decimalPlaces; ++i)
			{
				formatterBuilder.append("#");
			}
			formatterBuilder.append("E0");
			
			NumberFormat formatter = new DecimalFormat(formatterBuilder.toString());
			return formatter.format(value);
		}
		//If the number is so close to zero that it fills up the string with a large number of decimal places, round it
		//off to the nearest valid number. (Note: Not ideal, but scientific notation seems like overkill here!)
		else if (value < 0 && value > -1)
		{
			StringBuilder formatterBuilder = new StringBuilder();
			
			//Leave space for "-0."
			int decimalPlaces = maxStringLength - 3;
			
			if (value > -Math.pow(10, -(decimalPlaces + 1)))
			{
				return "0";
			}
			
			formatterBuilder.append("0.");
			for (int i = 0; i < decimalPlaces; ++i)
			{
				formatterBuilder.append("#");
			}
			
			NumberFormat formatter = new DecimalFormat(formatterBuilder.toString());
			
			String result = formatter.format(value);
			
			//Special case--the formatter rounded off to negative zero
			if (result.equals("-0"))
			{
				return "0";
			}
			
			return result;
		}
		//If the string is within the expected bounds, but just has too many decimal places
		else
		{
			int wholeNumbers = (int)Math.log10(-value) + 1;
			int decimalPlaces;
			
			if (wholeNumbers == maxStringLength - 1)
			{
				decimalPlaces = 0;
			}
			//Special case--no need to display the decimal point if there isn't any space left to display decimal places
			else if (wholeNumbers == maxStringLength - 2)
			{
				decimalPlaces = 0;
			}
			else
			{
				decimalPlaces = maxStringLength - (wholeNumbers + 2);
			}
			
			StringBuilder formatterBuilder = new StringBuilder();

			formatterBuilder.append("0");
			if (decimalPlaces > 0)
			{
				formatterBuilder.append(".");
			}
			for (int i = 0; i < decimalPlaces; ++i)
			{
				formatterBuilder.append("#");
			}
			
			NumberFormat formatter = new DecimalFormat(formatterBuilder.toString());
			return formatter.format(value);
		}
	}
}
