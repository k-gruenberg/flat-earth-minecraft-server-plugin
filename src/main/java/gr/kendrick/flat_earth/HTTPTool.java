package gr.kendrick.flat_earth;

public final class HTTPTool {

	private HTTPTool() {} // private constructor to prevent instantiation (Utility class)

	public static String getRequest(final String urlString, final String userAgent) {
		// -------------------- -> https://www.baeldung.com/java-http-request --------------------
		try {
			java.net.URL url = new java.net.URL(urlString);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setInstanceFollowRedirects(true); // "By default, the behavior is enabled."
			// "Provide a valid HTTP Referer or User-Agent identifying the application (stock User-Agents as set by http libraries will not do)."
			//    -> https://operations.osmfoundation.org/policies/nominatim/
			// -> https://stackoverflow.com/questions/2529682/setting-user-agent-of-a-java-urlconnection
			con.setRequestProperty("User-Agent", userAgent);
			int status = con.getResponseCode(); // "To execute the request we can use the getResponseCode(), connect(), getInputStream() or getOutputStream() methods"
			if (status >= 400) { // some HTTP error
				System.out.println("HTTP error requesting '" + urlString + "' with user agent '" + userAgent + "': " + status);
				return null;
			}
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
    			content.append(inputLine);
			}
			in.close();
			con.disconnect(); // "close the connection"
			return content.toString(); // -> https://docs.oracle.com/javase/7/docs/api/java/lang/StringBuffer.html#toString()
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
