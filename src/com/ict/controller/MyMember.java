package com.ict.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebServlet("/MyMember")
public class MyMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		// 토큰을 이용해서 API 호출 하기 위해서 session에 저장된 토큰 호출
		HttpSession session = request.getSession();
		String access_token = (String)session.getAttribute("access_token");
		String refresh_token = (String)session.getAttribute("refresh_token");
		
     	// 사용자 정보 요청에 필용한 URL
		String header = "Bearer " + access_token;
		String apiurl = "https://kapi.kakao.com/v2/user/me";
		
		Map<String, String> reqeustHeaders = new HashMap<String, String>();
		reqeustHeaders.put("Authorization", header);
		
		StringBuffer responseBody = new StringBuffer();
		try {
			URL url = new URL(apiurl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// POST 요청을 위해서 기본값이 false인 setDoOutput를 true
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			for (Map.Entry<String, String> k : reqeustHeaders.entrySet()) {
				conn.setRequestProperty(k.getKey(), k.getValue());
			}
			
			int responseCode = conn.getResponseCode();
			System.out.println(responseCode);
			if(responseCode == HttpURLConnection.HTTP_OK) {
				InputStreamReader reader 
					= new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(reader);
				String line;
				while((line = br.readLine()) != null ) {
					responseBody.append(line);
				
				}
			}else {
				responseBody.append("실패");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		out.print(responseBody.toString());
	}
}
