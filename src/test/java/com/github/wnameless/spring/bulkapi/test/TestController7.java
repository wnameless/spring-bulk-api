package com.github.wnameless.spring.bulkapi.test;

import com.github.wnameless.spring.bulkapi.Bulkable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Arash Moghani on 22 Jul 2020
 */

@Bulkable
@RestController
public class TestController7 {

    @RequestMapping("/getwithparameter")
    String getWithParameter(@RequestParam("abc") String param) {
        System.out.println(param);
        return param;

    }

}


