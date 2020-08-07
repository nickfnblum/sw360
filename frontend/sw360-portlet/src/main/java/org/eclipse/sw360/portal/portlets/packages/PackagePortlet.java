/*
 * Copyright Siemens AG, 2020, 2019. Part of the SW360 Portal Project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.sw360.portal.portlets.packages;

import static org.eclipse.sw360.portal.common.PortalConstants.PACKAGES_PORTLET_NAME;
import static org.eclipse.sw360.portal.common.PortalConstants.PACKAGE_ID;
import static org.eclipse.sw360.portal.common.PortalConstants.PAGENAME;
import static org.eclipse.sw360.portal.common.PortalConstants.PAGENAME_DETAIL;
import static org.eclipse.sw360.portal.common.PortalConstants.TODO_LIST;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.eclipse.sw360.datahandler.thrift.RequestStatus;
import org.eclipse.sw360.datahandler.thrift.licenses.LicenseService;
import org.eclipse.sw360.datahandler.thrift.licenses.Obligations;
import org.eclipse.sw360.datahandler.thrift.users.User;
import org.eclipse.sw360.portal.portlets.Sw360Portlet;
import org.eclipse.sw360.portal.users.UserCacheHolder;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@org.osgi.service.component.annotations.Component(
    immediate = true,
    properties = {
        "/org/eclipse/sw360/portal/portlets/base.properties",
        "/org/eclipse/sw360/portal/portlets/default.properties"
    },
    property = {
        "javax.portlet.name=" + PACKAGES_PORTLET_NAME,

        "javax.portlet.display-name=Packages",
        "javax.portlet.info.short-title=Packages",
        "javax.portlet.info.title=Packages",
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.init-param.view-template=/html/packages/view.jsp",
    },
    service = Portlet.class,
    configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class PackagePortlet extends Sw360Portlet {

    private static final Logger log = Logger.getLogger(PackagePortlet.class);


    //! Serve resource and helpers
    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) {

        final String id = request.getParameter("id");
        final User user = UserCacheHolder.getUserFromRequest(request);

        LicenseService.Iface licenseClient = thriftClients.makeLicenseClient();


        try {
            RequestStatus status = licenseClient.deleteObligations(id, user);
            renderRequestStatus(request,response, status);
        } catch (TException e) {
            log.error("Error deleting oblig", e);
            renderRequestStatus(request,response, RequestStatus.FAILURE);
        }
    }


    //! VIEW and helpers
    @Override
    public void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {

        final String pageName = request.getParameter(PAGENAME);
        final String id = request.getParameter("id");
        final String packageId = request.getParameter(PACKAGE_ID);
        if (PAGENAME_DETAIL.equals(pageName)) {
            prepareDetailView(request, response);
            include("/html/packages/detail.jsp", request, response);
        } else {
            // prepareStandardView(request);
            request.setAttribute("Testing", "Testing is successful!");
            super.doView(request, response);
        }
    }

    private void prepareStandardView(RenderRequest request) {
        List<Obligations> obligList;
        try {
            final User user = UserCacheHolder.getUserFromRequest(request);
            LicenseService.Iface licenseClient = thriftClients.makeLicenseClient();

            obligList = licenseClient.getObligations();

        } catch (TException e) {
            log.error("Could not get Obligations from backend ", e);
            obligList = Collections.emptyList();
        }

        request.setAttribute(TODO_LIST, obligList);
    }

    private void prepareDetailView(RenderRequest request, RenderResponse response) {
        String id = request.getParameter(PACKAGE_ID);
        final User user = UserCacheHolder.getUserFromRequest(request);
        request.setAttribute("name", id);
        request.setAttribute("mainLicenses", "Apache-2.0, GPL-2.0");
        request.setAttribute("vendor", "Microsoft");
        request.setAttribute("createdBy", user.getEmail());
        request.setAttribute("createdOn", "2020-06-18");
        request.setAttribute("packageType", "nuget");
        request.setAttribute("packageUrl", "https://www.nuget.org/packages/Microsoft.ApplicationInsights/2.4.0");
        request.setAttribute("linkedComponentRelease", ".net core (1.0.1)");
    }
}
