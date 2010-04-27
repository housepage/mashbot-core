package org.mashbot.server.plugins;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * TumbleJ is a Java API for Tumblr. It aims to perform posting and retrieving content. Currently it only supports writing of text posts.
 *
 * @author <a href="http://www.daneshzaki.com">Danesh Zaki</a> <p />
 * 
 * This code is released as
 * open-source under the LGPL license. See
 * <a href="http://www.gnu.org/licenses/lgpl.html">http://www.gnu.org/licenses/lgpl.html</a>
 * for license details. This code comes with no warranty or support.
 *
 *
 */

public class TumbleJ
{

	/**
	 * The constructor calls the openConnection method to open the connection to Tumblr.com
	 */
	
	public TumbleJ()  throws Exception
	{
		openConnection();
	}

	/**
	 * This method opens the connection to the Tumblr Write API URL
	 */
	
	public void openConnection() throws Exception
	{
		URL url;
		
		// URL of tumblr.com
		url = new URL (TUMBLR_WRITE_URL);
		
		connection = url.openConnection();
		
		//to get the status of the write operation
		connection.setDoInput (true);
		
		//to write the post to the URL
		connection.setDoOutput (true);
		
		//instruction not to use cache but the fresh connection
		connection.setUseCaches (false);
		
		//encode the contents of the form
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		
	}

	/**
	 * This method writes text posts to the Tumblr tumblog. The tumblog is identified by the email and password.
	 * @param email email ID used as username 
	 * @param password password 	 
	 * @param postType presently set to regular for text posts 	 
	 * @param postTitle title of the post
	 * @param body body of the post 	 
	 * @param tags tags for the post 	 
	 * @param date date of the post
	 */
	 
	public void post(String email, String password, String postType, String postTitle, String body, String tags, String date) throws Exception
	{
		
		DataOutputStream    printout;
		
		//only regular posts are supported at present
		postType = "regular";
		
		//do a HTTP POST with the parameters
		printout = new DataOutputStream (connection.getOutputStream ());

		//add content with the parameters
		StringBuffer content = new StringBuffer();
		
		content.append("email=");
		content.append(URLEncoder.encode (email, ENCODING));

		content.append("&password=");
		content.append(URLEncoder.encode (password, ENCODING));

		content.append("&type=");
		content.append(URLEncoder.encode (postType, ENCODING));

		content.append("&title=");
		content.append(URLEncoder.encode (postTitle, ENCODING));

		content.append("&body=");
		content.append(URLEncoder.encode (body, ENCODING));

		content.append("&tags=");
		content.append(URLEncoder.encode (tags, ENCODING));

		content.append("&date=");
		content.append(URLEncoder.encode (date, ENCODING));
	
		//write to the URL stream
		printout.writeBytes (content.toString());
		printout.flush ();
		printout.close ();
		
		//get the status
		String status = getStatus();
		
	}



	/**
	 * This method gets the status of the write operation
	 */
	 
	private String getStatus() throws Exception
	{
		BufferedReader     input;

		//get the response 
		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String status = null;
		
		status = input.readLine();

		input.close ();				
		
		return status;
	
	}
		

	//main method to use the API
	public static void main(String args[]) throws Exception
	{
		TumbleJ tumblr = new TumbleJ();
		tumblr.post(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);	
			
	}	
	

	//connection to Tumblr
	private URLConnection connection = null;
	
	private static final String ENCODING = "UTF-8";
	private static final String TUMBLR_WRITE_URL = "http://www.tumblr.com/api/write";
	
}
