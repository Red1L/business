/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.interfaces.assembler.dsl.resolver.sample;

import org.seedstack.business.api.interfaces.assembler.MatchingEntityId;

/**
 * Case 4: The first name and last name are mapped to a {@code Pair&lt;String, String&gt;} in the constructor.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
public class Case4Dto {

    String firstName;

    String lastName;

    String orderItem;

    String orderDescription;

    public Case4Dto(String firstName, String lastName, String orderItem, String orderDescription) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.orderItem = orderItem;
        this.orderDescription = orderDescription;
    }

    @MatchingEntityId(index = 0, typeIndex = 0)
    public String getFirstName() {
        return firstName;
    }

    @MatchingEntityId(index = 1, typeIndex = 0)
    public String getLastName() {
        return lastName;
    }

    @MatchingEntityId(index = 0, typeIndex = 1)
    public String getOrderItem() {
        return orderItem;
    }

    @MatchingEntityId(index = 1, typeIndex = 1)
    public String getOrderDescription() {
        return orderDescription;
    }
}
