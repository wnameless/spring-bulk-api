/*
 *
 * Copyright 2015 Wei-Ming Wu
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
package com.github.wnameless.spring.bulkapi;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * {@link BulkApiConfig} implements the
 * {@link BeanDefinitionRegistryPostProcessor} which helps the user to load
 * {@link BulkApiController} and {@link BulkApiExceptionHandlerAdvice} after
 * adding the {@link EnableBulkApi @EnableBulkApi} annotation to the Web App.
 *
 */
@Configuration
public class BulkApiConfig {

  @Bean
  static BulkApiConfigBean bulkApiConfigBean() {
    return new BulkApiConfigBean();
  }

}
