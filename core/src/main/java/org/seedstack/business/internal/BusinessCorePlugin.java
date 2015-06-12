/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.inject.Key;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import io.nuun.kernel.core.AbstractPlugin;
import org.kametic.specifications.Specification;
import org.seedstack.business.api.Producible;
import org.seedstack.business.api.domain.AggregateRoot;
import org.seedstack.business.api.domain.DomainObject;
import org.seedstack.business.api.domain.Factory;
import org.seedstack.business.api.domain.Repository;
import org.seedstack.business.api.interfaces.assembler.Assembler;
import org.seedstack.business.api.specifications.DomainSpecifications;
import org.seedstack.business.core.domain.FactoryInternal;
import org.seedstack.business.internal.strategy.FactoryPatternBindingStrategy;
import org.seedstack.business.internal.strategy.GenericBindingStrategy;
import org.seedstack.business.internal.strategy.api.BindingStrategy;
import org.seedstack.business.internal.strategy.api.ProviderFactory;
import org.seedstack.seed.core.utils.SeedBindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;

import static org.seedstack.business.api.specifications.BaseClassSpecifications.classIsAbstract;
import static org.seedstack.business.api.specifications.BaseClassSpecifications.classIsInterface;

/**
 * This plugin is a multi round plugin.
 * <p/>
 * It uses two round because it needs to scan user interfaces, for instance those annotated with {@code @Finder}.
 * Then in the second round, it scan the implementations of the scanned interfaces.
 * <p/>
 * This plugin also bind default implementation for repository, factory and assembler. For this, it uses the
 * {@link org.seedstack.business.internal.strategy.api.BindingStrategy}.
 *
 * @author epo.jemba@ext.mpsa.com
 * @author redouane.loulou@ext.mpsa.com
 * @author pierre.thirouin@ext.mpsa.com
 */
