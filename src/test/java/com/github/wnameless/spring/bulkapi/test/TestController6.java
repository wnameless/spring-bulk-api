package com.github.wnameless.spring.bulkapi.test;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.wnameless.spring.bulkapi.Bulkable;

import java.io.IOException;


/**
 * Created by Arash Moghani on 15 Jul 2020
 */

@Bulkable
@RestController
public class TestController6 {

    @SneakyThrows
    @RequestMapping("/exception")
    String exception() {
        throw new IOException();    // Creating error intentionally
    }

}


