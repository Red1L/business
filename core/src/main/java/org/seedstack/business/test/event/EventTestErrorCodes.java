/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.test.event;


import org.seedstack.seed.core.api.ErrorCode;

/**
 * Error codes for event fixtures.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
public enum EventTestErrorCodes implements ErrorCode {
    EVENT_WAS_HANDLER_BY,
    EVENT_WAS_NOT_HANDLER_BY,
    EVENT_WAS_NOT_EXACTLY_HANDLER_BY,
    FAILED_TO_INVOKE_METHOD,
    HANDLER_WAS_NOT_CALLED,
    HANDLER_WAS_NOT_CALLED_WITH_EXPECTED_EVENT,
}
