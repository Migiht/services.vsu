package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector(); 

        String webappDirLocation = "src/main/webapp/";
        Context context = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());

        
        File additionWebInfClasses = new File("target/classes");
        context.setAllowCasualMultipartParsing(true);
        org.apache.catalina.WebResourceRoot resources = new org.apache.catalina.webresources.StandardRoot(context);
        resources.addPreResources(new org.apache.catalina.webresources.DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        
        context.addWelcomeFile("index.jsp");

        
        

        
        ErrorPage errorPage = new ErrorPage();
        errorPage.setErrorCode(500);
        errorPage.setLocation("/error.jsp"); 
        context.addErrorPage(errorPage);

        tomcat.start();
        tomcat.getServer().await();
    }
} 
