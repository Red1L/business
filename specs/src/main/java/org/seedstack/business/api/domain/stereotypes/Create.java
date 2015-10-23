/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api.domain.stereotypes;

import java.lang.annotation.*;

/**
 * This annotation indicates a creation. Be careful, a creation is related to object instantiation not to persistence.
 * For instance, it could be used on a factory.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
public @interface Create {
}
