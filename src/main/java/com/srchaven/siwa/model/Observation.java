package com.srchaven.siwa.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Data model for weather observations.
 */
@Entity
@Table(catalog = "weather_data", name = "observations")
public class Observation implements ObservationSourceStringCarrierIF
{
    private int observationID;

    private String filename;

    /** The source string for this Observation (the line from the input file) */
    private String sourceString;
    // Example Record:
    // 26655 20110101 0100 20101231 1600 2.404 -162.92 68.03 -19.9 -20.0 -18.5 -20.4
    // cols 1 -- 5 [5 chars] WBANNO

    /** The station WBAN number. */
    private String wbanNo;

    public static final int WBANNO_MAX_LENGTH = 5;
    // cols 7 -- 14 [8 chars] UTC_DATE

    /** The UTC date of the observation. */
    private String utcDate;

    public static final int UTCDATE_MAX_LENGTH = 8;
    // cols 16 -- 19 [4 chars] UTC_TIME

    /**
     * The UTC time of the observation. Time is the end of the observed hour, so the 00 hour belongs with the previous
     * day's observation, since it contains observations between 11 PM and midnight of that day.
     */
    private String utcTime;

    public static final int UTCTIME_MAX_LENGTH = 4;
    // cols 21 -- 28 [8 chars] LST_DATE

    /** The Local Standard Time (LST) date of the observation. */
    private String lstDate;

    public static final int LSTDATE_MAX_LENGTH = 8;
    // cols 30 -- 33 [4 chars] LST_TIME

    /** The Local Standard Time (LST) time of the observation. Time is the end of the observed hour. */
    private String lstTime;

    public static final int LSTTIME_MAX_LENGTH = 4;
    // cols 35 -- 40 [6 chars] CRX_VN

    /**
     * The version number of the station datalogger program that was in effect at the time of the observation.
     *
     * Note: should be treated as a string.
     */
    private String crxVn;

    public static final int CRXVN_MAX_LENGTH = 6;
    // cols 42 -- 48 [7 chars] LONGITUDE

    /** Station longitude. */
    private String longitude;

    public static final int LONGITUDE_MAX_LENGTH = 7;
    // cols 50 -- 56 [7 chars] LATITUDE

    /** Station latitude. */
    private String latitude;

    public static final int LATITUDE_MAX_LENGTH = 7;
    // cols 58 -- 64 [7 chars] T_CALC
    // Value in file is in Celsius

    /**
     * Average temperature during the last 5 minutes of the hour.
     *
     * Note: USCRN/USRCRN stations have multiple co-located temperature sensors that record independent measurements.
     * This value is a single temperature number that is calculated from the multiple independent measurements.
     */
    private String tCalc;

    public static final int TCALC_MAX_LENGTH = 7;
    // cols 66 -- 72 [7 chars] T_HR_AVG
    // Value in file is in Celsius

    /**
     * Average temperature during the entire hour.
     *
     * Note: USCRN/USRCRN stations have multiple co-located temperature sensors that record independent measurements.
     * This value is a single temperature number that is calculated from the multiple independent measurements.
     */
    private String tHrAvg;

    public static final int THRAVG_MAX_LENGTH = 7;
    // cols 74 -- 80 [7 chars] T_MAX
    // Value in file is in Celsius

    /**
     * Maximum temperature during the hour.
     *
     * Note: USCRN/USRCRN stations have multiple co-located temperature sensors that record independent measurements.
     * This value is a single temperature number that is calculated from the multiple independent measurements. The
     * independent measurements are the maximum for each sensor of 5-minute average temperatures measured every 10
     * seconds during the hour.
     */
    private String tMax;

    public static final int TMAX_MAX_LENGTH = 7;
    // cols 82 -- 88 [7 chars] T_MIN
    // Value in file is in Celsius

