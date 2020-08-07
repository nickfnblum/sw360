<%--
  ~ Copyright Siemens AG, 2020. Part of the SW360 Portal Project.
  ~
  ~ This program and the accompanying materials are made
  ~ available under the terms of the Eclipse Public License 2.0
  ~ which is available at https://www.eclipse.org/legal/epl-2.0/
  ~
  ~ SPDX-License-Identifier: EPL-2.0
  --%>
<%@ page import="javax.portlet.PortletRequest" %>
<%@ page import="com.liferay.portal.kernel.portlet.PortletURLFactoryUtil" %>
<%@ page import="org.eclipse.sw360.portal.common.PortalConstants" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/html/init.jsp" %>
<%-- the following is needed by liferay to display error messages--%>
<%@ include file="/html/utils/includes/errorKeyToMessage.jspf"%>

<portlet:defineObjects/>
<liferay-theme:defineObjects/>

<c:catch var="attributeNotFoundException">
    <jsp:useBean id="name" class="java.lang.String" scope="request"/>
    <jsp:useBean id="mainLicenses" class="java.lang.String" scope="request"/>
    <jsp:useBean id="vendor" class="java.lang.String" scope="request"/>
    <jsp:useBean id="packageType" class="java.lang.String" scope="request"/>
    <jsp:useBean id="packageUrl" class="java.lang.String" scope="request"/>
    <jsp:useBean id="createdBy" class="java.lang.String" scope="request"/>
    <jsp:useBean id="createdOn" class="java.lang.String" scope="request"/>
    <jsp:useBean id="linkedComponentRelease" class="java.lang.String" scope="request"/>
 </c:catch>

<%@include file="/html/utils/includes/logError.jspf" %>

<core_rt:if test="${empty attributeNotFoundException}">
<div class="container" style="display: none;">
	<div class="row">
		<div class="col-3 sidebar">
			<div id="detailTab" class="list-group" data-initial-tab="${selectedTab}" role="tablist">
			<a class="list-group-item list-group-item-action <core_rt:if test="${selectedTab == 'tab-Summary'}">active</core_rt:if>" href="#tab-Summary" data-toggle="list" role="tab"><liferay-ui:message key="summary" /></a>
		    </div>
	    </div>
	    <div class="col">
		<div class="row portlet-toolbar">
				<div class="col-auto">
                        <div class="btn-toolbar" role="toolbar">
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-primary" >Edit Package</button>
                            </div>
                        </div>
				</div>
				<div class="col portlet-title text-truncate" title="${name}">
					<sw360:out value="${name}"/>
				</div>
			</div>
			<div class="row">
				<div class="col">
		            <div class="tab-content">
		                <div id="tab-Summary" class="tab-pane <core_rt:if test="${selectedTab == 'tab-Summary'}">active show</core_rt:if>" >
		                    <table class="table label-value-table" id="componentOverview">
							    <thead>
							        <tr>
							            <th colspan="2"><liferay-ui:message key="general" /></th>
							        </tr>
							    </thead>
							    <tr>
							        <td><liferay-ui:message key="name" />:</td>
							        <td><sw360:out value="${name}"/></td>
							    </tr>
							    <tr>
							        <td><liferay-ui:message key="vendor" />:</td>
							        <td><sw360:out value="${vendor}"/></td>
							    </tr>
							    <tr>
							        <td>Package Type:</td>
							        <td><sw360:out value="${packageType}"/></td>
							    </tr>
							    <tr>
							        <td>Package URL</td>
							        <td><a href="</a><sw360:out value="${packageUrl}"/>" ><sw360:out value="${packageUrl}"/></a></td>
							    </tr>
							    <tr>
							        <td><liferay-ui:message key="main.licenses" />:</td>
							        <td><sw360:out value="${mainLicenses}"/></td>
							    </tr>
							    <tr>
							        <td>Linked Component Release:</td>
							        <td><a href="<sw360:DisplayReleaseLink releaseId="123456789abc" bare="true" scopeGroupId="${concludedScopeGroupId}" />">
                                        <sw360:out value="${linkedComponentRelease}" maxChar="60"/>
                                        </a>
                                    </td>
							    </tr>
							    <tr>
							        <td><liferay-ui:message key="created.on" />:</td>
							        <td><sw360:out value="${createdOn}"/></td>
							    </tr>
							    <tr>
							        <td><liferay-ui:message key="created.by" />:</td>
							        <td><sw360:DisplayUserEmail email="${createdBy}"/></td>
							    </tr>
							</table>
		                </div>
		            </div>
		        </div>
		    </div>
        </div>
    </div>
</div>
</core_rt:if>

<%--for javascript library loading --%>
<%@ include file="/html/utils/includes/requirejs.jspf" %>

<script>

	require(['jquery', 'modules/listgroup'], function($, listgroup) {
		listgroup.initialize('detailTab', $('#detailTab').data('initial-tab') || 'tab-Summary');
	});
</script>
