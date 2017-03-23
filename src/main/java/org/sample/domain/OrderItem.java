package org.sample.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ymolodkov on 23.03.17.
 */

@Data
public class OrderItem  implements Serializable {
    private Long id = 0L;
    private int quantity;
    private Product product;
}