    /**
     * Minimum temperature during the hour.
     *
     * Note: USCRN/USRCRN stations have multiple co-located temperature sensors that record independent measurements.
     * This value is a single temperature number that is calculated from the multiple independent measurements. The
     * independent measurements are the minimum for each sensor of 5-minute average temperatures measured every 10
     * seconds during the hour.
     */
    private String tMin;

    public static final int TMIN_MAX_LENGTH = 7;

    /**
     * Unit of temperature used for all temperature readings. F = Fahrenheit, C = Celsius, K = Kelvin.
     */
    private char temperatureUnit;

    /**
     * Supplemental reports. This contains optional supplements to the report, such as surfing conditions or golfing
     * conditions.
     */
    List<SupplementalReport> supplementalReports;

//TODO: JavaDoc
//TODO: Better parameter validation
//TODO: Use individual parameters instead of an array
//TODO: Enum for temperature unit instead of char 
    public Observation(String sourceString, String filename, String[] fields, char temperatureUnit)
    {
        this.sourceString = sourceString;

        this.filename = filename;
        this.wbanNo = fields[0];
        this.utcDate = fields[1];
        this.utcTime = fields[2];
        this.lstDate = fields[3];
        this.lstTime = fields[4];
        this.crxVn = fields[5];
        this.longitude = fields[6];
        this.latitude = fields[7];
        this.tCalc = fields[8];
        this.tHrAvg = fields[9];
        this.tMax = fields[10];
        this.tMin = fields[11];

        this.supplementalReports = new ArrayList<SupplementalReport>();
    }

    /**
     * Getter.
     *
     * @return the observation ID for this Observation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "observationID")
    public int getObservationID()
    {
        return observationID;
    }

    /**
     * Setter.
     *
     * @param the observation ID for this observation.
     */
    public void setObservationID(int observationID)
    {
        this.observationID = observationID;
    }

    /**
     * Getter.
     *
     * @return the file that this report was extracted from.
     */
    @Transient
    public String getFilename()
    {
        return filename;
    }

    /**
     * Setter.
     *
     * @param filename the file that this report was extracted from.
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    /**
     * Getter.
     *
     * @return the station WBAN number.
     */
    @Column(name = "wbanNo", length = WBANNO_MAX_LENGTH, nullable = false)
    public String getWbanNo()
    {
        return wbanNo;
    }

    /**
     * Setter.
     *
     * @param wbanNo the station WBAN number. Maximum length is {@code WBANNO_MAX_LENGTH}. Cannot be {@code null}.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setWbanNo(String wbanNo)
    {
        if (wbanNo == null)
        {
            throw new IllegalArgumentException("Station WBAN number cannot be null");
        }
        else if (wbanNo.length() > WBANNO_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Station WBAN number cannot be larger than " + WBANNO_MAX_LENGTH
                    + " characters");
        }

        this.wbanNo = wbanNo;
    }

    /**
     * Getter.
     *
     * @return the UTC date of the observation.
     */
    @Column(name = "utcDate", length = UTCDATE_MAX_LENGTH, nullable = false)
    public String getUtcDate()
    {
        return utcDate;
    }

    /**
     * Setter.
     *
     * @param utcDate the UTC date of the observation. Maximum length is {@code UTCDATE_MAX_LENGTH}. Cannot be
     *            {@code null}.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setUtcDate(String utcDate)
    {
        if (utcDate == null)
        {
            throw new IllegalArgumentException("Observation UTC date cannot be null");
        }
        else if (utcDate.length() > UTCDATE_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Observation UTC date cannot be larger than " + UTCDATE_MAX_LENGTH
                    + " characters");
        }

        this.utcDate = utcDate;
    }

    /**
     * Getter.
     *
     * @return the UTC time of the observation. Time is the end of the observed hour, so the 00 hour belongs with the
     *         previous day's observation, since it contains observations between 11 PM and midnight of that day.
     */
    @Column(name = "utcTime", length = UTCTIME_MAX_LENGTH, nullable = false)
    public String getUtcTime()
    {
        return utcTime;
    }

