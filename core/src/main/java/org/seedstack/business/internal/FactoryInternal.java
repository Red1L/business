/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.seedstack.business.internal;

import com.google.inject.assistedinject.Assisted;
import org.seedstack.business.Producible;
import org.seedstack.business.domain.*;
import org.seedstack.business.domain.identity.IdentityService;
import org.seedstack.business.internal.utils.MethodMatcher;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.core.utils.SeedCheckUtils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * FactoryInternal allows the creations of {@link org.seedstack.business.domain.DomainObject} objects using their constructors.
 * <p>
 * The {@link #create(Object...)} method will look for a constructor matching the given parameters.
 * If a constructor is found the method will use it to create a new instance. If ambiguous constructors are
 * found, it throws an exception.
 * </p>
 * Ambiguous constructor could be found in the following cases:
 * 
 * 1. If a parameter is null and multiple constructors accept null.
 * <pre>
 * MyObject(String name)
 * MyObject(Integer age)
 * 
 * factory.create(null);
 * </pre> 
 * 
 * 2. If two constructor are found, one with primitive, and another with boxed type.
 * <pre>
 * MyObject(int age)
 * MyObject(Integer age)
 * </pre> 
 * 
 * @author redouane.loulou@ext.mpsa.com
 * @author pierre.thirouin@ext.mpsa.com
 * @param <DO> the domain object type
 */
public class FactoryInternal<DO extends DomainObject & Producible> implements Factory<DO> {

	protected Class<DO> domainObjectClass;

    @Inject
    private IdentityService identityService;

	/**
	 * Constructor.
	 * 
	 * @param domainObjectClass the domain object class
	 */
	@SuppressWarnings("unchecked")
    @Inject
	public FactoryInternal(@Assisted Object[] domainObjectClass) {
        Object[] clonedClasses = domainObjectClass.clone();
        SeedCheckUtils.checkIfNotNull(clonedClasses);
        SeedCheckUtils.checkIf(clonedClasses.length == 1);
		this.domainObjectClass = (Class<DO>) clonedClasses[0];
	}

	public Class<DO> getProducedClass() {
		return domainObjectClass;
	}

	@Override
	public DO create(Object... args) {
		Constructor<?> constructor = MethodMatcher.findMatchingConstructor(getProducedClass(), args);
		DO domainObject;
		if (constructor == null) {
            throw SeedException.createNew(DomainErrorCodes.DOMAIN_OBJECT_CONSTRUCTOR_NOT_FOUND)
                    .put("domainObject", getProducedClass()).put("parameters", Arrays.toString(args));
        }
        try {
			constructor.setAccessible(true);
            //noinspection unchecked
            domainObject = (DO) constructor.newInstance(args);

		} catch (Exception e) {
			throw SeedException.wrap(e, DomainErrorCodes.UNABLE_TO_INVOKE_CONSTRUCTOR).put("constructor", constructor)
					.put("domainObject", getProducedClass()).put("parameters", Arrays.toString(args));
		}
        domainObject = populateIdentity(domainObject);
        return domainObject;
	}

    /**
     * If the domain object is an {@link org.seedstack.business.domain.Entity} and has a field annotated
     * by {@link org.seedstack.business.domain.Identity}. Use the
     * {@link org.seedstack.business.domain.identity.IdentityService} in order to populate the entity's identity.
     * <p>
     * Note: The {@link Create} annotation cannot be used
     * here because the {@code create()} method doesn't always return an Entity and all the entities doesn't use the
     * identity creation strategy.
     * </p>
     * @param domainObject the domain object to populate
     * @return the domain object
     */
    private DO populateIdentity(DO domainObject) {
        if (Entity.class.isAssignableFrom(domainObject.getClass())) {
            Entity<?> entity = (Entity<?>) domainObject;
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Identity.class)) {
                    domainObject = (DO) identityService.identify(entity);
                }
            }
        }
        return domainObject;
    }

}
