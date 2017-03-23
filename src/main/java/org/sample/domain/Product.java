package org.sample.domain;

/**
 * @author ymolodkov on 23.03.17.
 */

import lombok.Data;

import java.io.Serializable;


@Data
public class Product implements Serializable {
    private Long id = 0L;
    private String name;
}
