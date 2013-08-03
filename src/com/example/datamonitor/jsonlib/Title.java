package com.example.datamonitor.jsonlib;

import java.sql.Date;
import java.util.List;

import com.example.datamonitor.R.string;

/*
{
	  "datastream_id": "string",
	  "title": "string",
	  "tags": "string",
	  "desc": "string",
	  "owner": "string",
	  "updated_time": "Date",
	  "created_time": "Date",
	  "total_units": 0,
	  "total_blocks": 0,
	  "units_list": [
	    {
	      "unit_id": "string",
	      "unit_symbol": "string",
	      "unit_label": "string",
	      "value_type": "string"
	    }
	  ]
	}
*/
public class Title {
	public String dataStream_id;
	public String title;
	public String tags;
	public String desc;
	public String owner;
	public Date updated_timeDate;
	public Date created_time;
	public int total_units;
	public int total_bloacks;
	public List<Unit> units_list;
	
}
