package ru.bookpleasure.launch;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.HostConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

    public static void main(String[] args) throws Exception {

        String rootPath = new File(".").getCanonicalPath();

        System.out.println(rootPath);


        String catalinaHome = "..";

        Tomcat tomcat = new Tomcat();

        tomcat.setBaseDir(new File(catalinaHome).getAbsolutePath());
        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.getHost().addLifecycleListener(new HostConfig());

        tomcat.start();
        tomcat.getServer().await();
    }
}