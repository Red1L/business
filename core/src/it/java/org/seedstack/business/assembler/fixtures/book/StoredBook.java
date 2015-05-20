/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.assembler.fixtures.book;

import org.seedstack.business.api.domain.base.BaseAggregateRoot;

import java.util.Date;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class StoredBook extends BaseAggregateRoot<BookId> {

    private BookId bookId;

    private Date publishDate;

    private String editor;

    public StoredBook(BookId bookId) {
        this.bookId = bookId;
    }

    @Override
    public BookId getEntityId() {
        return bookId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