    /**
     * Setter.
     *
     * @param utcDate the UTC date of the observation. Time is the end of the observed hour, so the 00 hour belongs with
     *            the previous day's observation, since it contains observations between 11 PM and midnight of that day.
     *            Maximum length is {@code UTCTIME_MAX_LENGTH}. Cannot be {@code null}.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setUtcTime(String utcTime)
    {
        if (utcTime == null)
        {
            throw new IllegalArgumentException("Observation time date cannot be null");
        }
        else if (utcTime.length() > UTCTIME_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Observation UTC time cannot be larger than " + UTCTIME_MAX_LENGTH
                    + " characters");
        }

        this.utcTime = utcTime;
    }

    /**
     * Getter.
     *
     * @return the Local Standard Time (LST) date of the observation.
     */
    @Column(name = "lstDate", length = LSTDATE_MAX_LENGTH)
    public String getLstDate()
    {
        return lstDate;
    }

    /**
     * Setter.
     *
     * @param lstDate the Local Standard Time (LST) date for the observation. Maximum length is
     *            {@code LSTDATE_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void setLstDate(String lstDate)
    {
        if (lstDate != null && lstDate.length() > LSTDATE_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Observation LST date cannot be larger than " + LSTDATE_MAX_LENGTH
                    + " characters");
        }

        this.lstDate = lstDate;
    }

    /**
     * Getter.
     *
     * @return the Local Standard Time (LST) time of the observation. Time is the end of the observed hour.
     */
    @Column(name = "lstTime", length = LSTTIME_MAX_LENGTH)
    public String getLstTime()
    {
        return lstTime;
    }

    /**
     * Setter.
     *
     * @param lstDate the Local Standard Time (LST) time of the observation. Time is the end of the observed hour.
     *            Maximum length is {@code LSTTIME_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void setLstTime(String lstTime)
    {
        if (lstTime != null && lstTime.length() > LSTTIME_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Observation LST time cannot be larger than " + LSTTIME_MAX_LENGTH
                    + " characters");
        }

        this.lstTime = lstTime;
    }

    /**
     * Getter.
     *
     * @return the version number of the station datalogger program that was in effect at the time of the observation.
     */
    @Column(name = "crxVn", length = CRXVN_MAX_LENGTH)
    public String getCrxVn()
    {
        return crxVn;
    }

    /**
     * Setter.
     *
     * @param crxVn the version number of the station datalogger program that was in effect at the time of the
     *            observation. Maximum length is {@code CRXVN_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void setCrxVn(String crxVn)
    {
        if (crxVn != null && crxVn.length() > CRXVN_MAX_LENGTH)
        {
            throw new IllegalArgumentException("CRX version cannot be larger than " + CRXVN_MAX_LENGTH + " characters");
        }

        this.crxVn = crxVn;
    }

    /**
     * Getter.
     *
     * @return station longitude.
     */
    @Column(name = "longitude", length = LONGITUDE_MAX_LENGTH)
    public String getLongitude()
    {
        return longitude;
    }

    /**
     * Setter.
     *
     * @param longitude station longitude. Maximum length is {@code LONGITUDE_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void setLongitude(String longitude)
    {
        if (longitude != null && longitude.length() > LONGITUDE_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Station longitude cannot be larger than " + LONGITUDE_MAX_LENGTH
                    + " characters");
        }

        this.longitude = longitude;
    }

    /**
     * Getter.
     *
     * @return station latitude.
     */
    @Column(name = "latitude", length = LATITUDE_MAX_LENGTH)
    public String getLatitude()
    {
        return latitude;
    }

