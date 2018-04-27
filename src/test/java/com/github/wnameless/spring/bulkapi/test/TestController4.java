/*
 *
 * Copyright 2018 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.github.wnameless.spring.bulkapi.test;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.wnameless.spring.bulkapi.Bulkable;

@Bulkable
@RestController
public class TestController4 {

  public static class MyInnerInfos {
    String c;

    public MyInnerInfos() {}
  }

  public static class MyInfos {
    MyInnerInfos a;
    String b;

    public MyInfos() {}
  }

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public String list(@RequestBody MyInfos infos) {
    return "list";
  }

}