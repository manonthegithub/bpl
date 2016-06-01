package ru.bookpleasure;

/**
 * Created by Kirill on 01/04/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bookpleasure.beans.FilesBean;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Component
public class StartStopListener implements ServletContextListener {

//    @Autowired
//    FilesBean fb;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Hellooo! Listener Starting!!!");
//        fb.loadFilesFromDbAfterDeploy(
//                        servletContextEvent.
//                                getServletContext().
//                                getRealPath(FilesBean.IMAGE_FILES_PATH));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
