/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.assembler.dsl;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.api.domain.AggregateRoot;
import org.seedstack.business.api.interfaces.assembler.Assembler;
import org.seedstack.business.api.interfaces.assembler.BaseAssembler;
import org.seedstack.business.api.interfaces.assembler.DtoOf;
import org.seedstack.business.assembler.fixtures.MyAggregateRoot;
import org.seedstack.business.assembler.fixtures.MyAssembler;
import org.seedstack.business.assembler.fixtures.MyUnrestrictedDto;
import org.seedstack.business.core.interfaces.AutomaticAssembler;
import org.seedstack.business.core.interfaces.DefaultAssembler;
import org.seedstack.business.core.interfaces.assembler.dsl.InternalRegistry;
import org.seedstack.business.core.interfaces.assembler.dsl.fixture.AutoAssembler;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;

/**
 * Tests the DSL internal registry.
 *
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 */
@RunWith(SeedITRunner.class)
public class InternalRegistryIT {

    @Inject
    private InternalRegistry registry;

    @Inject
    private Assembler<MyAggregateRoot, MyUnrestrictedDto> expectedAssembler;

    @Inject
    private AutomaticAssembler<Order, OrderDto> expectedAutomaticAssembler;

    @Test
    public void testAssemblerOfWithProvidedAssembler() {
        Assembler<?, ?> assembler = registry.assemblerOf(MyAggregateRoot.class, MyUnrestrictedDto.class);
        Assertions.assertThat(assembler).isNotNull();
        Assertions.assertThat(assembler).isInstanceOf(BaseAssembler.class);
        Assertions.assertThat(assembler).isInstanceOf(MyAssembler.class);

        Assertions.assertThat(expectedAssembler).isNotNull();
        Assertions.assertThat(assembler.getClass()).isEqualTo(expectedAssembler.getClass());
    }

    static class Order implements AggregateRoot<String> {
        @Override
        public String getEntityId() { return null; }
    }

    @DtoOf(Order.class)
    static class OrderDto  { }

    @Test
    public void testAssemblerOfWithDefaultAssembler() {
        Assembler<?, ?> assembler = registry.assemblerOf(Order.class, OrderDto.class);
        Assertions.assertThat(assembler).isNotNull();
        Assertions.assertThat(assembler).isInstanceOf(AutomaticAssembler.class);
        Assertions.assertThat(assembler).isInstanceOf(DefaultAssembler.class);

        Assertions.assertThat(expectedAutomaticAssembler).isNotNull();
        Assertions.assertThat(assembler.getClass()).isEqualTo(expectedAutomaticAssembler.getClass());
    }

    @Test
    public void testAssemblerOfWithAutomaticAssembler() {
        Assembler<?, ?> assembler = registry.assemblerOf(org.seedstack.business.core.interfaces.assembler.dsl.fixture.Order.class
                , org.seedstack.business.core.interfaces.assembler.dsl.fixture.OrderDto.class);
        Assertions.assertThat(assembler).isNotNull();
        Assertions.assertThat(assembler).isInstanceOf(AutomaticAssembler.class);
        Assertions.assertThat(assembler).isNotInstanceOf(DefaultAssembler.class);

        Assertions.assertThat(assembler).isInstanceOf(AutoAssembler.class);
    }
}
