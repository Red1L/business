/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.event;


import io.nuun.kernel.spi.Concern;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Nuun concern for ordering event operations.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Concern(name="seed-event-concern", priority= Concern.Priority.NORMAL)
public @interface EventConcern {
}
