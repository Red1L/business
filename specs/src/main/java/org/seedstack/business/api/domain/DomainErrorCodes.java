/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api.domain;


import org.seedstack.seed.core.api.ErrorCode;

public enum DomainErrorCodes implements ErrorCode {
	AGGREGATE_ROOT_CREATION_ISSUE,
	ENTITY_WITHOUT_IDENTITY_ISSUE,
	DOMAIN_OBJECT_CONSTRUCTOR_NOT_FOUND,
	UNABLE_TO_INVOKE_CONSTRUCTOR,
	AMBIGUOUS_CONSTRUCTOR_FOUND
}
