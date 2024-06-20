package com.sandy.ecp.framework.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IoUtil {

	public static String readyString(InputStream in , String charsetName) throws IOException {
		InputStreamReader isr = new InputStreamReader(in, charsetName);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
	
	/**
     * Closes the given stream. The same as calling {@link Closeable#close()} , but errors while closing are silently ignored.
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
            	closeable.close();
            }
        } catch (IOException ignore) {
            // Exception is silently ignored
        }
    }
}
