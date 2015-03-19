/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.assertions.asserts;


import org.seedstack.business.api.application.annotations.ApplicationService;
import org.seedstack.business.api.domain.GenericFactory;
import org.seedstack.business.api.domain.GenericRepository;
import org.seedstack.business.api.domain.annotations.DomainAggregateRoot;
import org.seedstack.business.api.domain.annotations.DomainEntity;
import org.seedstack.business.api.domain.annotations.DomainPolicy;
import org.seedstack.business.api.domain.annotations.DomainService;
import org.seedstack.business.api.domain.base.BaseAggregateRoot;
import org.seedstack.business.api.domain.base.BaseEntity;
import org.seedstack.business.api.domain.base.BaseValueObject;
import org.seedstack.business.api.domain.meta.specifications.DomainSpecifications;
import org.seedstack.business.assertions.BusinessErrorCodes;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.kametic.specifications.Specification;
import org.seedstack.seed.core.api.SeedException;

import java.lang.reflect.Modifier;
import java.util.Iterator;


/**
 * This class gives helper methods for asserting on business classes.
 * 
 * @author epo.jemba@ext.mpsa.com
 */
public final class BusinessReflectionAsserts {

    private static final String CONSTRUCTOR_NAME = "constructorName";
    private static final String CONSTRUCTOR_CLASS_NAME = "constructorClassName";
    private static final String CLASS_NAME = "className";
    private static final String PARENT_CLASS_NAME = "parentClassName";
    private static final String MORE = "more";
    private static final String ANNOTATION_NAME = "annotationName";

	private BusinessReflectionAsserts() {
	}

	/**
	 * Asserts that the aggregate root class is valid.
     *
	 * @param actual the class to be checked.
	 */
    public static void assertAggregateRootClassIsValid(Class<?> actual) {

        // we check that the constructors are not public
        $(
                actual, DomainSpecifications.not(DomainSpecifications.classConstructorIsPublic()),
                BusinessErrorCodes.CLASS_CONSTRUCTORS_MUST_NOT_BE_PUBLIC,
                CONSTRUCTOR_NAME, actual.getSimpleName(),
                CONSTRUCTOR_CLASS_NAME, actual.getSimpleName()
        );

        // we check the class is not abstract
        $(
                actual, DomainSpecifications.not(DomainSpecifications.classModifierIs(Modifier.ABSTRACT)),
                BusinessErrorCodes.CLASS_MUST_NOT_BE_ABSTRACT, CLASS_NAME, actual.getName()
        );

        // we check for the annotation
        $(
                actual, DomainSpecifications.classInherits(BaseAggregateRoot.class),
                BusinessErrorCodes.CLASS_MUST_EXTENDS, CLASS_NAME, actual.getName(), PARENT_CLASS_NAME, BaseAggregateRoot.class.getName(),
                MORE, "Or one of of its children classes."
        );

        // we check for the annotation
        $(
                actual, DomainSpecifications.ancestorMetaAnnotatedWith(DomainAggregateRoot.class),
                BusinessErrorCodes.CLASS_OR_PARENT_MUST_BE_ANNOTATED_WITH
        );

        // check @id in fields
    }

    /**
     * Asserts that the given class is a valid ValueObject.
     *
     * @param actual the class to be checked
     */
	public static void assertValueObjectClassIsValid(Class<?> actual) {
		
		// we check that the constructors are not public
        $(
            actual , DomainSpecifications.not(DomainSpecifications.classConstructorIsPublic()) ,
            BusinessErrorCodes.CLASS_CONSTRUCTORS_MUST_NOT_BE_PUBLIC ,
                CONSTRUCTOR_CLASS_NAME, actual.getSimpleName()
		);
		
		// we check the class is not abstract
        $(
            actual, DomainSpecifications.not(DomainSpecifications.classModifierIs(Modifier.ABSTRACT)) ,
            BusinessErrorCodes.CLASS_MUST_NOT_BE_ABSTRACT  , CLASS_NAME, actual.getName() );
		
		// we check for the annotation
        $(
            actual , DomainSpecifications.classInherits(BaseValueObject.class) ,
            BusinessErrorCodes.CLASS_MUST_EXTENDS , CLASS_NAME, actual.getName() , PARENT_CLASS_NAME, BaseValueObject.class.getName(),
                MORE, "Or one of of its children classes."
		);
		
		// we check value object
        $(
            actual , DomainSpecifications.classHasOnlyPackageViewSetters()  ,
            BusinessErrorCodes.CLASS_MUST_HAVE_ONLY_PACKAGED_VIEW_SETTERS , CLASS_NAME, actual.getName()
        );
	}

