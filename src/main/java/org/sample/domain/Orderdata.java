package org.sample.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ymolodkov on 23.03.17.
 */
@Data
public class Orderdata implements Serializable {

    private Long id = 0L;
    private String orderNumber;
    private Set<OrderItem> items = new HashSet<>();

}

