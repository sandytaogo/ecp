/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.sandy.ecp.framework.domain.Sort.Order;
import com.sandy.ecp.framework.util.StringUtil;
/**
 * 排序显示对象
 * @author Sandy
 * @date 2022-04-24 12:12:12
 * @since 1.0.0 
 */
public class Sort implements Iterable<Order>, Serializable
{
    private static final long serialVersionUID = -7779141228578267275L;
    public static final Direction DEFAULT_DIRECTION;
    private final List<Order> orders;
    
    protected Sort() {
        this(new String[] { " " });
    }
    
    public Sort(final Order... orders) {
        this(Arrays.asList(orders));
    }
    
    public Sort(final List<Order> orders) {
        if (null == orders || orders.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }
        this.orders = orders;
    }
    
    public Sort(final String... properties) {
        this(Sort.DEFAULT_DIRECTION, properties);
    }
    
    public Sort(final Direction direction, final String... properties) {
        this(direction, (properties == null) ? new ArrayList<String>() : Arrays.asList(properties));
    }
    
    public Sort(final Direction direction, final List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
        this.orders = new ArrayList<Order>(properties.size());
        for (final String property : properties) {
            this.orders.add(new Order(direction, property));
        }
    }
    
    public boolean add(Order e) {
    	return orders.add(e);
    }
    
    public Order getOrderFor(final String property) {
        for (final Order order : this) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }
        return null;
    }
    
    @Override
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Sort)) {
            return false;
        }
        final Sort that = (Sort)obj;
        return this.orders.equals(that.orders);
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.orders.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("Sort [orders=%s]", this.orders);
    }
    
    static {
        DEFAULT_DIRECTION = Direction.ASC;
    }
    
    public enum Direction
    {
        ASC, 
        DESC;
        
        public static Direction fromString(final String value) {
            try {
                return valueOf(value.toUpperCase(Locale.US));
            }
            catch (Exception e) {
                throw new IllegalArgumentException(String.format("Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }
        
        public static Direction fromStringOrNull(final String value) {
            try {
                return fromString(value);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
    
    public static class Order implements Serializable
    {
        private static final long serialVersionUID = -2924541152535686768L;
        private final Direction direction;
        private final String property;
        
        protected Order() {
            this(" ");
        }
        
        public Order(final String property) {
            this(Sort.DEFAULT_DIRECTION, property);
        }
        
        public Order(final Direction direction, final String property) {
            if (StringUtil.isEmpty(property)) {
                throw new IllegalArgumentException("Property must not null or empty!");
            }
            this.direction = ((direction == null) ? Sort.DEFAULT_DIRECTION : direction);
            this.property = property;
        }
        
        public Direction getDirection() {
            return this.direction;
        }
        
        public String getProperty() {
            return this.property;
        }
        
        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + this.direction.hashCode();
            result = 31 * result + this.property.hashCode();
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Order)) {
                return false;
            }
            final Order that = (Order)obj;
            return this.direction.equals(that.direction) && this.property.equals(that.property);
        }
        
        @Override
        public String toString() {
            return String.format("%s: %s", this.property, this.direction);
        }
    }
}
