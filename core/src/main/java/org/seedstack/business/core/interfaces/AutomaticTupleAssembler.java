/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.core.interfaces;

import net.jodah.typetools.TypeResolver;
import org.javatuples.Tuple;
import org.modelmapper.ModelMapper;
import org.seedstack.business.api.interfaces.assembler.AbstractBaseAssembler;
import org.seedstack.business.api.interfaces.assembler.BaseTupleAssembler;
import org.seedstack.business.api.Tuples;
import org.seedstack.seed.core.utils.SeedReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This assembler automatically assembles aggregates in DTO and vice versa.
 *
 * @param <T> the tuple
 * @param <D> the dto
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 *         24/02/2015
 */
public abstract class AutomaticTupleAssembler<T extends Tuple, D> extends AbstractBaseAssembler<T, D> {

    protected ParameterizedType aggregateClasses;
    protected Class<D> dtoClass;
    private ModelMapper assembleModelMapper;
    private ModelMapper mergeModelMapper;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AutomaticTupleAssembler() {
        assembleModelMapper = configureAssembly();
        mergeModelMapper = configureMerge();
        // TODO <pith> : check modelmappers are not null

        Class<? extends BaseTupleAssembler> class1 = (Class<? extends BaseTupleAssembler>) SeedReflectionUtils.cleanProxy(getClass());
        ParameterizedType p1 = (ParameterizedType) class1.getGenericSuperclass();
        // descendant of tuple.
        ParameterizedType tupleType = (ParameterizedType) p1.getActualTypeArguments()[0];

        Type rawTupleType = tupleType.getRawType();
        Class<? extends Tuple> tupleClass = (Class<? extends Tuple>) rawTupleType;
        Type[] actualTypeArguments = tupleType.getActualTypeArguments();
        int length = actualTypeArguments.length;
        Class[] aggregateClassesArray = new Class[length];

        System.arraycopy(actualTypeArguments, 0, aggregateClassesArray, 0, length);

        aggregateClasses = Tuples.typeOfTuple(aggregateClassesArray);

        dtoClass = (Class<D>) TypeResolver.resolveRawArguments(class1.getGenericSuperclass(), class1)[1];
    }

    public AutomaticTupleAssembler(ParameterizedType aggregateClasses, Class<D> dtoClass) {
        this.aggregateClasses = aggregateClasses;
        this.dtoClass = dtoClass;

        assembleModelMapper = configureAssembly();
        mergeModelMapper = configureMerge();
    }

    @Override
    public D assembleDtoFromAggregate(T sourceAggregate) {
        D sourceDto = null;

        for (Object o : sourceAggregate) {
            if (sourceDto == null) {
                sourceDto = assembleModelMapper.map(o, dtoClass);
            }
            assembleModelMapper.map(o, sourceDto);

        }
        return sourceDto;
    }

    @Override
    public void updateDtoFromAggregate(D sourceDto, T sourceAggregate) {
        for (Object o : sourceAggregate) {
            assembleModelMapper.map(o, sourceDto);
        }
    }

    @Override
    public void mergeAggregateWithDto(T targetAggregate, D sourceDto) {
        for (Object o : targetAggregate) {
            mergeModelMapper.map(sourceDto, o);
        }
    }

    protected abstract ModelMapper configureAssembly();

    protected abstract ModelMapper configureMerge();
}
