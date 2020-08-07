/*
 * Copyright Siemens AG, 2020. Part of the SW360 Portal Project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.sw360.datahandler.db;

import org.eclipse.sw360.datahandler.couchdb.DatabaseConnector;
import org.eclipse.sw360.datahandler.couchdb.DatabaseRepository;
import org.eclipse.sw360.datahandler.thrift.packages.Package;
import org.ektorp.support.View;

/**
 * CRUD access for the Package class
 *
 * @author abdul.mannankapti@siemens.com
 */
@View(name = "all", map = "function(doc) { if (doc.type == 'package') emit(doc._id, doc) }")
public class PackageRepository extends DatabaseRepository<Package> {

    public PackageRepository(DatabaseConnector db) {
        super(Package.class, db);
        initStandardDesignDocument();
    }
}
