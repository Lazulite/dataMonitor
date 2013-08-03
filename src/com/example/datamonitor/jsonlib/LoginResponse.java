package com.example.datamonitor.jsonlib;

import java.sql.Date;

import com.example.datamonitor.R.string;
import com.google.gson.annotations.*;

/*
{
	  "result": "succeed",
	  "usertoken": {
	    "loginid": "lei",
	    "token": "ceeeb2c68e9b4d28b670fd570c6dd795",
	    "expire_in_seconds": "444444"
	  },
	  "userinfo": {
	    "avatar": {
	      "loginid": "lei",
	      "url": "http://146.169.35.28:55555/healthbook/v1/users/avatar/defaults/undefined_avatar.gif"
	    },
	    "user": {
	      "loginid": "lei",
	      "screenname": "Leslie",
	      "email": "luckytobegin@gmail.com",
	      "birthday": "1/1/1990",
	      "gender": "female",
	      "height_cm": "0.0",
	      "weight_kg": "0.0",
	      "country": "",
	      "language": "en",
	      "created_time": "Jul 1, 2013 3:03:49 PM"
	    },
	    "total_followers": "0",
	    "total_followings": "0",
	    "is_follower": false,
	    "is_following": false
	  }
	}*/

public class LoginResponse {
	
	@SerializedName("result")
	public String result;
	
	@SerializedName("usertoken")
	public static Usertoken usertoken;
	
	public static class userInfo{
		public static class avatar{
			@SerializedName("loginid")
			public String loginid;
			@SerializedName("url")
			public String url;
		}
		public static class user{
			public String loginid;
			public String screenname;
			public String email;
			public String birthday;
			public String gender;
			public Double height_cm;
			public Double weight_kg;
			public Double country;
			public Double language;
			public Date created_time;
		}
		public Integer total_followers;
		public Integer total_followings;
		public Boolean is_follower;
		public Boolean is_following;
	}
	
	public static String gettoken(){
		return usertoken.token;
	}
}