    /**
     * Setter.
     *
     * @param latitude station latitude. Maximum length is {@code LATITUDE_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void setLatitude(String latitude)
    {
        if (latitude != null && latitude.length() > LATITUDE_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Station latitude cannot be larger than " + LATITUDE_MAX_LENGTH
                    + " characters");
        }

        this.latitude = latitude;
    }

    /**
     * Getter.
     *
     * @return average temperature during the last 5 minutes of the hour. Note: USCRN/USRCRN stations have multiple
     *         co-located temperature sensors that record independent measurements. This value is a single temperature
     *         number that is calculated from the multiple independent measurements.
     */
    @Column(name = "tCalc", length = TCALC_MAX_LENGTH)
    public String gettCalc()
    {
        return tCalc;
    }

    /**
     * Setter.
     *
     * @param tCalc average temperature during the last 5 minutes of the hour. Note: USCRN/USRCRN stations have multiple
     *            co-located temperature sensors that record independent measurements. This value is a single
     *            temperature number that is calculated from the multiple independent measurements. Maximum length is
     *            {@code TCALC_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void settCalc(String tCalc)
    {
        if (tCalc != null && tCalc.length() > TCALC_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Average temperature for the last 5 minutes cannot be larger than "
                    + TCALC_MAX_LENGTH + " characters");
        }

        this.tCalc = tCalc;
    }

    /**
     * Getter.
     *
     * @return average temperature during the entire hour. Note: USCRN/USRCRN stations have multiple co-located
     *         temperature sensors that record independent measurements. This value is a single temperature number that
     *         is calculated from the multiple independent measurements.
     */
    @Column(name = "tHrAvg", length = THRAVG_MAX_LENGTH)
    public String gettHrAvg()
    {
        return tHrAvg;
    }

    /**
     * Setter.
     *
     * @param tHrAvg average temperature during the entire hour. Note: USCRN/USRCRN stations have multiple co-located
     *            temperature sensors that record independent measurements. This value is a single temperature number
     *            that is calculated from the multiple independent measurements. Maximum length is
     *            {@code THRAVG_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void settHrAvg(String tHrAvg)
    {
        if (tHrAvg != null && tHrAvg.length() > THRAVG_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Average temperature for the last hour cannot be larger than "
                    + THRAVG_MAX_LENGTH + " characters");
        }

        this.tHrAvg = tHrAvg;
    }

    /**
     * Getter.
     *
     * @return maximum temperature during the hour. Note: USCRN/USRCRN stations have multiple co-located temperature
     *         sensors that record independent measurements. This value is a single temperature number that is
     *         calculated from the multiple independent measurements. The independent measurements are the maximum for
     *         each sensor of 5-minute average temperatures measured every 10 seconds during the hour.
     */
    @Column(name = "tMax", length = TMAX_MAX_LENGTH)
    public String gettMax()
    {
        return tMax;
    }

    /**
     * Setter.
     *
     * @param tMax maximum temperature during the hour. Note: USCRN/USRCRN stations have multiple co-located temperature
     *            sensors that record independent measurements. This value is a single temperature number that is
     *            calculated from the multiple independent measurements. The independent measurements are the maximum
     *            for each sensor of 5-minute average temperatures measured every 10 seconds during the hour. Maximum
     *            length is {@code TMAX_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void settMax(String tMax)
    {
        if (tMax != null && tMax.length() > TMAX_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Maximum temperature for the last hour cannot be larger than "
                    + TMAX_MAX_LENGTH + " characters");
        }

        this.tMax = tMax;
    }

    /**
     * Getter.
     *
     * @return minimum temperature during the hour. Note: USCRN/USRCRN stations have multiple co-located temperature
     *         sensors that record independent measurements. This value is a single temperature number that is
     *         calculated from the multiple independent measurements. The independent measurements are the minimum for
     *         each sensor of 5-minute average temperatures measured every 10 seconds during the hour.
     */
    @Column(name = "tMin", length = TMIN_MAX_LENGTH)
    public String gettMin()
    {
        return tMin;
    }