    /**
     * Asserts that the class is a valid Entity class.
     *
     * @param actual the class to be checked
     */
	public static void assertEntityClassIsValid(Class<?> actual) {
		// we check that the constructors are not public
        $(
          actual , DomainSpecifications.not(DomainSpecifications.classConstructorIsPublic()) ,
          BusinessErrorCodes.CLASS_CONSTRUCTORS_MUST_NOT_BE_PUBLIC,
                CONSTRUCTOR_NAME, actual.getSimpleName(),
                CONSTRUCTOR_CLASS_NAME, actual.getSimpleName()
        );

        // we check the class is not abstract
        $(
            actual, DomainSpecifications.not(DomainSpecifications.classModifierIs(Modifier.ABSTRACT)) ,
            BusinessErrorCodes.CLASS_MUST_NOT_BE_ABSTRACT  , CLASS_NAME, actual.getName()
        );


        // we check for the annotation
        $(
            actual , DomainSpecifications.classInherits(BaseEntity.class) ,
            BusinessErrorCodes.CLASS_MUST_EXTENDS , CLASS_NAME, actual.getName() , PARENT_CLASS_NAME, BaseEntity.class.getName() ,
                MORE, "Or one of of its children classes."
        );

        // we check for the annotation
        $(
            actual , DomainSpecifications.ancestorMetaAnnotatedWith(DomainEntity.class) ,
            BusinessErrorCodes.CLASS_OR_PARENT_MUST_BE_ANNOTATED_WITH,
                CLASS_NAME, actual.getName() ,
                ANNOTATION_NAME, DomainEntity.class.getName()
         );
	}

    /**
     * Asserts that the class is a valid repository.
     *
     * @param actual the class to be checked
     */
	public static void assertRepositoryInterfaceClassIsValid(Class<?> actual) {
		
		// we check the class is not abstract
        $(
            actual, DomainSpecifications.classIsInterface() ,
            BusinessErrorCodes.CLASS_MUST_BE_INTERFACE  , CLASS_NAME, actual.getName()
		);
		
		// we check for the annotation
        $(
            actual , DomainSpecifications.classInherits(GenericRepository.class) ,
            BusinessErrorCodes.CLASS_MUST_EXTENDS ,
                CLASS_NAME, actual.getName() , PARENT_CLASS_NAME, GenericRepository.class.getName(),
                MORE, ""
		);
	}

    /**
     * Asserts that the class is a valid factory interface.
     *
     * @param actual the class to be checked
     */
	public static void assertFactoryInterfaceClassIsValid(Class<?> actual) {
		// we check the class is not abstract
        $(
            actual, DomainSpecifications.classIsInterface(),
            BusinessErrorCodes.CLASS_MUST_BE_INTERFACE, CLASS_NAME, actual.getName()
        );
		
		// we check for the annotation
        $(
            actual , DomainSpecifications.classInherits(GenericFactory.class) ,
            BusinessErrorCodes.CLASS_MUST_EXTENDS ,
                CLASS_NAME, actual.getName() , PARENT_CLASS_NAME, GenericFactory.class.getName(),
                MORE, ""
		);
	}

    /**
     * Asserts that the class is valid DomainServiceInterface.
     *
     * @param actual the class to be checked
     */
	public static void assertDomainServiceInterfaceClassIsValid(Class<?> actual) {
		// we check the class is not abstract
        $(
            actual, DomainSpecifications.classIsInterface() ,
            BusinessErrorCodes.CLASS_MUST_BE_INTERFACE  , CLASS_NAME, actual.getName()
        );
		
		// we check for the annotation
        $(
            actual , DomainSpecifications.classMetaAnnotatedWith(DomainService.class),
            BusinessErrorCodes.CLASS_OR_PARENT_MUST_BE_ANNOTATED_WITH,
                CLASS_NAME, actual.getName() ,
                ANNOTATION_NAME, DomainService.class.getName()
		 );
	
	}

    /**
     * Asserts that the class is a valid ApplicationServiceInterface.
     *
     * @param actual the class to be checked
     */
	public static void assertApplicationServiceInterfaceClassIsValid(Class<?> actual) {
		// we check the class is not abstract
        $(
            actual, DomainSpecifications.classIsInterface() ,
            BusinessErrorCodes.CLASS_MUST_BE_INTERFACE  , CLASS_NAME, actual.getName()
        );
		
		// we check for the annotation
        $(
            actual , DomainSpecifications.classMetaAnnotatedWith(ApplicationService.class),
            BusinessErrorCodes.CLASS_OR_PARENT_MUST_BE_ANNOTATED_WITH,
                CLASS_NAME, actual.getName() ,
                ANNOTATION_NAME, ApplicationService.class.getName()
        );
	}

    /**
     * Asserts that the class is a valid DomainPolicyInterface.
     *
     * @param actual the class to be checked
     */
	public static void assertDomainPolicyInterfaceClassIsValid(Class<?> actual) {
		// we check the class is not abstract
        $(
            actual, DomainSpecifications.classIsInterface() ,
            BusinessErrorCodes.CLASS_MUST_BE_INTERFACE  , CLASS_NAME, actual.getName()
        );
		
		// we check for the annotation
        $(
            actual , DomainSpecifications.classMetaAnnotatedWith(DomainPolicy.class),
            BusinessErrorCodes.CLASS_OR_PARENT_MUST_BE_ANNOTATED_WITH,
                CLASS_NAME, actual.getName() ,
                ANNOTATION_NAME, DomainPolicy.class.getName()
        );
	}

    @SuppressWarnings("unchecked")
	private static <T> void $(T actual, Specification<T> specification, BusinessErrorCodes errorCode, String... messages) { //NOSONAR
        if (!specification.isSatisfiedBy(actual)) {
            SeedException seedException  = SeedException.createNew(errorCode);
			
			Iterator<String> it = new ArrayIterator(messages);
	        while (it.hasNext()) {
                String key = it.next();
	            String value = "";
	            if (it.hasNext()) {
					value = it.next();
				}
	            seedException.put(key, value);
	        }
			
			seedException.thenThrows();
		}
	}




	

}
