/*
 * Copyright Siemens AG, 2020. Part of the SW360 Portal Project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.sw360.packages;

import static org.eclipse.sw360.datahandler.common.SW360Assert.assertId;

import java.io.IOException;

import org.apache.thrift.TException;
import org.eclipse.sw360.datahandler.common.DatabaseSettings;
import org.eclipse.sw360.datahandler.db.PackageDatabaseHandler;
import org.eclipse.sw360.datahandler.thrift.SW360Exception;
import org.eclipse.sw360.datahandler.thrift.packages.Package;
import org.eclipse.sw360.datahandler.thrift.packages.PackageService;

/**
 * Implementation of the Thrift service
 *
 * @author abdul.mannankapti@siemens.com
 */
public class PackageHandler implements PackageService.Iface {

    private final PackageDatabaseHandler handler;

    PackageHandler() throws IOException {
        handler = new PackageDatabaseHandler(DatabaseSettings.getConfiguredHttpClient(), DatabaseSettings.COUCH_DB_DATABASE);
    }

    @Override
    public Package getPackageById(String packageId) throws SW360Exception, TException {
        assertId(packageId);
        return handler.getPackageById(packageId);
    }
}