/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.domain;

import java.lang.annotation.*;

/**
 * This annotation is a marker for {@link org.seedstack.business.domain.GenericFactory}.
 * <p>
 * {@code GenericFactory} is the interface to extend to create a Domain Factory interface.
 * </p>
 *
 * @author epo.jemba@ext.mpsa.com
 */
@Documented
@DomainElement
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface DomainFactory {

}