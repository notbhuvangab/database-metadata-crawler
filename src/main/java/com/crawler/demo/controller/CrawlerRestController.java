package com.crawler.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.demo.dao.CrawlerDao;
import com.crawler.demo.model.ConnectionEstablish;
import com.crawler.demo.model.Profiler;

@RestController
public class CrawlerRestController {
	
	@Autowired
	private CrawlerDao crawlerDao;
	
//	
	@RequestMapping("/")
	@ResponseBody
	public String welcome(@RequestBody ConnectionEstablish c) {
		if(crawlerDao.prepareConn("jdbc:mysql://" + c.getHostId() + "/",c.getUser(),c.getPassword())!=null)
			return "Connection has been established";
		else
			return "Connection unsuccessful";
	}
	
	@RequestMapping(value = "/schemas",
			method = RequestMethod.GET,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<String> getSchemas(@RequestBody ConnectionEstablish c){
		return crawlerDao.getSchemas(c);
	}
	
	@RequestMapping(value = "/{schema}",
			method = RequestMethod.GET,
					consumes = {MediaType.APPLICATION_JSON_VALUE},
					produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<String> getTables(@PathVariable("schema") String schema,@RequestBody ConnectionEstablish c){
		return crawlerDao.getTables(schema,c);
	}
	
	@RequestMapping(value = "/{schema}/{table}",
			method = RequestMethod.GET,
					consumes = {MediaType.APPLICATION_JSON_VALUE},
					produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<String> getColumns(@PathVariable("schema") String schema,@PathVariable("table") String table,@RequestBody ConnectionEstablish c){
		return crawlerDao.getColumns(schema,table,c);
	}
	
	@RequestMapping(value = "/{schema}/{table}/profile")
	@ResponseBody
	public Map<String,Profiler> getProfiler(@PathVariable("schema") String schema,@PathVariable("table") String table,@RequestBody ConnectionEstablish c,@RequestParam(value="limit", required = false) Integer limit){
		
		if(limit==null)
			return crawlerDao.getProfiler(schema,table,c,-1);
		else
			return crawlerDao.getProfiler(schema,table,c,limit);
	}
}
