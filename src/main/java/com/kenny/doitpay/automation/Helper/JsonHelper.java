package com.kenny.doitpay.automation.Helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonHelper {

	
	 private static final ObjectMapper MAPPER = new ObjectMapper();
	    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();

	    /** Kembalikan JSON yang sudah di-format. Kalau bukan JSON valid, return input asli. */
	    public static String prettyPrint(String json) {
	        if (json == null) return null;
	        try {
	            Object obj = MAPPER.readValue(json, Object.class);
	            return WRITER.writeValueAsString(obj);
	        } catch (Exception e) {
	            return json;
	        }
	    }
}
