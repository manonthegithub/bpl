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
import ru.bookpleasure.beans.FilesService;

@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    FilesService filesService;

    /**
     * Загружаем файлы картинок из базы на старте
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        filesService.loadFilesFromDbAfterDeploy();
    }

}
