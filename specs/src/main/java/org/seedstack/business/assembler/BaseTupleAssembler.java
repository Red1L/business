/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.assembler;

import org.javatuples.Tuple;
import org.seedstack.seed.core.utils.SeedReflectionUtils;

import java.lang.reflect.ParameterizedType;

/**
 * This class is used by developers as bases for Tuple based assemblers.
 *
 * @param <T> the tuple type for this assembler.
 * @param <D> the actual dto type.
 * @author epo.jemba@ext.mpsa.com
 */
public abstract class BaseTupleAssembler<T extends Tuple, D> extends AbstractBaseAssembler<T, D> {

    /**
     * Default needed constructor. Initialize internal private fields {@code TupleType aggregateClasses}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BaseTupleAssembler() {
        Class<? extends BaseTupleAssembler> class1 = (Class<? extends BaseTupleAssembler>) SeedReflectionUtils.cleanProxy(getClass());

        dtoClass = (Class<D>) ((ParameterizedType) class1.getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * This method is used by developers or by {@link org.seedstack.business.assembler.FluentAssembler}
     * to assemble a new DTO from the given aggregate.
     * <ul>
     * <li>It calls {@link #newDto()} for the DTO creation;</li>
     * <li>and {@link #doAssembleDtoFromAggregate(Object, org.javatuples.Tuple)} for the assembly algorithm.</li>
     * </ul>
     *
     * @param sourceAggregate The aggregate from which create the DTO.
     * @return the assembled dto
     * @see Assembler#assembleDtoFromAggregate(Object)
     */
    @Override
    public D assembleDtoFromAggregate(T sourceAggregate) {
        D newDto = newDto();
        doAssembleDtoFromAggregate(newDto, sourceAggregate);

        return newDto;
    }

    @Override
    public void assembleDtoFromAggregate(D sourceDto, T sourceAggregate) {
        doAssembleDtoFromAggregate(sourceDto, sourceAggregate);
    }

    /**
     * This method is used by developers or by {@link org.seedstack.business.assembler.FluentAssembler}
     * to actually merge the aggregate.
     * <p>
     * It will call {@link #doMergeAggregateWithDto(org.javatuples.Tuple, Object)}, which is overridden by developers.
     * </p>
     *
     * @param targetAggregate the aggregate to merge
     * @param sourceDto       the dto to copy data from
     * @see Assembler#mergeAggregateWithDto(Object, Object)
     */
    @Override
    public void mergeAggregateWithDto(T targetAggregate, D sourceDto) {
        doMergeAggregateWithDto(targetAggregate, sourceDto);
    }

    /**
     * This method has to be overridden by users to actually assembling the DTO from the aggregate.
     * <pre>
     * targetDto.fillProductId(sourceAggregate.getEntityId().getStoreId(),
     *     sourceAggregate.getEntityId().getProductCode());
     * targetDto.setName(sourceAggregate.getName());
     * targetDto.setDescription(sourceAggregate.getDescription());
     * </pre>
     * <p>
     * This method will be called by the public method {@link #assembleDtoFromAggregate(org.javatuples.Tuple)}
     * </p>
     *
     * @param targetDto       the dto to assemble
     * @param sourceAggregate the aggregate to copy data from
     */
    protected abstract void doAssembleDtoFromAggregate(D targetDto, T sourceAggregate);

    /**
     * This method has to be overridden by users to actually merge an aggregate with the DTO.
     * <pre>
     * targetAggregate.setName(sourceDto.getName());
     * targetAggregate.setDescription(sourceDto.getDescription());
     * </pre>
     * This method will be called by the public method
     * {@link #mergeAggregateWithDto(org.javatuples.Tuple, Object)}
     *
     * @param targetAggregate the aggregate to merge
     * @param sourceDto       the dto to copy data from
     */
    protected abstract void doMergeAggregateWithDto(T targetAggregate, D sourceDto);

}
