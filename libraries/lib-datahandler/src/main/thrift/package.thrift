/*
 * Copyright Siemens AG, 2020. Part of the SW360 Portal Project.
 * With contributions by Bosch Software Innovations GmbH, 2016.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
include "users.thrift"
include "sw360.thrift"

namespace java org.eclipse.sw360.datahandler.thrift.packages
namespace php sw360.thrift.packages

typedef users.User User
typedef sw360.SW360Exception SW360Exception

struct Package {
    1: optional string id,
    2: optional string revision,
    3: optional string type = "package"
}

service PackageService {
    /**
     * get Package by Id
     */
    Package getPackageById(1: string packageId) throws (1: SW360Exception exp);
}
