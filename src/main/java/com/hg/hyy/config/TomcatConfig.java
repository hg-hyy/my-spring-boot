package com.hg.hyy.config;

import org.apache.catalina.Context;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// import org.apache.catalina.connector.Connector;
// import org.apache.tomcat.util.descriptor.web.SecurityCollection;
// import org.apache.tomcat.util.descriptor.web.SecurityConstraint;

@Configuration
public class TomcatConfig {
    // @Bean
    // public ServletWebServerFactory servletContainer() {
    // TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
    // @Override
    // protected void postProcessContext(Context context) {
    // SecurityConstraint constraint = new SecurityConstraint();
    // constraint.setUserConstraint("CONFIDENTIAL");
    // SecurityCollection collection = new SecurityCollection();
    // collection.addPattern("/*");
    // constraint.addCollection(collection);
    // context.addConstraint(constraint);
    // }
    // };
    // tomcat.addAdditionalTomcatConnectors(createSslConnector());

    // return tomcat;
    // }
    // private Connector createSslConnector() {
    // Connector connector = new
    // Connector("org.apache.coyote.http11.Http11NioProtocol");
    // connector.setScheme("http");
    // // Connector监听的http的端口号
    // connector.setPort(8090);
    // connector.setSecure(false);
    // // 监听到http的端口号后转向到的https的端口号
    // connector.setRedirectPort(8443);
    // return connector;
    // }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers((connector -> {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            connector.setScheme("https");
            // Connector监听的http的端口号
            connector.setPort(8080);
            connector.setSecure(false);
            // 监听到http的端口号后转向到的https的端口号
            connector.setRedirectPort(80);
            protocol.setMaxThreads(200);
            protocol.setMaxConnections(1000);
            // protocol这个对象中还有很多tomcat的参数可以设置，读者可以自己了解了解。
        }));

        /*
         * tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
         *
         * @Override public void customize(Connector connector) { Http11NioProtocol
         * protocol = (Http11NioProtocol) connector.getProtocolHandler();
         * protocol.setMaxThreads(200); protocol.setMaxConnections(1000); } });
         */

        return tomcat;
    }

    // 创建wss协议接口
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addServletContainerInitializer(new WsSci(), null);
            }

        };
    }

}
