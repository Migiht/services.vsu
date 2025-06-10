package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector(); // Must be called for the port to be applied

        String webappDirLocation = "src/main/webapp/";
        Context context = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());

        // To enable annotation scanning for servlets
        File additionWebInfClasses = new File("target/classes");
        context.setAllowCasualMultipartParsing(true);
        org.apache.catalina.WebResourceRoot resources = new org.apache.catalina.webresources.StandardRoot(context);
        resources.addPreResources(new org.apache.catalina.webresources.DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        // Add a default welcome file
        context.addWelcomeFile("index.jsp");

        // Optional: Add a simple index.jsp to redirect to the projects list
        // I will create this file next.

        // Optional: Configure an error page
        ErrorPage errorPage = new ErrorPage();
        errorPage.setErrorCode(500);
        errorPage.setLocation("/error.jsp"); // I will create this file next.
        context.addErrorPage(errorPage);

        tomcat.start();
        tomcat.getServer().await();
    }
} 