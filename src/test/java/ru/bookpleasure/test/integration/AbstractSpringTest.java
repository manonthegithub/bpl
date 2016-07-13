package ru.bookpleasure.test.integration;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Kirill on 11/07/16.
 */
@ContextConfiguration("/webapp/WEB-INF/bpl-servlet.xml")
@WebAppConfiguration
public abstract class AbstractSpringTest extends AbstractTransactionalTestNGSpringContextTests {

}
