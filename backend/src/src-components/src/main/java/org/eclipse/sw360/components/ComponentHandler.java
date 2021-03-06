/*
 * Copyright Siemens AG, 2013-2017. Part of the SW360 Portal Project.
 *
 * SPDX-License-Identifier: EPL-1.0
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.sw360.components;

import org.apache.thrift.TException;
import org.eclipse.sw360.datahandler.common.DatabaseSettings;
import org.eclipse.sw360.datahandler.db.ComponentDatabaseHandler;
import org.eclipse.sw360.datahandler.db.ComponentSearchHandler;
import org.eclipse.sw360.datahandler.db.ReleaseSearchHandler;
import org.eclipse.sw360.datahandler.thrift.*;
import org.eclipse.sw360.datahandler.thrift.attachments.Attachment;
import org.eclipse.sw360.datahandler.thrift.components.Component;
import org.eclipse.sw360.datahandler.thrift.components.ComponentService;
import org.eclipse.sw360.datahandler.thrift.components.Release;
import org.eclipse.sw360.datahandler.thrift.components.ReleaseLink;
import org.eclipse.sw360.datahandler.thrift.users.User;
import org.ektorp.http.HttpClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.eclipse.sw360.datahandler.common.SW360Assert.*;

/**
 * Implementation of the Thrift service
 *
 * @author cedric.bodet@tngtech.com
 * @author Johannes.Najjar@tngtech.com
 */
public class ComponentHandler implements ComponentService.Iface {

    private final ComponentDatabaseHandler handler;
    private final ComponentSearchHandler componentSearchHandler;
    private final ReleaseSearchHandler releaseSearchHandler;

    public ComponentHandler() throws IOException {
        this(DatabaseSettings.getConfiguredHttpClient(), DatabaseSettings.COUCH_DB_DATABASE, DatabaseSettings.COUCH_DB_ATTACHMENTS);
    }

    ComponentHandler(Supplier<HttpClient> httpClient, String dbName, String attachmentDbName) throws IOException {
        handler = new ComponentDatabaseHandler(httpClient, dbName, attachmentDbName);
        componentSearchHandler = new ComponentSearchHandler(httpClient, dbName);
        releaseSearchHandler = new ReleaseSearchHandler(httpClient, dbName);
    }

    // TODO use dependency injection instead of this constructors mess
    public ComponentHandler(ThriftClients thriftClients) throws IOException {
        this(DatabaseSettings.getConfiguredHttpClient(), DatabaseSettings.COUCH_DB_DATABASE, DatabaseSettings.COUCH_DB_ATTACHMENTS, thriftClients);
    }

    ComponentHandler(Supplier<HttpClient> httpClient, String dbName, String attachmentDbName, ThriftClients thriftClients) throws IOException {
        handler = new ComponentDatabaseHandler(httpClient, dbName, attachmentDbName, thriftClients);
        componentSearchHandler = new ComponentSearchHandler(httpClient, dbName);
        releaseSearchHandler = new ReleaseSearchHandler(httpClient, dbName);
    }

    /////////////////////
    // SUMMARY GETTERS //
    /////////////////////
    @Override
    public List<Component> getComponentsShort(Set<String> ids) {
        return handler.getComponentsShort(ids);
    }

    @Override
    public List<Component> getComponentSummary(User user) throws TException {
        assertUser(user);

        return handler.getComponentSummary(user);
    }

    @Override
    public List<Component> getRecentComponentsSummary(int limit, User user) throws TException {
        assertUser(user);

        return handler.getRecentComponentsSummary(limit, user);
    }

    @Override
    public int getTotalComponentsCount(User user) throws TException {
        assertUser(user);
        return handler.getTotalComponentsCount();
    }

    @Override
    public List<Release> getReleaseSummary(User user) throws TException {
        assertUser(user);

        return handler.getReleaseSummary();
    }

    @Override
    public List<Component> refineSearch(String text, Map<String, Set<String>> subQueryRestrictions) throws TException {
        return componentSearchHandler.search(text, subQueryRestrictions);
    }

    @Override
    public List<Component> getMyComponents(User user) throws TException {
        assertUser(user);

        return handler.getMyComponents(user.getEmail());
    }

