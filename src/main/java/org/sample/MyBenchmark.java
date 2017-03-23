/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.*;
import org.sample.domain.OrderItem;
import org.sample.domain.Orderdata;
import org.sample.domain.Product;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(5)
@State(Scope.Benchmark)
public class MyBenchmark {

    private List<Orderdata> orders;

    @Setup
    public void fillOrderdata() {

        Set<OrderItem> orderItems = IntStream.range(1, 100)
                .mapToObj(i -> {
                    Product product = createProduct(i);
                    return createOrderItem(i, product);
                })
                .collect(Collectors.toSet());

        orders = IntStream.range(1, 100)
                .mapToObj(i -> createOrderData(i, orderItems))
                .collect(Collectors.toList());
    }

    private Product createProduct(int i) {
        Product product = new Product();
        product.setId((long) i);
        product.setName(String.valueOf(i));
        return product;
    }

    private OrderItem createOrderItem(int i, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId((long) i);
        orderItem.setQuantity(i);
        orderItem.setProduct(product);
        return orderItem;
    }

    private Orderdata createOrderData(int i, Set<OrderItem> orderItems) {
        Orderdata orderdata = new Orderdata();
        orderdata.setId((long) i);
        orderdata.setItems(orderItems);
        orderdata.setOrderNumber(String.valueOf(i));
        return orderdata;
    }

    @Benchmark
    public void forEachMethod() {
        orders.stream()
                .forEach(orderData -> orderData.getItems().stream()
                        .forEach(item ->
                                someOperation(item.getProduct().getName())));
    }

    @Benchmark
    public void flatMapMethod() {
        orders.stream()
                .flatMap(o -> o.getItems().stream())
                .map(OrderItem::getProduct)
                .map(Product::getName)
                .forEach(this::someOperation);
    }

    private String someOperation(String s){
        return s + s;
    }

}
