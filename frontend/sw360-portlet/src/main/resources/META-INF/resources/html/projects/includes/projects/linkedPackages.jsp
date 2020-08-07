<%--
  ~ Copyright Siemens AG, 2020. Part of the SW360 Portal Project.
  ~
  ~ This program and the accompanying materials are made
  ~ available under the terms of the Eclipse Public License 2.0
  ~ which is available at https://www.eclipse.org/legal/epl-2.0/
  ~
  ~ SPDX-License-Identifier: EPL-2.0
--%>

<%@include file="/html/init.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ page import="com.liferay.portal.kernel.portlet.PortletURLFactoryUtil" %>
<%@ page import="org.eclipse.sw360.datahandler.thrift.projects.Project" %>
<%@ page import="javax.portlet.PortletRequest" %>
<%@ page import="org.eclipse.sw360.portal.common.PortalConstants"%>

<portlet:defineObjects/>
<liferay-theme:defineObjects/>

<liferay-portlet:renderURL var="friendlyPackageURL" portletName="sw360_portlet_packages">
    <portlet:param name="<%=PortalConstants.PAGENAME%>" value="<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_PAGENAME%>"/>
    <portlet:param name="<%=PortalConstants.PACKAGE_ID%>" value="<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_ID%>"/>
</liferay-portlet:renderURL>

<table id="linkedPackagesTable" class="table table-bordered"></table>
<%--for javascript library loading --%>
<%@ include file="/html/utils/includes/requirejs.jspf" %>

<script>
    AUI().use('liferay-portlet-url', function () {
        var PortletURL = Liferay.PortletURL;

        require(['jquery', 'modules/autocomplete', 'modules/dialog', 'bridges/datatables', 'utils/render' ], function($, autocomplete, dialog, datatables, render) {

            var tableData = [
                ["Microsoft", "microsoft.applicationinsights 2.11.0", "MIT", "nuget"],
                ["Microsoft", "microsoft.applicationinsights 2.3.0", "", "npm"],
                ["Microsoft", "microsoft.applicationinsights 2.4.0", "", "pip"],
                ["Microsoft", "microsoft.applicationinsights.agent.intercept 2.4.0", "", ""],
                ["Microsoft", "microsoft.applicationinsights.aspnetcore 2.8.0", "GPL-2.0", "nuget"],
                ["Microsoft", "microsoft.applicationinsights.dependencycollector 2.11.0", "", "nuget"],
                ["Microsoft", "microsoft.applicationinsights.eventcountercollector 2.11.0", "", "nuget"],
                ["Microsoft", "microsoft.applicationinsights.perfcountercollector 2.11.0", "MIT", "nuget"],
                ["Microsoft", "microsoft.applicationinsights.snapshotcollector 1.3.4", "", "npm"],
                ["Microsoft", "microsoft.applicationinsights.windowsserver 2.11.0", "", "nuget"]
            ];
            var packagesTable = createPackagesTable();
            // create and render data table
            function createPackagesTable() {
                let columns = [
                    {"title": "<liferay-ui:message key="vendor" />", width: "15%"},
                    {"title": "Package Name", render: {display: renderPackageNameLink}, width: "50%"},
                    {"title": "<liferay-ui:message key="main.licenses" />", width: "20%"},
                    {"title": "Package Type", width: "15%"}
                ];
                let printColumns = [0, 1, 2, 3];
                var packagesTable = datatables.create('#linkedPackagesTable', {
                    searching: true,
                    deferRender: false, // do not change this value
                    data: tableData,
                    columns: columns,
                    initComplete: datatables.showPageContainer,
                    language: {
                        url: "<liferay-ui:message key="datatables.lang" />",
                        loadingRecords: "<liferay-ui:message key="loading" />"
                    },
                    order: [
                        [1, 'asc']
                    ]
                }, printColumns);

                return packagesTable;
            }

            function renderPackageNameLink(data, type, row) {
                return render.linkTo(replaceFriendlyUrlParameter('<%=friendlyPackageURL%>'.replace(/projects/g, "packages"), row[1], '<%=PortalConstants.PAGENAME_DETAIL%>'), row[1]);
            }

            function replaceFriendlyUrlParameter(portletUrl, id, page) {
                return portletUrl
                    .replace('<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_PAGENAME%>', page)
                    .replace('<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_ID%>', id);
            }
        });
    });
</script>