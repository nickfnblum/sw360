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

import static org.eclipse.sw360.datahandler.common.SW360Assert.assertNotNull;

import java.net.MalformedURLException;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.eclipse.sw360.datahandler.couchdb.DatabaseConnector;
import org.eclipse.sw360.datahandler.thrift.SW360Exception;
import org.eclipse.sw360.datahandler.thrift.packages.Package;
import org.ektorp.http.HttpClient;

/**
 * Class for accessing the CouchDB database for Artifacts.
 *
 * @author: abdul.mannankapti@siemens.com
 */
public class PackageDatabaseHandler {
    private final DatabaseConnector db;
    private final PackageRepository packageRepository;

    private static final Logger log = Logger.getLogger(PackageDatabaseHandler.class);

    public PackageDatabaseHandler(Supplier<HttpClient> httpClient, String dbName) throws MalformedURLException {
        db = new DatabaseConnector(httpClient, dbName);
        packageRepository = new PackageRepository(db);
    }

    public Package getPackageById(String id) throws SW360Exception {
        Package pkg = packageRepository.get(id);
        assertNotNull(pkg);
        return pkg;
    }
}
