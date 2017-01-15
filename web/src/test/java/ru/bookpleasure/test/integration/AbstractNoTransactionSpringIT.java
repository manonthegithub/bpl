package ru.bookpleasure.test.integration;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Kirill on 15/01/17.
 */
@ContextConfiguration("/webapp/WEB-INF/bpl-servlet.xml")
@WebAppConfiguration
abstract class AbstractNoTransactionSpringIT extends AbstractTestNGSpringContextTests {
}
