package com.github.wnameless.spring.bulkapi;

import java.net.URI;

/**
 * 
 * {@link URITransformer} is designed to have more controls on request
 * {@link URI}s. Every request {@link URI} computed by each bulk request can be
 * altered by providing a Spring Bean which implements this interface.<br>
 * <br>
 * For example:
 * 
 * <pre>
 * {@literal @Bean}
 * public URITransformer uriTransformer() {
 * 
 *   return new URITransformer() {
 *   
 *     {@literal @Override}
 *     public URI transform(URI uri) {
 *       // Some actions with uri..
 *       return uri;
 *     }
 *
 * };
 * </pre>
 *
 */
public interface URITransformer {

  /**
   * Transforms the given {@link URI}.
   * 
   * @param uri
   *          to be transformed
   * @return transformed {@link URI}
   */
  public URI transform(URI uri);

}
