package com.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class DiscoveryServiceConsumerApplication {

	//can use both the discoveryClient or eurekaClient
	//need to autowire it or inject it too via @Inject
	@Autowired
	private EurekaClient client;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceConsumerApplication.class, args);
	}
	
	@RequestMapping(value="/")
	public String callService() {
		RestTemplate template = restTemplateBuilder.build();
		InstanceInfo instanceInfo = client.getNextServerFromEureka("serviceProvider", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		ResponseEntity<String> response = template.exchange(baseUrl, HttpMethod.GET, null, String.class);
		
		return response.getBody();
	}
}
