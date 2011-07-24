package com.srchaven.siwa.util;

/**
 * Helper bean that is able to retrieve information about the host system.
 */
public class SystemInfo
{
	/**
	 * Getter.
	 * 
	 * @return the number of processors available to this JVM, as a {@code String}
	 */
	public String getNumProcessors()
	{
		return Integer.toString(Runtime.getRuntime().availableProcessors());
	}
}
