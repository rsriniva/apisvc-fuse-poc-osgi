package com.redhat.fuse.demo.apisvc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.bashburn.apimanager.InputApiRequest;

public class ExtractHeaderPropertiesProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    InputApiRequest body = exchange.getIn().getBody(InputApiRequest.class);
    for(InputApiRequest.RequestProperties props : body.getRequestProperties()) {
      exchange.getIn().setHeader(props.getKey(), props.getValue());
    }

  }

}
