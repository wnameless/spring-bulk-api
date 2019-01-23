/*
 *
 * Copyright 2016 Wei-Ming Wu
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

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.wnameless.spring.bulkapi.BulkApiService;
import com.github.wnameless.spring.bulkapi.DefaultBulkApiService;
import com.github.wnameless.spring.bulkapi.EnableBulkApi;
import com.github.wnameless.spring.bulkapi.URITransformer;

@EnableBulkApi
@Configuration
public class AppConfig {

  @Autowired
  @Bean
  public BulkApiService bulkApiService(ApplicationContext appCtx) {
    return new CostumBulkApiService(appCtx);
  }

  public class CostumBulkApiService extends DefaultBulkApiService {

    public CostumBulkApiService(ApplicationContext appCtx) {
      super(appCtx);
    }

  }

  @Bean
  public TestURITransformer uriTransformer() {
    return new TestURITransformer();
  }

  public class TestURITransformer implements URITransformer {

    private boolean used = false;

    @Override
    public URI transform(URI uri) {
      used = true;
      return uri;
    }

    public boolean isUsed() {
      return used;
    }

  }

}
