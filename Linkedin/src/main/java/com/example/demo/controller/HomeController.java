package com.example.demo.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

	// get client id and client Secret by creating a app in
	// https://www.linkedin.com developers
	// and set your redirect url
	public String clientId = "78bcr1itnfs1v6";
	public String clientSecret = "jioC63APQm5j3e0K";
	public String redirectUrl = "http://localhost:6060/home";

	// create button on your page and hit this get request
	@GetMapping("/authorization")
	public String authorization() {
		String authorizationUri = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="
				+ clientId + "&redirect_uri=" + redirectUrl
				+ "&state=asasasasasas&scope=r_basicprofile%20r_emailaddress";
		return "redirect:" + authorizationUri;
	}

	// after login in your linkedin account your app will hit this get request
	@GetMapping("/home")

	// now store your authorization code
	public String home(@RequestParam("code") String authorizationCode) throws JSONException {
		System.out.println(authorizationCode);
		// to trade your authorization code for access token
		String accessTokenUri = "https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&code="
				+ authorizationCode + "&redirect_uri=" + redirectUrl + "&client_id=" + clientId + "&client_secret="
				+ clientSecret + "";
		
		System.out.println(accessTokenUri);

		// linkedin api to get linkedidn profile detail
		String linedkinDetailUri = "https://api.linkedin.com/v1/people/~:(id,first-name,email-address,last-name,headline,picture-url,industry,summary)?format=json";

		// store your access token
		String accessToken = null;
		RestTemplate restTemplate = new RestTemplate();

		try {
			String accessTokenRequest = restTemplate.getForObject(accessTokenUri, String.class);
			JSONObject jsonObjOfAccessToken = new JSONObject(accessTokenRequest);
			accessToken = jsonObjOfAccessToken.get("access_token").toString();
		} catch (HttpClientErrorException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		// trade your access token
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> linkedinDetailRequest = restTemplate.exchange(linedkinDetailUri, HttpMethod.GET, entity,
				String.class);
		// store json data
		JSONObject jsonObjOfLinkedinDetail = new JSONObject(linkedinDetailRequest.getBody());
		// print json data
		System.out.println(jsonObjOfLinkedinDetail);

		return "home";
	}
}