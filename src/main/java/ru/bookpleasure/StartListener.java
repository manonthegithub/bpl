package ru.bookpleasure;

/**
 * Created by Kirill on 01/04/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.bookpleasure.beans.FilesBean;

import javax.servlet.ServletContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    FilesBean filesBean;

    @Autowired
    ServletContext context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        filesBean.loadFilesFromDbAfterDeploy(context.getRealPath(FilesBean.IMAGE_FILES_PATH));
    }

}
