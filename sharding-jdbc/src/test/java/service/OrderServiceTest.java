package service;

import com.person.demo.JdbcApplication;
import com.person.demo.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

/**
 * Created by Darren on 2018-07-17
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JdbcApplication.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void saveInfo(){
        orderService.saveInfo();
    }

    @Test
    public void weakTraction(){
        try {
            orderService.weakTraction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void flexibleTractionInfo(){
        try {
            orderService.flexibleTractionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllItem(){
        orderService.getAllItem();
    }
}
