package com.redhat.fuse.demo.apisvc;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.bashburn.apimanager.OutputApiRequest;

public class WsdlAggregationStrategy implements AggregationStrategy {
  private static final Logger log = LoggerFactory.getLogger(WsdlAggregationStrategy.class);

  @Override
  public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
    OutputApiRequest output = new OutputApiRequest();
    output.setResult("SOME CRAZY RESULT");
    newExchange.getOut().setBody(output, OutputApiRequest.class);
    return newExchange;
  }

}
