/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.core.interfaces.assembler.dsl;

import org.seedstack.business.api.interfaces.assembler.dsl.DtoAssemblerProvider;

import java.util.List;

/**
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 */
public class DtoAssemblerProviderImpl implements DtoAssemblerProvider {

    private final DtosAssemblerProviderImpl dtosAssemblerProvider;

    public DtoAssemblerProviderImpl(InternalRegistry registry, AssemblerContext context) {
        this.dtosAssemblerProvider = new DtosAssemblerProviderImpl(registry, context);
    }

    @Override
    public <D> D to(Class<D> dtoClass) {
        List<D> ds = dtosAssemblerProvider.to(dtoClass);
        if (ds != null && !ds.isEmpty()) {
            return ds.get(0);
        } else {
            return null;
        }
    }
}
