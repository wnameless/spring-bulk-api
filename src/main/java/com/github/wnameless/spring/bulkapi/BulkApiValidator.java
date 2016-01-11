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

import static net.sf.rubycollect4j.RubyCollections.ra;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.wnameless.spring.routing.RoutingPath;
import com.github.wnameless.spring.routing.RoutingPathResolver;

import net.sf.rubycollect4j.block.TransformBlock;

/**
 * 
 * {@link BulkApiValidator} can check all bulk request paths and methods whether
 * bulkable or not.
 *
 */
public class BulkApiValidator {

  private final RoutingPathResolver pathRes;

  public BulkApiValidator(ApplicationContext appCtx) {
    Map<String, Object> bulkableBeans =
        appCtx.getBeansWithAnnotation(Bulkable.class);
    List<String> basePackageNames =
        ra(bulkableBeans.values()).map(new TransformBlock<Object, String>() {

          @Override
          public String yield(Object item) {
            return item.getClass().getPackage().getName();
          }

        });

    pathRes = new RoutingPathResolver(appCtx, appCtx.getBean(Environment.class),
        basePackageNames.toArray(new String[basePackageNames.size()]));
  }

  /**
   * Checks if any request path with certain method is bulkable or not.
   * 
   * @param path
   *          a request bulk path
   * @param method
   *          a request bulk method
   * @return true if request path and method is bulkable, false otherwise
   */
  public boolean validatePath(String path, HttpMethod method) {
    RoutingPath rp = pathRes.findByRequestPathAndMethod(path,
        RequestMethod.valueOf(method.toString()));

    if (rp == null) return false;

    return ra(rp.getClassAnnotations())
        .map(new TransformBlock<Annotation, Class<? extends Annotation>>() {

          @Override
          public Class<? extends Annotation> yield(Annotation item) {
            return item.annotationType();
          }

        }).contains(Bulkable.class);
  }

}
