/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api;

import com.google.common.collect.Lists;
import com.google.inject.util.Types;
import org.fest.reflect.core.Reflection;
import org.javatuples.*;
import org.seedstack.seed.core.utils.SeedCheckUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Static utility methods to easily create tuples.
 * 
 * @author redouane.loulou@ext.mpsa.com
 */
public final class Tuples {

    private Tuples() {
    }

    /**
     * Transforms a list of object into a tuple. Does not work array of more than ten element.
     *
     * @param objects the list of object
     * @param <TUPLE> the tuple type
     * @return a tuple
     * @throws org.seedstack.seed.core.api.SeedException if the array length is greater than 10
     */
    @SuppressWarnings("unchecked")
    public static <TUPLE extends Tuple> TUPLE  create(List<?> objects) {
        SeedCheckUtils.checkIf(objects.size() <= 10, "Can't create a Tuple of more than ten element.");

        Class<? extends Tuple> tupleClass = classOfTuple(objects.toArray(new Object[objects.size()]));

        return  (TUPLE) Reflection
                .staticMethod("fromCollection")
                .withReturnType(tupleClass)
                .withParameterTypes(Collection.class)
                .in(tupleClass).invoke(objects);
    }

    /**
     * Transforms an array of object into a tuple. Does not work array of more than ten element.
     *
     * @param objects the array of object
     * @param <TUPLE> the tuple type
     * @return a tuple
     * @throws org.seedstack.seed.core.api.SeedException if the array length is greater than 10
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <TUPLE extends Tuple> TUPLE  create(Object firstObject, Object... objects) {
        List list = Lists.newArrayList(firstObject);
        list.addAll(Arrays.asList(objects));
        return (TUPLE) create(list);
    }

    /**
     * Transforms an array of object into a tuple. Does not work array of more than ten element.
     *
     * @param objects the array of object
     * @param <TUPLE> the tuple type
     * @return a tuple
     * @throws org.seedstack.seed.core.api.SeedException if the array length is greater than 10
     * @deprecated Use the shorter {@code create} method instead.
     */
    @Deprecated
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <TUPLE extends Tuple> TUPLE  createTupleFromList(Object... objects) {
        return (TUPLE) create(objects);
    }

    /**
     * Transforms a list of object into a tuple. Does not work array of more than ten element.
     *
     * @param objects the list of object
     * @param <TUPLE> the tuple type
     * @return a tuple
     * @throws org.seedstack.seed.core.api.SeedException if the array length is greater than 10
     * @deprecated Use the shorter {@code create} method instead.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <TUPLE extends Tuple> TUPLE  createTupleFromList(List<Object> objects) {
        return  create(objects);
    }

    public static Class<? extends Tuple> classOfTuple(List<?> objects) {
        return classOfTuple(objects.toArray());
    }

    /**
     * Finds for a list of object the associated tuple class, eg. a {@code Pair} for a list of two object and a {@code Triplet} for a list of three.
     *
     * @param objects the list of objects
     * @return the tuple class
     */
    public static Class<? extends Tuple> classOfTuple(Object... objects) {
        Class<? extends Tuple> tupleClass = null;

        int tupleSize = objects.length;

        switch (tupleSize) {
            case 1:
                tupleClass = Unit.class;
                break;
            case 2:
                tupleClass = Pair.class;
                break;
            case 3:
                tupleClass = Triplet.class;
                break;
            case 4:
                tupleClass = Quartet.class;
                break;
            case 5:
                tupleClass = Quintet.class;
                break;
            case 6:
                tupleClass = Sextet.class;
                break;
            case 7:
                tupleClass = Septet.class;
                break;
            case 8:
                tupleClass = Octet.class;
                break;
            case 9:
                tupleClass = Ennead.class;
                break;
            case 10:
                tupleClass = Decade.class;
                break;
            default:
                break;
        }
        return tupleClass;
    }

    /**
     * Gets the final tuple type for a list of class.
     * <p>
     * For instance, for a list with Customer.class and Order.class the method will return {@literal Pair<Customer, Order>}.
     * </p>
     *
     * @param classes the tuple's classes
     * @return the tuple type
     */
    public static ParameterizedType typeOfTuple(Class<?>... classes) {
        return Types.newParameterizedType(classOfTuple(classes), classes);
    }

    public static <T> List<T> toList(Tuple tuple) {
        List<Object> objects = new ArrayList<Object>(tuple.getSize());
        for (Object o : tuple) {
            objects.add(o);
        }
        //noinspection unchecked
        return (List<T>) objects;
    }

    public static List<?> toListOfClasses(Tuple tuple) {
        List<Class<?>> objects = new ArrayList<Class<?>>(tuple.getSize());
        for (Object o : tuple) {
            objects.add(o.getClass());
        }
        return objects;
    }
}