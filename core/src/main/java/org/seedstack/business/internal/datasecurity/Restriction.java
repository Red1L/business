/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.datasecurity;

import org.seedstack.seed.security.spi.data.DataObfuscationHandler;

import java.lang.annotation.*;





/**
 * This annotation will allow
 * 
 * @author epo.jemba@ext.mpsa.com
 * @author pierre.thirouin@ext.mpsa.com
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD})
public @interface Restriction {
	
	/**
	 * The security expression related to the annotated element.
	 * <p>
	 * Please use an Expression Language.
	 * <p>
	 * 
	 */
	String value() default "${false}";
	
	/**
	 * The obfuscation handler to use in case the restriction is false.
	 * 
	 */
	Class<? extends DataObfuscationHandler<?>> obfuscation () default InstanceCleaner.class;
}
