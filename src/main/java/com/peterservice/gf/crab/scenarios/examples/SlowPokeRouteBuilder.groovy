package com.peterservice.gf.crab.scenarios.examples

import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlowPokeRouteBuilder extends RouteBuilder {
    Logger logger = LoggerFactory.getLogger(SlowPokeRouteBuilder.class)


    @Override
    def void configure() throws Exception {

        from("crab:slowpoke").id("slowpokeRoute")
                .process(new Processor() {
            @Override
            void process(Exchange exchange) throws Exception {
                Message inMessage = exchange.in;
               // exchange.setProperty("ps.crab.context.waittime", inMessage.getHeader('waittime', String.class));
               // Message inMessage = exchange.in;
                inMessage.setHeader(Exchange.HTTP_URI, "http://srv5-fombell:10105/" + inMessage.getHeader('operation', String.class) + "?LOGIN=HEX_ADMIN&PASSWORD=1111");
                inMessage.setHeader(Exchange.HTTP_METHOD, 'GET');
                inMessage.setHeader(Exchange.CONTENT_TYPE, 'application/xml');
            }
        })
               // .delay(exchangeProperty("ps.crab.context.waittime"))
                .to("http4://hexhost")
                .setHeader("ps.crab.result.description", simple("body hex: \${body}"))
                .setHeader("ps.crab.description", simple("body hex: \${body}"))
                .to("crab:success")

    }
}