    @Override
    public List<Release> searchReleases(String searchText) throws TException {
        return releaseSearchHandler.search(searchText);
    }

    @Override
    public List<Release> searchReleaseByNamePrefix(String name) throws TException {
        return handler.searchReleaseByNamePrefix(name);
    }

    @Override
    public List<Component> getSubscribedComponents(User user) throws TException {
        assertUser(user);

        return handler.getSubscribedComponents(user.getEmail());
    }

    @Override
    public List<Release> getSubscribedReleases(User user) throws TException {
        assertUser(user);

        return handler.getSubscribedReleases(user.getEmail());
    }

    @Override
    public List<Release> getRecentReleases() throws TException {
        return handler.getRecentReleases();
    }

    ////////////////////////////
    // GET INDIVIDUAL OBJECTS //
    ////////////////////////////

    @Override
    public Component getComponentById(String id, User user) throws TException {
        assertId(id);
        assertUser(user);

        return handler.getComponent(id, user);
    }

    @Override
    public Component getComponentByIdForEdit(String id, User user) throws TException {
        assertId(id);
        assertUser(user);

        return handler.getComponentForEdit(id, user);
    }

    @Override
    public Release getReleaseById(String id, User user) throws TException {
        assertId(id);
        assertUser(user);

        return handler.getRelease(id, user);
    }

    @Override
    public Release getReleaseByIdForEdit(String id, User user) throws TException {
        assertId(id);
        assertUser(user);

        return handler.getReleaseForEdit(id, user);
    }

    @Override
    public List<Release> getReleasesByIdsForExport(Set<String> ids) throws TException {
        assertNotNull(ids);
        return handler.getDetailedReleasesForExport(ids);
    }

    @Override
    public List<Release> getReleasesById(Set<String> ids, User user) throws TException {
        assertUser(user);
        assertNotNull(ids);
        return handler.getReleases(ids);
    }

    @Override
    public List<Release> getFullReleasesById(Set<String> ids, User user) throws TException {
        assertUser(user);
        assertNotNull(ids);
        return handler.getFullReleases(ids);
    }

    @Override
    public List<Release> getReleasesWithPermissions(Set<String> ids, User user) throws TException {
        assertUser(user);
        assertNotNull(ids);
        return handler.getReleasesWithPermissions(ids, user);
    }

    @Override
    public List<Release> getReleasesFromVendorId(String id, User user) throws TException {
        assertUser(user);
        assertNotNull(id);
        return handler.getReleasesFromVendorId(id, user);
    }

    @Override
    public List<Release> getReleasesFromVendorIds(Set<String> ids) throws TException {
        return handler.getReleasesFromVendorIds(ids);
    }

    ////////////////////////////
    // ADD INDIVIDUAL OBJECTS //
    ////////////////////////////

    @Override
    public AddDocumentRequestSummary addComponent(Component component, User user) throws TException {
        assertNotNull(component);
        assertIdUnset(component.getId());
        assertUser(user);

        return handler.addComponent(component, user.getEmail());
    }

    @Override
    public AddDocumentRequestSummary addRelease(Release release, User user) throws TException {
        assertNotNull(release);
        assertIdUnset(release.getId());
        assertUser(user);

        return handler.addRelease(release, user.getEmail());
    }

    ///////////////////////////////
    // UPDATE INDIVIDUAL OBJECTS //
    ///////////////////////////////

    @Override
    public RequestStatus updateComponent(Component component, User user) throws TException {
        assertNotNull(component);
        assertId(component.getId());
        assertUser(user);

        return handler.updateComponent(component, user);
    }

    @Override
    public RequestSummary updateComponents(Set<Component> components, User user) throws TException {
        assertUser(user);

        return handler.updateComponents(components, user);
    }

    public RequestStatus updateComponentFromModerationRequest(Component componentAdditions, Component componentDeletions, User user) {
        return handler.updateComponentFromAdditionsAndDeletions(componentAdditions, componentDeletions, user);
    }

    @Override
    public RequestStatus mergeComponents(String componentTargetId, String componentSourceId, Component componentSelection,
            User user) throws TException {
        return handler.mergeComponents(componentTargetId, componentSourceId, componentSelection, user);
    }

