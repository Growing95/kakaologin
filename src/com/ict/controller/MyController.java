package com.ict.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@WebServlet("/login")
public class MyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		// 2. 인증코드 받기
		String code = request.getParameter("code");
		
		// 3. 토큰 받기	(인가코드를 이용해서 )	
		String reqURL = "https://kauth.kakao.com/oauth/token";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// POST 요청을 위해서 기본값이 false인 setDoOutput를 true
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			// 필수 파라미터값 보내기 
			BufferedWriter bw = 
				new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuffer sb = new StringBuffer();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=96a9876448df3a7c145147543a0a25ff");
			sb.append("&redirect_uri=https://localhost:8090/login");
			sb.append("&code="+code);
			bw.write(sb.toString());
			bw.flush();
			
			// 인가코드로 토큰요청이 성공적으로 끝나면 /login으로 토큰을 가져온다.
			int responseCode = conn.getResponseCode();
			System.out.println(responseCode+"컨트롤러");
			// 받은 코드 읽기
			BufferedReader br
			   = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";
			String result = "";
			while((line=br.readLine())!=null) {
				 result += line ;
			}
			br.close();
			bw.close();
			
			System.out.println(result);
			
			// 받은 정보가 JSON이므로 json-sample-1.1.1.jar를  다운받아서 파싱하자 
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result.toString());
			JSONObject jobj = (JSONObject)obj;
			
			String access_token = (String)jobj.get("access_token");
			String refresh_token = (String)jobj.get("refresh_token") ;
			
			// MyMember에서 사용하기 위해서 session에 저장
			request.getSession().setAttribute("access_token", access_token);
			request.getSession().setAttribute("refresh_token", refresh_token);
			
			response.sendRedirect("view/result.jsp");
		} catch (Exception e) {
			System.out.println(e);
		} 
		
	}
}

