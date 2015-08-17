/**
 *
 * @author Wei-Ming Wu
 *
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * {@link EnableBulkApi @EnableBulkApi} should be used with Spring
 * {@link org.springframework.context.annotation.Configuration @Configuration}.
 * It enables the bulk request support for any Spring RESTful APIs.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan("com.github.wnameless.spring.bulkapi")
public @interface EnableBulkApi {}
