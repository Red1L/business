/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.core.interfaces.assembler.resolver.sample;

import org.seedstack.business.api.interfaces.assembler.MatchingEntityId;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class CaseFail3Dto {

    String firstName;

    String lastName;

    public CaseFail3Dto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @MatchingEntityId(index = 0, typeIndex = 0)
    public String getFirstName() {
        return firstName;
    }

    @MatchingEntityId(index = 0, typeIndex = 0)
    public String getLastName() {
        return lastName;
    }

}
