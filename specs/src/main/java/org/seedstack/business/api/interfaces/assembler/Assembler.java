/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api.interfaces.assembler;

import java.lang.reflect.Type;


/**
 * This interface represents the Assembler concepts between an aggregate and a DTO.
 * <p/>
 * This is a helper to transform an aggregate to a DTO and vice versa.
 * <p/>
 * Underlying class will have to provide the gateway implementation.
 *
 * @param <AggregateRoot>     the aggregate root
 * @param <Dto>               the dto type
 * @param <AggregateRootType> the aggregate root class type
 */
public interface Assembler<AggregateRoot, Dto, AggregateRootType extends Type> {


    /**
     * Creates a new DTO and fill it from the aggregate.
     *
     * @param sourceAggregate The aggregate to copy data from.
     * @return a DTO type
     */
    Dto assembleDtoFromAggregate(AggregateRoot sourceAggregate);

    /**
     * Updates the given DTO from the aggregate.
     *
     * @param sourceDto       The dto to update.
     * @param sourceAggregate The aggregate to copy data from.
     */
    void updateDtoFromAggregate(Dto sourceDto, AggregateRoot sourceAggregate);

    /**
     * Merges a source DTO into an existing aggregate.
     *
     * @param targetAggregate The aggregate to merge.
     * @param sourceDto       The dto to copy data from.
     */
    void mergeAggregateWithDto(AggregateRoot targetAggregate, Dto sourceDto);

    /**
     * Returns the aggregate type handled by the assembler.
     *
     * @return Class<AggregateRootType>
     */
    AggregateRootType getAggregateClass();

    /**
     * Returns the DTO type handled by the assembler.
     *
     * @return Class<Dto>
     */
    Class<Dto> getDtoClass();
}
