package com.kzsrm.serviceImpl;

import org.springframework.stereotype.Service;

import com.kzsrm.service.TestService;

@Service
public class TestServiceImpl implements TestService {
	
	public String getTimestamp(String param) {
		Long timestamp = System.currentTimeMillis();
		return timestamp.toString();
	}
	
}
