package net.kaleidos.taiga

import groovy.util.logging.Log4j
import net.kaleidos.domain.Issue
import net.kaleidos.domain.IssuePriority
import net.kaleidos.domain.IssueSeverity
import net.kaleidos.domain.IssueStatus
import net.kaleidos.domain.IssueType
import net.kaleidos.domain.Membership
import net.kaleidos.domain.Project
import net.kaleidos.domain.Role
import net.kaleidos.domain.User
import net.kaleidos.domain.Wikilink
import net.kaleidos.domain.Wikipage
import net.kaleidos.taiga.builder.IssueBuilder
import net.kaleidos.taiga.builder.IssuePriorityBuilder
import net.kaleidos.taiga.builder.IssueSeverityBuilder
import net.kaleidos.taiga.builder.IssueStatusBuilder
import net.kaleidos.taiga.builder.IssueTypeBuilder
import net.kaleidos.taiga.builder.MembershipBuilder
import net.kaleidos.taiga.builder.ProjectBuilder
import net.kaleidos.taiga.builder.RoleBuilder
import net.kaleidos.taiga.builder.UserBuilder
import net.kaleidos.taiga.builder.WikilinkBuilder
import net.kaleidos.taiga.builder.WikipageBuilder
import net.kaleidos.taiga.mapper.Mappers

@Log4j
class TaigaClient extends BaseClient {

    private static final Map URLS_IMPORTER = [
        projects: '/api/v1/importer',
    ]

    private static final Map URLS = [
        auth           : '/api/v1/auth',
        projects       : '/api/v1/projects',
        issueTypes     : '/api/v1/issue-types',
        issueStatuses  : '/api/v1/issue-statuses',
        issuePriorities: '/api/v1/priorities',
        issueSeverities: '/api/v1/severities',
        issues         : '/api/v1/issues',
        roles          : '/api/v1/roles',
        memberships    : '/api/v1/memberships',
        registerUsers  : '/api/v1/auth/register',
        wikis          : '/api/v1/wiki',
        wikiLinks      : '/api/v1/wiki-links',
    ]

    TaigaClient(String serverUrl) {
        super(serverUrl)
    }

    TaigaClient authenticate(String username, String password) {
        def params = [username: username, password: password, type: 'normal']

        def response = this.doPost(URLS.auth, params)
        client.httpClient.defaultHeaders = [Authorization: "Bearer ${response.auth_token}"]

        this
    }

    // PROJECT
    List<Map> getProjects() {
        this.doGet(URLS.projects)
    }

    Project getProjectById(Long projectId) {
        def json = this.doGet("${URLS.projects}/${projectId}")

        new ProjectBuilder().build(json)
    }

    Project createProject(Project project) {
        log.debug "Saving project ==> ${project.name}"

        def params = Mappers.map(project)
        def json = this.doPost(URLS_IMPORTER.projects, params)

        new ProjectBuilder().build(json)
    }

    void deleteProject(Project project) {
        log.debug "Deleting project ==> ${project.id} - ${project.name}"
        this.doDelete("${URLS.projects}/${project.id}")
    }



    // ROLES
    Role addRole(String name, Project project) {
        def params = [
            project: project.id,
            name   : name
        ]
        def json = this.doPost(URLS.roles, params)

        def role = new RoleBuilder().build(json)
        project.roles << role

        role
    }

    // MEMBERSHIPS
    Membership createMembership(String email, String role, Project project) {
        def params = [
            project: project.id,
            role   : project.findRoleByName(role).id,
            email  : email
        ]
        def json = this.doPost(URLS.memberships, params)

        def membership = new MembershipBuilder().build(json, project)
        project.memberships << membership

        membership
    }

    // ISSUES
    Issue createIssue(Project project, String type, String status, String priority, String subject, String description, String userEmail = "") {
        def params = [
            type       : project.findIssueTypeByName(type).id,
            status     : project.findIssueStatusByName(status).id,
            priority   : project.findIssuePriorityByName(priority).id,
            subject    : subject,
            description: description,
            project    : project.id,
            severity   : project.defaultSeverity
        ]

        def json = this.doPost(URLS.issues, params)

        new IssueBuilder().build(json, project)
    }

    // ISSUE TYPES
    IssueType addIssueType(String name, Project project) {
        def params = [project: project.id, name: name]
        def json = this.doPost(URLS.issueTypes, params)

        def issueType = new IssueTypeBuilder().build(json)
        project.issueTypes << issueType

        issueType
    }

    // ISSUE STATUSES
    IssueStatus addIssueStatus(String name, Boolean isClosed, Project project) {
        def params = [project: project.id, name: name, is_closed: isClosed]
        def json = this.doPost(URLS.issueStatuses, params)

        def issueStatus = new IssueStatusBuilder().build(json)
        project.issueStatuses << issueStatus

        issueStatus
    }

    // ISSUE PRIORITIES
    IssuePriority addIssuePriority(String name, Project project) {
        def params = [project: project.id, name: name]
        def json = this.doPost(URLS.issuePriorities, params)

        def issuePriority = new IssuePriorityBuilder().build(json)
        project.issuePriorities << issuePriority

        issuePriority
    }

    // ISSUE SEVERITIES
    IssueSeverity addIssueSeverity(String name, Project project) {
        def params = [project: project.id, name: name]
        def json = this.doPost(URLS.issueSeverities, params)

        def issueSeverity = new IssueSeverityBuilder().build(json)
        project.issueSeverities << issueSeverity

        issueSeverity
    }

    // USERS
    User registerUser(String email, String password, String token) {
        def params = [
            email    : email,
            token    : token,
            username : email,
            existing : false,
            full_name: email,
            password : password,
            type     : 'private',
        ]
        def json = this.doPost(URLS.registerUsers, params)

        new UserBuilder().build(json)
    }

    // WIKIS
    Wikipage createWiki(String slug, String content, Project project) {
        def params = [
            slug   : slug,
            content: content,
            project: project.id,
        ]
        def json = this.doPost(URLS.wikis, params)

        new WikipageBuilder().build(json, project)
    }

    Wikilink createWikiLink(String title, String href, Project project) {
        def params = [
            title  : title,
            href   : href,
            project: project.id
        ]
        def json = this.doPost(URLS.wikiLinks, params)

        new WikilinkBuilder().build(json, project)
    }
}