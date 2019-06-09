package com.kalvin.kvf;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 作用：junit测试基类<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年04月29日 15:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class BaseJunitTest {
    protected final static Logger LOGGER = LoggerFactory.getLogger(BaseJunitTest.class);
}