public class BusinessCorePlugin extends AbstractPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCorePlugin.class);

    private Collection<Class<?>> repositoriesInterfaces;
    private Collection<Class<?>> domainServiceInterfaces;
    private Collection<Class<?>> applicationServiceInterfaces;
    private Collection<Class<?>> finderServiceInterfaces;
    private Collection<Class<?>> policyInterfaces;
    private Collection<Class<?>> interfacesServiceInterfaces;
    private Collection<Class<?>> domainFactoryInterfaces;
    private Collection<Class<?>> assemblersClasses;
    private Collection<Class<?>> defaultAssemblersClasses;
    private Collection<Class<?>> aggregateClasses;
    private Collection<Class<?>> valueObjectClasses;
    private Collection<Class<?>> domainRepoImpls;
    private Map<Type, Class<?>> linksForDefaultAssemblers = new HashMap<Type, Class<?>>();
    private Map<Class<?>, Specification<Class<?>>> specsByInterfaceMap = new HashMap<Class<?>, Specification<Class<?>>>();
    private Collection<BindingStrategy> bindingStrategies = new ArrayList<BindingStrategy>();
    private Map<Key<?>, Class<?>> bindings = new HashMap<Key<?>, Class<?>>();

    @Override
    public String name() {
        return "seed-business-support";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {

        if (roundEnvironment.firstRound()) {
            return classpathScanRequestBuilder()
                    .specification(DomainSpecifications.aggregateRootSpecification)
                    .specification(DomainSpecifications.valueObjectSpecification)
                    .specification(DomainSpecifications.domainRepoImplSpecification)
                    .specification(DomainSpecifications.domainRepoSpecification)
                    .specification(DomainSpecifications.domainServiceSpecification)
                    .specification(DomainSpecifications.applicationServiceSpecification)
                    .specification(DomainSpecifications.interfacesServiceSpecification)
                    .specification(DomainSpecifications.finderServiceSpecification)
                    .specification(DomainSpecifications.policySpecification)
                    .specification(DomainSpecifications.domainFactorySpecification)
                    .specification(DomainSpecifications.classicAssemblerSpecification)
                    .specification(DomainSpecifications.defaultAssemblerSpecification)
                    .specification(DomainSpecifications.dtoWithDefaultAssemblerSpecification).build();
        } else {
            // ROUND 2 ===========================
            //noinspection unchecked
            return classpathRequestForDescendantTypesOf(
                    repositoriesInterfaces,
                    domainServiceInterfaces,
                    applicationServiceInterfaces,
                    interfacesServiceInterfaces,
                    finderServiceInterfaces,
                    finderServiceInterfaces,
                    policyInterfaces,
                    domainFactoryInterfaces).build();
        }
    }

    /**
     * Builds a ClasspathScanRequest to find all the descendant of the given interfaces.
     *
     * @param interfacesArgs the interfaces
     */
    private ClasspathScanRequestBuilder classpathRequestForDescendantTypesOf(Collection<Class<?>>... interfacesArgs) {
        ClasspathScanRequestBuilder classpathScanRequestBuilder = classpathScanRequestBuilder();
        for (Collection<Class<?>> interfaces : interfacesArgs) {
            for (Class<?> anInterface : interfaces) {
                LOGGER.trace("Request implementations of: {}", anInterface.getName());
                //noinspection unchecked
                Specification<Class<?>> spec = and(descendantOf(anInterface), not(classIsInterface()), not(classIsAbstract()));
                classpathScanRequestBuilder = classpathScanRequestBuilder.specification(spec);
                specsByInterfaceMap.put(anInterface, spec);
            }
        }
        return classpathScanRequestBuilder;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public InitState init(InitContext initContext) {
        Map<Specification, Collection<Class<?>>> scannedTypesBySpecification = initContext.scannedTypesBySpecification();

        // The first round is used to scan interfaces
        if (roundEnvironment.firstRound()) {
            repositoriesInterfaces = scannedTypesBySpecification.get(DomainSpecifications.domainRepoSpecification);
            LOGGER.debug("Repository Interface(s) => {}", repositoriesInterfaces);

            domainRepoImpls = scannedTypesBySpecification.get(DomainSpecifications.domainRepoImplSpecification);
            LOGGER.debug("Domain repository default implementation(s) => {}", domainRepoImpls);

            aggregateClasses = scannedTypesBySpecification.get(DomainSpecifications.aggregateRootSpecification);
            LOGGER.debug("Aggregate root(s) => {}", aggregateClasses);

            valueObjectClasses = scannedTypesBySpecification.get(DomainSpecifications.valueObjectSpecification);
            LOGGER.debug("Value object(s) => {}", valueObjectClasses);

            domainServiceInterfaces = scannedTypesBySpecification.get(DomainSpecifications.domainServiceSpecification);
            LOGGER.debug("Domain Service Interface(s) => {}", domainServiceInterfaces);

            applicationServiceInterfaces = scannedTypesBySpecification.get(DomainSpecifications.applicationServiceSpecification);
            LOGGER.debug("Application Service Interface(s) => {}", applicationServiceInterfaces);

            interfacesServiceInterfaces = scannedTypesBySpecification.get(DomainSpecifications.interfacesServiceSpecification);
            LOGGER.debug("Interfaces Service Interface(s) => {}", interfacesServiceInterfaces);

            finderServiceInterfaces = scannedTypesBySpecification.get(DomainSpecifications.finderServiceSpecification);
            LOGGER.debug("Finder Interface(s) => {}", finderServiceInterfaces);

            policyInterfaces = scannedTypesBySpecification.get(DomainSpecifications.policySpecification);
            LOGGER.debug("Policy Interface(s) => {}", policyInterfaces);

            domainFactoryInterfaces = scannedTypesBySpecification.get(DomainSpecifications.domainFactorySpecification);
            LOGGER.debug("Factory Interface(s) => {}", domainFactoryInterfaces);

            assemblersClasses = scannedTypesBySpecification.get(DomainSpecifications.classicAssemblerSpecification);
            LOGGER.debug("Assembler class(es) => {}", assemblersClasses);

            defaultAssemblersClasses = scannedTypesBySpecification.get(DomainSpecifications.defaultAssemblerSpecification);
            LOGGER.debug("Default assembler classes => {}", defaultAssemblersClasses);

            return InitState.NON_INITIALIZED;
        } else {
            // The second round is used to scan implementations of the previously scanned interfaces

            // Classic bindings
            // -- add assemblers to the default mode even if they have no client user interfaces
            List<Class<?>> assemblerClass = new ArrayList<Class<?>>();
            assemblerClass.add(Assembler.class);
            specsByInterfaceMap.put(Assembler.class, DomainSpecifications.classicAssemblerSpecification);

            //noinspection unchecked
            List<Collection<Class<?>>> collections = Lists.newArrayList(
                    applicationServiceInterfaces,
                    domainFactoryInterfaces,
                    domainServiceInterfaces,
                    finderServiceInterfaces,
                    interfacesServiceInterfaces,
                    policyInterfaces,
                    repositoriesInterfaces,
                    assemblerClass
            );
            for (Collection<Class<?>> interfaces : collections) {
                bindings.putAll(associatesInterfaceToImplementations(initContext, interfaces));
            }

            // Bindings for default repositories
            bindingStrategies = buildDefaultRepositoryBindings();

            // Bindings for default factories
            // -- case for aggregate roots and value objects
            BindingStrategy factoryStrategy = buildAggregateDefaultFactoryBindings();
            if (factoryStrategy != null) {
                bindingStrategies.add(factoryStrategy);
            }
            // -- case for other producible objects
            bindingStrategies.add(buildDefaultFactoryBindings());

            // Bindings for default assemblers
            Collection<Class<?>> dtoWithDefaultAssemblerClasses = scannedTypesBySpecification.get(DomainSpecifications.dtoWithDefaultAssemblerSpecification);
            bindingStrategies.addAll(new DefaultAssemblerCollector(defaultAssemblersClasses).collect(dtoWithDefaultAssemblerClasses));

            return InitState.INITIALIZED;
        }
    }

    @Override
    public Object nativeUnitModule() {
        return new BusinessModule(assemblersClasses, bindings, bindingStrategies);
    }

    /**
     * Associates scanned interfaces to their implementations. It also handles qualified bindings in the case where
     * there is multiple implementation for the same interface.
     * <p>
     * This is the "default mode" for binding in the business framework.
     * </p>
     *
     * @param initContext the context containing the implementations
     * @param interfaces  the interfaces to bind
     * @return the map of interface/implementation to bind
     * @see org.seedstack.seed.core.utils.SeedBindingUtils#resolveBindingDefinitions(Class, Class, Class[])
     */
    private Map<Key<?>, Class<?>> associatesInterfaceToImplementations(InitContext initContext, Collection<Class<?>> interfaces) {
        Map<Key<?>, Class<?>> keyMap = new HashMap<Key<?>, Class<?>>();
        for (Class<?> anInterface : interfaces) {
            Collection<Class<?>> subTypes = initContext.scannedTypesBySpecification().get(specsByInterfaceMap.get(anInterface));
            keyMap.putAll(SeedBindingUtils.resolveBindingDefinitions(anInterface, subTypes));
        }
        return keyMap;
    }

    /**
     * Prepares the binding strategy which bind default factories of aggregate roots and value objects.
     * <p>
     * For instance:
     * </p>
     * <pre>
     * {@literal @}Inject
     * Factory&lt;Customer&gt; customerFactory;
     * </pre>
     *
     * @return a binding strategy
     */
    private BindingStrategy buildAggregateDefaultFactoryBindings() {
        Collection<Class<?>[]> generics = new ArrayList<Class<?>[]>();
        if (aggregateClasses != null && !aggregateClasses.isEmpty()) {
            for (Class<?> aggregateClass : aggregateClasses) {
                generics.add(new Class<?>[]{aggregateClass});
            }
        }
        if (valueObjectClasses != null && !valueObjectClasses.isEmpty()) {
            for (Class<?> valueObjectClass : valueObjectClasses) {
                generics.add(new Class<?>[]{valueObjectClass});
            }
        }
        if (!generics.isEmpty()) {
            return new GenericBindingStrategy(generics, Factory.class, FactoryInternal.class, new ProviderFactory<Factory>());
        }
        return null;
    }

    /**
     * Prepares the binding strategy which bind default factories of {@link org.seedstack.business.api.Producible}
     * objects other than aggregate roots and value objects.
     * <p>
     * It binds for instance default factories of policies.
     * </p>
     * <pre>
     * {@literal @}Inject
     * Factory&lt;Customer&gt; customerFactory;
     * </pre>
     *
     * @return a binding strategy
     */
    private BindingStrategy buildDefaultFactoryBindings() {
        //noinspection unchecked
        Specification<Class<?>> creatable = and(descendantOf(Producible.class), descendantOf(DomainObject.class));

        // iterate on all the domain element to bind
        Multimap<Type, Class<?>> defaultFactoryToBind = ArrayListMultimap.create();
        for (Map.Entry<Key<?>, Class<?>> keyClassEntry : bindings.entrySet()) {

            // filter on those which are creatable by a domain factory
            if (creatable.isSatisfiedBy(keyClassEntry.getKey().getTypeLiteral().getRawType())) {
                defaultFactoryToBind.put(keyClassEntry.getKey().getTypeLiteral().getType(), keyClassEntry.getValue());
            }
        }
        return new FactoryPatternBindingStrategy(defaultFactoryToBind, Factory.class, FactoryInternal.class, new ProviderFactory<Factory>());
    }

    /**
     * Prepares the binding strategies which bind default repositories. The specificity here is that it could have
     * multiple implementations of default repository, i.e. one per persistence.
     *
     * @return a binding strategy
     */
    private Collection<BindingStrategy> buildDefaultRepositoryBindings() {
        // this method support multiple default implementation for repository (one for each persistence technology).

        Collection<BindingStrategy> bindingStrategies = new ArrayList<BindingStrategy>();
        Collection<Class<?>[]> generics = new ArrayList<Class<?>[]>();
        for (Class<?> aggregateClass : aggregateClasses) {
            Class<?> keyType = TypeToken.of(aggregateClass).resolveType(AggregateRoot.class.getTypeParameters()[0]).getRawType();
            generics.add(new Class<?>[]{aggregateClass, keyType});
        }
        for (Class<?> domainRepoImpl : domainRepoImpls) {
            bindingStrategies.add(new GenericBindingStrategy(generics, Repository.class, domainRepoImpl, new ProviderFactory<Repository>()));
        }
        return bindingStrategies;
    }
}
