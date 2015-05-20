/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api.interfaces.assembler.dsl;

import org.javatuples.Tuple;

/**
 * Specifies whether the aggregate root tuple should be retrieved from a repository or created from a factory.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
<<<<<<< HEAD
=======
 */
<<<<<<< HEAD
>>>>>>> rename author to respect javadoc format
 */
=======
>>>>>>> Change default assembler behavior
public interface TupleAggAssemblerWithRepoProvider<T extends Tuple> {

    /**
     * Loads the aggregate root tuple from their repository.
     * <p>
     * It uses the {@link org.seedstack.business.api.interfaces.assembler.MatchingEntityId} annotation on
     * the DTO to find the aggregate IDs.
     * </p>
     *
     * @return next DSL element
     */
    TupleAggAssemblerWithRepoAndFactProvider<T> fromRepository();

    /**
     * Create the aggregate root tuple from their factory.
     * <p>
     * It uses the {@link org.seedstack.business.api.interfaces.assembler.MatchingFactoryParameter} annotation on
     * the DTO to find the factory method parameters.
     * </p>
     *
     * @return the list of aggregate tuple
     */
    T fromFactory();
}
