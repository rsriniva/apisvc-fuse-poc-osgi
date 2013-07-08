package com.redhat.fuse.demo.apisvc;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.redhat.bashburn.apimanager.ApiRequestEndpoint;
import com.redhat.bashburn.apimanager.InputApiRequest;
import com.redhat.bashburn.apimanager.OutputApiRequest;

public class ApiManagerRoutesClientTest extends CamelSpringTestSupport {
  private static final String URL = "http://localhost:{{port}}/cxf/apisvc-fuse-poc-osgi/webservices/apirequest";
  
  @BeforeClass
  public static void setupFreePort() throws Exception {
    int port = AvailablePortFinder.getNextAvailable();
    String s = "port=" + port;
    File custom = new File("target/custom.properties");
    FileOutputStream fos = new FileOutputStream(custom);
    fos.write(s.getBytes());
    fos.close();
  }
  
  protected static ApiRequestEndpoint createCxfEndpoint(String url) {
    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
    factory.setServiceClass(ApiRequestEndpoint.class);
    factory.setAddress(url);
    return (ApiRequestEndpoint)factory.create();
  }

  @Override
  protected AbstractApplicationContext createApplicationContext() {
    return new ClassPathXmlApplicationContext(new String[] {"camel-context.xml"});
  }

  @Test
  public void testApiRequest() throws Exception {
    InputApiRequest input = new InputApiRequest();
    input.setApiKey("TEST_KEY");
    input.setApplicationName("TEST_APPLICATION");
    input.setApplicationOwnerId("TEST_OWNER_ID");
    InputApiRequest.RequestProperties prop = new InputApiRequest.RequestProperties();
    prop.setKey("http.query.string");
    prop.setValue("wsdl");
    input.getRequestProperties().add(prop);
    
    String url = context.resolvePropertyPlaceholders(URL);
    ApiRequestEndpoint endpoint = createCxfEndpoint(url);
    OutputApiRequest output = endpoint.apiRequest(input);
    
    assertEquals("SOME CRAZY RESULT", output.getResult());
  }
}
