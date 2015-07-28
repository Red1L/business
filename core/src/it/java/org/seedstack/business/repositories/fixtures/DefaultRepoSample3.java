/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.repositories.fixtures;

import com.google.inject.assistedinject.Assisted;
import org.seedstack.business.api.domain.AggregateRoot;
import org.seedstack.business.api.domain.BaseRepository;
import org.seedstack.business.spi.GenericImplementation;

import javax.inject.Inject;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
@GenericImplementation
@MyQualifier
public class DefaultRepoSample3<A extends AggregateRoot<K>, K> extends BaseRepository<A,K> {

    @Inject
    public DefaultRepoSample3(@Assisted Object[] genericClasses) {
        super((Class)genericClasses[0], (Class)genericClasses[1]);
    }

    @Override
    protected A doLoad(K id) {
        return null;
    }

    @Override
    protected void doDelete(A a) {
    }

    @Override
    protected void doPersist(A a) {
    }

    @Override
    protected A doSave(A a) {
        return null;
    }
}
