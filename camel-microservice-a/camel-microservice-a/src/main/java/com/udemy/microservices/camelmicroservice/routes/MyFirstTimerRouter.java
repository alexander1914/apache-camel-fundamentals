package com.udemy.microservices.camelmicroservice.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingProcessingComponent;

    @Override
    public void configure() throws Exception {
        // timer
        // transformation
        // log
        // TODO: This a settings to data input and output.
        from("timer:first-timer")
                .log("${body}")
                .transform().constant("My Contast Message")
                .log("${body}")
                //.transform().constant("Time now is " + LocalDateTime.now())

                // Processing
                // Transformation
                .bean(getCurrentTimeBean)
                .log("${body}")
                .bean(loggingProcessingComponent)
                .log("${body}")
                .process(new SimpleLoggingProcessor())
                .to("log:first-timer");//database

    }
}

@Component
class GetCurrentTimeBean {
    public String getCurrentTime(){
        return "Time now is: " + LocalDateTime.now();
    }
}
@Component
class SimpleLoggingProcessingComponent {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    public void process(String message){
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}

class SimpleLoggingProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLoggingProcessingComponent {}",
                exchange.getMessage().getBody());
    }
}