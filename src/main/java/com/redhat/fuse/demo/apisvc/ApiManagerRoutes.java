/**
 * 
 */
package com.redhat.fuse.demo.apisvc;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.bashburn.apimanager.InputApiRequest;
import com.redhat.bashburn.apimanager.OutputApiRequest;

/**
 * @author bashburn
 *
 */
public class ApiManagerRoutes extends RouteBuilder {
  private static final Logger log = LoggerFactory.getLogger(ApiManagerRoutes.class);
  
  private ExtractHeaderPropertiesProcessor extractProps;
  private AggregationStrategy wsdlAggregationStrategy;
  private String appKeyValidationServicePath;

  /* (non-Javadoc)
   * @see org.apache.camel.builder.RouteBuilder#configure()
   */
  @Override
  public void configure() throws Exception {
    OutputApiRequest successOutput = new OutputApiRequest();
    successOutput.setResult("SUCCESS");
    
    OutputApiRequest failureOutput = new OutputApiRequest();
    failureOutput.setResult("FAILURE");
    
    from("cxf:bean:testservice")
      .convertBodyTo(InputApiRequest.class)
      .process(extractProps)
      .wireTap("log:com.redhat.fuse.demo?level=INFO&showAll=true")
      .choice()
        .when(header("http.query.string").isEqualTo("wsdl"))
          .pollEnrich(createWsdlExtractUri(header("http.request.path").toString()), wsdlAggregationStrategy)
        .when(header("http.request.path").isEqualTo(appKeyValidationServicePath))
          .to("log:com.redhat.fuse.demo?level=INFO&showAll=true")
        .otherwise()
          .to("log:com.redhat.fuses.demo?level=INFO&showAll=true")
    ;

  }

  private String createWsdlExtractUri(String requestPath) {
    log.info("Request Path for extracting URI [{}]", requestPath);
    return "log:com.redhat.fuse.demo?level=INFO&showAll=true";
  }

  public ExtractHeaderPropertiesProcessor getExtractProps() {
    return extractProps;
  }
  public void setExtractProps(ExtractHeaderPropertiesProcessor extractProps) {
    this.extractProps = extractProps;
  }
  public AggregationStrategy getWsdlAggregationStrategy() {
    return wsdlAggregationStrategy;
  }
  public void setWsdlAggregationStrategy(AggregationStrategy wsdlAggregationStrategy) {
    this.wsdlAggregationStrategy = wsdlAggregationStrategy;
  }
  public String getAppKeyValidationServicePath() {
    return appKeyValidationServicePath;
  }
  public void setAppKeyValidationServicePath(String appKeyValidationServicePath) {
    this.appKeyValidationServicePath = appKeyValidationServicePath;
  }
}
