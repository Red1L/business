/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal;

import com.google.common.collect.Lists;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.seedstack.business.domain.AggregateRoot;
import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.business.domain.BaseRepository;
import org.seedstack.business.domain.Repository;
import org.seedstack.business.internal.strategy.api.BindingStrategy;
import org.seedstack.business.repositories.fixtures.MyQualifier;
import org.seedstack.seed.Application;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class DefaultRepositoryCollectorTest {

    private DefaultRepositoryCollector underTest;
    private Application application;
    private TypeLiteral<?> genericInterface = TypeLiteral.get(Types.newParameterizedType(Repository.class, new Type[]{MyAgg.class}));

    @Before
    public void before() {
        application = mock(Application.class);
        underTest = new DefaultRepositoryCollector(
                Lists.<Class<?>>newArrayList(MySubAgg1.class, MySubAgg2.class),
                Lists.<Class<?>>newArrayList(MyDefaultRepo.class),
                application
        );
    }

    @Test
    public void testCollectSuperclasses() throws Exception {
        Collection<BindingStrategy> bindingStrategies = underTest.collect();
        assertThat(((Map<?, ?>) Whitebox.getInternalState(bindingStrategies.iterator().next(), "constructorParamsMap")).size()).isEqualTo(3);
    }

    @Test
    public void testGetDefaultWithQualifierString() {
        Configuration configuration = mock(Configuration.class);
        when(configuration.getString("default-repository")).thenReturn("my-qualifier");

        when(application.getConfiguration(MyAgg.class)).thenReturn(configuration);
        Key<?> key = underTest.defaultRepositoryQualifier(MyAgg.class, genericInterface);

        assertThat(key.getAnnotation()).isEqualTo(Names.named("my-qualifier"));
    }

    @Test
    public void testGetDefaultWithQualifierAnnotation() {
        Configuration configuration = mock(Configuration.class);
        when(configuration.getString("default-repository")).thenReturn("org.seedstack.business.repositories.fixtures.MyQualifier");

        when(application.getConfiguration(MyAgg.class)).thenReturn(configuration);
        Key<?> key = underTest.defaultRepositoryQualifier(MyAgg.class, genericInterface);

        assertThat(key.getAnnotationType()).isEqualTo(MyQualifier.class);
    }

    private static class MyAgg extends BaseAggregateRoot<Long> {
        @Override
        public Long getEntityId() {
            return null;
        }
    }

    private static class MySubAgg1 extends MyAgg {
    }

    private static class MySubAgg2 extends MyAgg {
    }

    public class MyDefaultRepo<A extends AggregateRoot<K>, K> extends BaseRepository<A, K> {
        @Override
        public A load(K id) {
            return null;
        }

        @Override
        public boolean exists(K id) {
            return false;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void clear() {

        }

        @Override
        public void delete(K id) {

        }

        @Override
        public void delete(A aggregate) {

        }

        @Override
        public void persist(A aggregate) {

        }

        @Override
        public A save(A aggregate) {
            return null;
        }
    }
}
