package com.srchaven.siwa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Abstract base class for supplemental reports. Used to allow subclasses to be easily persisted.
 */
@Entity
@Table(catalog = "weather_data", name = "supplemental_reports")
public class SupplementalReport
{
	private int suppRepID;

	private String reportType;
	private String reportContents;

	public static final int REPORT_TYPE_MAX_LENGTH = 10;
	
	/**
	 * Constructor.
	 * 
	 * @param reportType the report type. Cannot be {@code null} or empty.
	 * @param reportContents the report contents. Cannot be {@code null} or empty.
	 * 
	 * @throws IllegalArgumentException if any of the parameters are invalid.
	 */
	public SupplementalReport(String reportType, String reportContents)
	{
		if (reportType == null)
		{
			throw new IllegalArgumentException("Report type cannot be null");
		}
		else if (reportType.isEmpty())
		{
			throw new IllegalArgumentException("Report type cannot be empty");
		}
		else if (reportType.length() > REPORT_TYPE_MAX_LENGTH)
		{
			throw new IllegalArgumentException("Provided report type was of length " + reportType.length()
					+ " but max length for that field is " + REPORT_TYPE_MAX_LENGTH);
		}
		if (reportContents == null)
		{
			throw new IllegalArgumentException("Report contents cannot be null");
		}
		else if (reportContents.isEmpty())
		{
			throw new IllegalArgumentException("Report contents cannot be empty");
		}
		
		this.reportType = reportType;
		this.reportContents = reportContents;
	}

	/**
	 * Getter.
	 * 
	 * @return the supplemental report ID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "suppRepID")
	public int getSuppRepID()
	{
		return suppRepID;
	}
	
	/**
	 * Setter.
	 * 
	 * @param the supplemental report ID.
	 */
	public void setSuppRepID(int suppRepID)
	{
		this.suppRepID = suppRepID;
	}
	
	/**
	 * Getter.
	 * 
	 * @return a string describing this supplemental report type. Maximum length is {@code REPORT_TYPE_MAX_LENGTH}.
	 */
	@Column(name = "reportType", nullable = false, length = REPORT_TYPE_MAX_LENGTH)
	public String getReportType()
	{
		return reportType;
	}

	/**
	 * Setter.
	 * 
	 * @param reportType a string describing this supplemental report type. Maximum length is
	 *            {@code REPORT_TYPE_MAX_LENGTH}. Cannot be {@code null} or empty.
	 * 
	 * @throws IllegalArgumentException if the parameter is invalid.
	 */
	public void setReportType(String reportType)
	{
		if (reportType == null)
		{
			throw new IllegalArgumentException("Report type cannot be null");
		}
		else if (reportType.isEmpty())
		{
			throw new IllegalArgumentException("Report type cannot be empty");
		}
		else if (reportType.length() > REPORT_TYPE_MAX_LENGTH)
		{
			throw new IllegalArgumentException("Provided report type was of length " + reportType.length()
					+ " but max length for that field is " + REPORT_TYPE_MAX_LENGTH);
		}

		this.reportType = reportType;
	}

	/**
	 * Getter.
	 * 
	 * @return the contents of the supplemental report.
	 */
	@Column(name = "reportContents", nullable = false)
	public String getReportContents()
	{
		return reportContents;
	}

	/**
	 * Setter.
	 * 
	 * @param reportContents the contents of the supplemental report. Cannot be {@code null} or empty.
	 * 
	 * @throws IllegalArgumentException if the parameter is invalid.
	 */
	public void setReportContents(String reportContents)
	{
		if (reportContents == null)
		{
			throw new IllegalArgumentException("Report contents cannot be null");
		}
		else if (reportContents.isEmpty())
		{
			throw new IllegalArgumentException("Report contents cannot be empty");
		}
		
		this.reportContents = reportContents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getReportType() + ": " + getReportContents();
	}
}
