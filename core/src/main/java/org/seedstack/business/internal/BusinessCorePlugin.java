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

import com.google.common.collect.Lists;
import com.google.inject.Key;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import io.nuun.kernel.core.AbstractPlugin;
import org.kametic.specifications.Specification;
import org.seedstack.business.api.interfaces.assembler.Assembler;
import org.seedstack.business.api.specifications.DomainSpecifications;
import org.seedstack.business.internal.strategy.api.BindingStrategy;
import org.seedstack.business.internal.utils.BindingUtils;
import org.seedstack.seed.core.internal.application.ApplicationPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Collection<Class<?>> aggregateClasses;
    private Collection<Class<?>> applicationServiceInterfaces;
    private Collection<Class<?>> assemblersClasses;
    private Collection<Class<?>> domainFactoryInterfaces;
    private Collection<Class<?>> domainServiceInterfaces;
    private Collection<Class<?>> finderServiceInterfaces;
    private Collection<Class<?>> interfacesServiceInterfaces;
    private Collection<Class<?>> policyInterfaces;
    private Collection<Class<?>> repositoriesInterfaces;
    private Collection<Class<?>> valueObjectClasses;

    private Collection<Class<?>> defaultAssemblerClasses;
    private Collection<Class<?>> defaultRepositoryClasses;

    private Map<Class<?>, Specification<Class<?>>> specsByInterfaceMap = new HashMap<Class<?>, Specification<Class<?>>>();
    private Collection<BindingStrategy> bindingStrategies = new ArrayList<BindingStrategy>();
    private Map<Key<?>, Class<?>> bindings = new HashMap<Key<?>, Class<?>>();

    @Override
    public String name() {
        return "business-core";
    }

    @Override
    public Collection<Class<? extends Plugin>> requiredPlugins() {
        //noinspection unchecked
        return Lists.<Class<? extends Plugin>>newArrayList(ApplicationPlugin.class);
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {

        if (roundEnvironment.firstRound()) {
            return classpathScanRequestBuilder()
                    .specification(DomainSpecifications.AGGREGATE_ROOT)
                    .specification(DomainSpecifications.APPLICATION_SERVICE)
                    .specification(DomainSpecifications.CLASSIC_ASSEMBLER)
                    .specification(DomainSpecifications.DOMAIN_SERVICE)
                    .specification(DomainSpecifications.FACTORY)
                    .specification(DomainSpecifications.FINDER)
                    .specification(DomainSpecifications.INTERFACE_SERVICE)
                    .specification(DomainSpecifications.POLICY)
                    .specification(DomainSpecifications.REPOSITORY)
                    .specification(DomainSpecifications.VALUE_OBJECT)

                    .specification(DomainSpecifications.DEFAULT_ASSEMBLER)
                    .specification(DomainSpecifications.DEFAULT_REPOSITORY)
                    .specification(DomainSpecifications.DTO_OF).build();
        } else {
            // ROUND 2 ===========================

            //noinspection unchecked
            return classpathRequestForDescendantTypesOf(
                    applicationServiceInterfaces,
                    domainFactoryInterfaces,
                    domainServiceInterfaces,
                    finderServiceInterfaces,
                    finderServiceInterfaces,
                    interfacesServiceInterfaces,
                    policyInterfaces,
                    repositoriesInterfaces).build();
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
        Map<Specification, Collection<Class<?>>> spec = initContext.scannedTypesBySpecification();

        // The first round is used to scan interfaces
        if (roundEnvironment.firstRound()) {
            aggregateClasses = spec.get(DomainSpecifications.AGGREGATE_ROOT);
            LOGGER.debug("Aggregate root(s) => {}", aggregateClasses);

            applicationServiceInterfaces = spec.get(DomainSpecifications.APPLICATION_SERVICE);
            LOGGER.debug("Application Service Interface(s) => {}", applicationServiceInterfaces);

            assemblersClasses = spec.get(DomainSpecifications.CLASSIC_ASSEMBLER);
            LOGGER.debug("Assembler class(es) => {}", assemblersClasses);

            domainFactoryInterfaces = spec.get(DomainSpecifications.FACTORY);
            LOGGER.debug("Factory Interface(s) => {}", domainFactoryInterfaces);

            domainServiceInterfaces = spec.get(DomainSpecifications.DOMAIN_SERVICE);
            LOGGER.debug("Domain Service Interface(s) => {}", domainServiceInterfaces);

            finderServiceInterfaces = spec.get(DomainSpecifications.FINDER);
            LOGGER.debug("Finder Interface(s) => {}", finderServiceInterfaces);

            interfacesServiceInterfaces = spec.get(DomainSpecifications.INTERFACE_SERVICE);
            LOGGER.debug("Interfaces Service Interface(s) => {}", interfacesServiceInterfaces);

            policyInterfaces = spec.get(DomainSpecifications.POLICY);
            LOGGER.debug("Policy Interface(s) => {}", policyInterfaces);

            repositoriesInterfaces = spec.get(DomainSpecifications.REPOSITORY);
            LOGGER.debug("Repository Interface(s) => {}", repositoriesInterfaces);

            valueObjectClasses = spec.get(DomainSpecifications.VALUE_OBJECT);
            LOGGER.debug("Value object(s) => {}", valueObjectClasses);

            // Default implementations

            defaultRepositoryClasses = spec.get(DomainSpecifications.DEFAULT_REPOSITORY);
            LOGGER.debug("Default repositories => {}", defaultRepositoryClasses);

            defaultAssemblerClasses = spec.get(DomainSpecifications.DEFAULT_ASSEMBLER);
            LOGGER.debug("Default assembler(s) => {}", defaultAssemblerClasses);

            return InitState.NON_INITIALIZED;
        } else {
            // The second round is used to scan implementations of the previously scanned interfaces

            // Classic bindings
            // -- add assemblers to the default mode even if they have no client user interfaces
            List<Class<?>> assemblerClass = new ArrayList<Class<?>>();
            assemblerClass.add(Assembler.class);
            specsByInterfaceMap.put(Assembler.class, DomainSpecifications.CLASSIC_ASSEMBLER);

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
            bindingStrategies = new DefaultRepositoryCollector(aggregateClasses, defaultRepositoryClasses).collect();

            // Bindings for default factories
            Collection<Class<?>> aggregateOrVOClasses = new ArrayList<Class<?>>();
            aggregateOrVOClasses.addAll(aggregateClasses);
            aggregateOrVOClasses.addAll(valueObjectClasses);
            bindingStrategies.addAll(new DefaultFactoryCollector(aggregateOrVOClasses, bindings).collect());

            // Bindings for default assemblers
            Collection<Class<?>> dtoWithDefaultAssemblerClasses = spec.get(DomainSpecifications.DTO_OF);
            bindingStrategies.addAll(new DefaultAssemblerCollector(defaultAssemblerClasses).collect(dtoWithDefaultAssemblerClasses));

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
     * @see org.seedstack.business.internal.utils.BindingUtils#resolveBindingDefinitions(Class, Class, Class[])
     */
    private Map<Key<?>, Class<?>> associatesInterfaceToImplementations(InitContext initContext, Collection<Class<?>> interfaces) {
        Map<Key<?>, Class<?>> keyMap = new HashMap<Key<?>, Class<?>>();
        for (Class<?> anInterface : interfaces) {
            Collection<Class<?>> subTypes = initContext.scannedTypesBySpecification().get(specsByInterfaceMap.get(anInterface));
            keyMap.putAll(BindingUtils.resolveBindingDefinitions(anInterface, subTypes));
        }
        return keyMap;
    }
}
