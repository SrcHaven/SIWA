package com.srchaven.siwa.util;
import org.junit.Test;

import com.srchaven.siwa.util.FileNameUtils;

import static org.junit.Assert.*;

public class FileNameUtilsTest {
	
	@Test
	public void test_extractState_normal() {
		String filename = "CRNH0202-2010-GA_Brunswick_23_S.txt";
		String expected = "GA";
		String actual = FileNameUtils.extractState(filename);
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_extractState_null() {
		String expected = "";
		String actual = FileNameUtils.extractState(null);
		assertEquals(expected, actual);
	}

}
