package ru.bookpleasure.test.unit;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Kirill on 11/01/17.
 */
@ContextConfiguration("/webapp/WEB-INF/bpl-servlet.xml")
@WebAppConfiguration
public class AbstractSpringUnitTest extends AbstractTestNGSpringContextTests {

}
