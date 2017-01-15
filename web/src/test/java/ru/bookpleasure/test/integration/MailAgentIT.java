package ru.bookpleasure.test.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.bookpleasure.MailAgent;

/**
 * Created by Kirill on 15/01/17.
 */
public class MailAgentIT extends AbstractNoTransactionSpringIT {

    @Autowired
    MailAgent mailAgent;


    @Test(enabled = false)
    public void mailTest() throws Exception {
        mailAgent.sendMailFromRobot("robot@bookpleasure.ru", "Hello", "You got it!");
    }

}