    /**
     * Setter.
     *
     * @param tMin minimum temperature during the hour. Note: USCRN/USRCRN stations have multiple co-located temperature
     *            sensors that record independent measurements. This value is a single temperature number that is
     *            calculated from the multiple independent measurements. The independent measurements are the minimum
     *            for each sensor of 5-minute average temperatures measured every 10 seconds during the hour. Maximum
     *            length is {@code TMIN_MAX_LENGTH}.
     *
     * @throws IllegalArgumentException if the parameter is invalid;
     */
    public void settMin(String tMin)
    {
        if (tMin != null && tMin.length() > TMIN_MAX_LENGTH)
        {
            throw new IllegalArgumentException("Minimum temperature for the last hour cannot be larger than "
                    + TMIN_MAX_LENGTH + " characters");
        }

        this.tMin = tMin;
    }

    /**
     * Getter
     *
     * @return unit of temperature used for all temperature readings. F = Fahrenheit, C = Celsius, K = Kelvin.
     */
    @Column(name = "temperatureUnit")
    public char getTemperatureUnit()
    {
        return temperatureUnit;
    }

    /**
     * Getter.
     *
     * @param temperatureUnit unit of temperature used for all temperature readings. F = Fahrenheit, C = Celsius, K =
     *            Kelvin. Must be one of the listed temperature units.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setTemperatureUnit(char temperatureUnit)
    {
//		char upperTempUnit = Character.toUpperCase(temperatureUnit);
//
//		if (upperTempUnit != 'F' && upperTempUnit != 'C' && upperTempUnit != 'K')
//		{
//			throw new IllegalArgumentException("Unsupported temperature unit \"" + upperTempUnit + "\"");
//		}

        this.temperatureUnit = temperatureUnit;
    }

    /**
     * Getter.
     *
     * @return supplemental reports. This contains optional supplements to the observation, such as surfing conditions
     *         or golfing conditions.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "observationID")
    public List<SupplementalReport> getSupplementalReports()
    {
        return Collections.unmodifiableList(supplementalReports);
//		return supplementalReports;
    }

    /**
     * Setter.
     *
     * @param reports supplemental reports. This contains optional supplements to the observation, such as surfing
     *            conditions or golfing conditions. Cannot be {@code null} (use {@code Collections.emptyList()}).
     */
    public void setSupplementalReports(List<SupplementalReport> reports)
    {
        if (reports == null)
        {
            throw new IllegalArgumentException("Supplemental reports cannot be null");
        }

        this.supplementalReports = new ArrayList<SupplementalReport>(reports);
    }

    /**
     * Adds a supplemental report to the current list of supplemental reports.
     *
     * @param report the report to add. Cannot be {@code null}.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void addSupplementalReport(SupplementalReport report)
    {
        if (report == null)
        {
            throw new IllegalArgumentException("Report cannot be null");
        }

        this.supplementalReports.add(report);
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    @Override
    public String getSourceString()
    {
        return sourceString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Observation [");
        builder.append("filename=").append(filename);
        builder.append(", wbanNo=").append(wbanNo);
        builder.append(", utcDate=").append(utcDate);
        builder.append(", utcTime=").append(utcTime);
        builder.append(", lstDate=").append(lstDate);
        builder.append(", lstTime=").append(lstTime);
        builder.append(", crxVn=").append(crxVn);
        builder.append(", longitude=").append(longitude);
        builder.append(", latitude=").append(latitude);
        builder.append(", tCalc=").append(tCalc);
        builder.append(", tHrAvg=").append(tHrAvg);
        builder.append(", tMax=").append(tMax);
        builder.append(", tMin=").append(tMin);

        if (supplementalReports != null)
        {
            for (SupplementalReport currSupRep : supplementalReports)
            {
                builder.append(", Supplemental: [");
                builder.append(currSupRep);
                builder.append("]");
            }
        }

        return builder.toString();
    }
}
