/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.event;


import org.seedstack.seed.core.api.ErrorCode;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 11/06/2014
 */
public enum EventErrorCodes implements ErrorCode {
    CYCLE_WAS_DETECTED_IN_FIRED_EVENT,
    INTERCEPTED_CLASS_SHOULD_BE_A_REPOSITORY, HANDLER_EXECUTION_FAILED
}
