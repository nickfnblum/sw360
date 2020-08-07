<%--
  ~ Copyright Siemens AG, 2020. Part of the SW360 Portal Project.
  - With contributions by Bosch Software Innovations GmbH, 2016-2017.
  ~
  ~ This program and the accompanying materials are made
  ~ available under the terms of the Eclipse Public License 2.0
  ~ which is available at https://www.eclipse.org/legal/epl-2.0/
  ~
  ~ SPDX-License-Identifier: EPL-2.0
  --%>
<%@ page import="com.liferay.portal.kernel.portlet.PortletURLFactoryUtil" %>
<%@ page import="javax.portlet.PortletRequest" %>
<%@ page import="org.eclipse.sw360.portal.common.PortalConstants" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ include file="/html/init.jsp" %>
<%-- the following is needed by liferay to display error messages--%>
<%@ include file="/html/utils/includes/errorKeyToMessage.jspf"%>

<portlet:defineObjects/>
<liferay-theme:defineObjects/>
<jsp:useBean id="Testing" class="java.lang.String" scope="request" />

<%-- <div class="container">
    <div class="row portlet-toolbar">
        <div class="col portlet-title text-truncate" title="<liferay-ui:message key="administration" />">
            <liferay-ui:message key="packages" />
        </div>
    </div>
    <div class="row">
        <div class="col">
            <sw360:out value="${Testing}" />
        </div>
    </div>
</div> --%>

<portlet:renderURL var="friendlyPackageURL">
    <portlet:param name="<%=PortalConstants.PAGENAME%>" value="<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_PAGENAME%>"/>
    <portlet:param name="<%=PortalConstants.PACKAGE_ID%>" value="<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_ID%>"/>
</portlet:renderURL>

<liferay-portlet:renderURL var="friendlyLicenseURL" portletName="sw360_portlet_licenses">
    <portlet:param name="<%=PortalConstants.PAGENAME%>" value="<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_PAGENAME%>"/>
    <portlet:param name="<%=PortalConstants.LICENSE_ID%>" value="<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_ID%>"/>
</liferay-portlet:renderURL>

<div class="container" style="display: none;">
	<div class="row">
		<div class="col-3 sidebar">
			<div class="card-deck">
				<div id="searchInput" class="card">
					<div class="card-header">
						<liferay-ui:message key="advanced.search" />
					</div>
                    <div class="card-body">
                        <form method="post">
                            <div class="form-group">
                                <label for="package_name">Package Name</label>
                                <input type="text" class="form-control form-control-sm"
                                    value="" id="package_name">
                            </div>
                            <div class="form-group">
                                <label for="package_type">Package Type</label>
                                <select class="form-control form-control-sm" id="package_type" name="Package Type">
                                    <option value="<%=PortalConstants.NO_FILTER%>" class="textlabel stackedLabel"></option>
                                    <option value="nuget" class="textlabel stackedLabel">nuget</option>
                                    <option value="npm" class="textlabel stackedLabel">npm</option>
                                    <option value="pip" class="textlabel stackedLabel">pip</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="main_licenses">Main Licenses</label>
                                <input type="text" class="form-control form-control-sm"
                                    value="" id="main_licenses">
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm btn-block"><liferay-ui:message key="search" /></button>
				        </form>
					</div>
				</div>
			</div>
		</div>
		<div class="col">
            <div class="row portlet-toolbar">
				<div class="col-auto">
					<div class="btn-toolbar" role="toolbar">
                        <div class="btn-group" role="group">
							<button type="button" class="btn btn-primary">Add Package</button>
							<button type="button" class="btn btn-secondary">Import Packages BOM</button>
						</div>
						<div id="btnExportGroup" class="btn-group" role="group">
							<button id="btnExport" type="button" class="btn btn-secondary">Export Package SPDX</button>
						</div>
					</div>
				</div>
                <div class="col portlet-title text-truncate" title="<liferay-ui:message key="packages" />">
					<liferay-ui:message key="packages" />
				</div>
            </div>

            <div class="row">
                <div class="col">
			        <table id="packagesTable" class="table table-bordered"></table>
                </div>
            </div>

		</div>
	</div>
</div>
<%@ include file="/html/utils/includes/pageSpinner.jspf" %>

<div class="dialogs auto-dialogs"></div>
<%--for javascript library loading --%>
<%@ include file="/html/utils/includes/requirejs.jspf" %>