    @Override
    public RequestStatus updateRelease(Release release, User user) throws TException {
        assertNotNull(release);
        assertId(release.getId());
        assertUser(user);

        return handler.updateRelease(release, user, ThriftUtils.IMMUTABLE_OF_RELEASE);
    }

    @Override
    public RequestStatus updateReleaseFossology(Release release, User user) throws TException {
        assertNotNull(release);
        assertId(release.getId());
        assertUser(user);

        return handler.updateRelease(release, user, ThriftUtils.IMMUTABLE_OF_RELEASE_FOR_FOSSOLOGY);
    }

    @Override
    public RequestSummary updateReleases(Set<Release> releases, User user) throws TException {
        assertUser(user);
        return handler.updateReleases(releases, user);
    }

    public RequestStatus updateReleaseFromModerationRequest(Release releaseAdditions, Release releaseDeletions, User user) {
        return handler.updateReleaseFromAdditionsAndDeletions(releaseAdditions, releaseDeletions, user);
    }

    ///////////////////////////////
    // DELETE INDIVIDUAL OBJECTS //
    ///////////////////////////////

    @Override
    public RequestStatus deleteComponent(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.deleteComponent(id, user);
    }

    @Override
    public RequestStatus deleteRelease(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.deleteRelease(id, user);
    }

    @Override
    public List<Release> getReleasesByComponentId(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.getReleasesFromComponentId(id, user);

    }

    @Override
    public Set<Component> getUsingComponentsForRelease(String releaseId) throws TException {
        return handler.getUsingComponents(releaseId);
    }

    @Override
    public Set<Component> getUsingComponentsForComponent(Set<String> releaseIds) throws TException {
        return handler.getUsingComponents(releaseIds);
    }

    @Override
    public boolean releaseIsUsed(String releaseId) throws TException {
        return handler.checkIfInUse(releaseId);
    }

    @Override
    public boolean componentIsUsed(String componentId) throws TException {
        return handler.checkIfInUseComponent(componentId);
    }

    //////////////////////////////////
    // SUBSCRIBE INDIVIDUAL OBJECTS //
    //////////////////////////////////
    @Override
    public RequestStatus subscribeComponent(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.subscribeComponent(id, user);
    }

    @Override
    public RequestStatus subscribeRelease(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.subscribeRelease(id, user);
    }

    @Override
    public RequestStatus unsubscribeComponent(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.unsubscribeComponent(id, user);
    }

    @Override
    public RequestStatus unsubscribeRelease(String id, User user) throws TException {
        assertUser(user);
        assertId(id);

        return handler.unsubscribeRelease(id, user);
    }

    /////////////////////
    // EXCEL EXPORT    //
    /////////////////////
    @Override
    public List<Component> getComponentSummaryForExport() throws TException {
        return handler.getComponentSummaryForExport();
    }

    @Override
    public List<Component> getComponentDetailedSummaryForExport() throws TException {
        return handler.getComponentDetailedSummaryForExport();
    }

    @Override
    public List<Component> searchComponentForExport(String name) throws TException {
        return handler.searchComponentByNameForExport(name);
    }

    @Override
    public Component getComponentForReportFromFossologyUploadId(String uploadId) throws TException {
        return handler.getComponentForReportFromFossologyUploadId(uploadId);
    }

    @Override
    public Set<Attachment> getSourceAttachments(String releaseId) throws TException {
        return handler.getSourceAttachments(releaseId);
    }

    @Override
    public List<ReleaseLink> getLinkedReleases(Map<String, ProjectReleaseRelationship> relations) throws TException {
        assertNotNull(relations);

        return handler.getLinkedReleases(relations);
    }

    @Override
    public List<ReleaseLink> getLinkedReleaseRelations(Map<String, ReleaseRelationship> relations) throws TException {
        return handler.getLinkedReleases(relations);
    }

    @Override
    public Set<String> getUsedAttachmentContentIds() throws TException {
        return handler.getusedAttachmentContentIds();
    }

    @Override
    public Map<String, List<String>> getDuplicateComponents() throws TException {
        return handler.getDuplicateComponents();
    }

    @Override
    public Map<String, List<String>> getDuplicateReleases() throws TException {
        return handler.getDuplicateReleases();
    }

    @Override
    public Map<String, List<String>> getDuplicateReleaseSources() throws TException {
        return handler.getDuplicateReleaseSources();
    }

}