<script>
    AUI().use('liferay-portlet-url', function () {
        var PortletURL = Liferay.PortletURL;

        require(['jquery', 'modules/autocomplete', 'modules/dialog', 'bridges/datatables', 'utils/render' ], function($, autocomplete, dialog, datatables, render) {

            var tableData = [
                ["Microsoft", "microsoft.applicationinsights/2.11.0", "MIT", "nuget", ""],
                ["Microsoft", "microsoft.applicationinsights/2.3.0", "", "npm", ""],
                ["Microsoft", "microsoft.applicationinsights/2.4.0", "", "pip", ""],
                ["Microsoft", "microsoft.applicationinsights.agent.intercept/2.4.0", "", "", ""],
                ["Microsoft", "microsoft.applicationinsights.aspnetcore/2.8.0", "GPL-2.0", "nuget", ""],
                ["Microsoft", "microsoft.applicationinsights.dependencycollector/2.11.0", "", "nuget", ""],
                ["Microsoft", "microsoft.applicationinsights.eventcountercollector/2.11.0", "", "nuget", ""],
                ["Microsoft", "microsoft.applicationinsights.perfcountercollector/2.11.0", "MIT", "nuget", ""],
                ["Microsoft", "microsoft.applicationinsights.snapshotcollector/1.3.4", "", "npm", ""],
                ["Microsoft", "microsoft.applicationinsights.windowsserver/2.11.0", "", "nuget", ""],
                ["Microsoft", "microsoft.applicationinsights.windowsserver.telemetrychannel/2.11.0", "Apache-2.0", "nuget", ""],
                ["Microsoft", "microsoft.aspnetcore.authentication.abstractions/2.1.0", "", "", ""],
                ["Microsoft", "microsoft.aspnetcore.authentication.core/2.1.0", "MIT", "", ""],
                ["Microsoft", "microsoft.aspnetcore.authorization/2.1.0", "", "", ""],
                ["Microsoft", "microsoft.aspnetcore.authorization.policy/2.1.0", "", "pip", ""],
                ["Microsoft", "microsoft.aspnetcore.hosting/1.0.2", "MIT", "npm", ""],
                ["Microsoft", "microsoft.aspnetcore.hosting.abstractions/1.0.2", "Apache-2.0", "npm", ""],
                ["Microsoft", "microsoft.aspnetcore.hosting.abstractions/2.1.0", "Apache-2.0", "pip", ""]
            ];
            var packagesTable = createPackagesTable();
            // create and render data table
            function createPackagesTable() {
                let columns = [
                    {"title": "<liferay-ui:message key="vendor" />", width: "15%"},
                    {"title": "Package Name", render: {display: renderPackageNameLink}, width: "45%"},
                    {"title": "<liferay-ui:message key="main.licenses" />", width: "20%"},
                    {"title": "Package Type", width: "10%"},
                    {"title": "<liferay-ui:message key="actions" />", render: {display: renderPackageActions}, className: 'two actions', orderable: false, width: "10%"}
                ];
                let printColumns = [0, 1, 2, 3];
                var packagesTable = datatables.create('#packagesTable', {
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

            // helper functions
            function makePackageUrl(packageId, page) {
                var portletURL = PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, portletDisplay.getId(), themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>')
                    .setParameter('<%=PortalConstants.PAGENAME%>', page)
                    .setParameter('<%=PortalConstants.PACKAGE_ID%>', packageId);
                return portletURL.toString();
            }

            function renderPackageActions(id, type, row) {
                var $actions = $('<div>', {
				    'class': 'actions'
                    }),
                    $editAction = $('<svg>', {
                        'class': 'edit lexicon-icon',
                    }),
                    $deleteAction = $('<svg>', {
                        'class': 'delete lexicon-icon',
                    });

                $editAction.append($('<title><liferay-ui:message key="edit" /></title><use href="/o/org.eclipse.sw360.liferay-theme/images/clay/icons.svg#pencil"/>'));
                $deleteAction.append($('<title>Delete</title><use href="/o/org.eclipse.sw360.liferay-theme/images/clay/icons.svg#trash"/>'));

                $actions.append($editAction, $deleteAction);
                return $actions[0].outerHTML;
            }

            function renderPackageNameLink(data, type, row) {
                return render.linkTo(makePackageUrl(row[1], '<%=PortalConstants.PAGENAME_DETAIL%>'), row[1]);
            }

            function replaceFriendlyUrlParameter(portletUrl, id, page) {
                return portletUrl
                    .replace('<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_PAGENAME%>', page)
                    .replace('<%=PortalConstants.FRIENDLY_URL_PLACEHOLDER_ID%>', id);
            }
        });
    });
</script>