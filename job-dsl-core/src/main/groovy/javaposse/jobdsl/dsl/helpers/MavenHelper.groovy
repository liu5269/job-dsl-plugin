package javaposse.jobdsl.dsl.helpers

import javaposse.jobdsl.dsl.WithXmlAction

import static com.google.common.base.Preconditions.checkState
import static javaposse.jobdsl.dsl.JobParent.getMaven

class MavenHelper extends AbstractHelper {

    Map<String, Object> jobArguments

    StringBuilder allGoals = new StringBuilder()
    StringBuilder allMavenOpts = new StringBuilder()
    boolean rootPOMAdded = false
    boolean perModuleEmailAdded = false
    boolean archivingDisabledAdded = false
    boolean runHeadlessAdded = false
    boolean ignoreUpstreamChangesAdded = false
    boolean jdkAdded = false

    MavenHelper(List<WithXmlAction> withXmlActions, Map<String, Object> jobArguments = [:]) {
        super(withXmlActions)
        this.jobArguments = jobArguments
    }

    /**
     * Specifies the path to the root POM.
     * @param rootPOM path to the root POM
     */
    def rootPOM(String rootPOM) {
        checkState jobArguments['type'] == maven, "rootPOM can only be applied for Maven jobs"
        checkState !rootPOMAdded, "rootPOM can only be applied once"
        rootPOMAdded = true
        execute { Node node ->
            appendOrReplaceNode node, 'rootPOM', rootPOM
        }
    }

    /**
     * Specifies the goals to execute.
     * @param goals the goals to execute
     */
    def goals(String goals) {
        checkState jobArguments['type'] == maven, "goals can only be applied for Maven jobs"
        if (allGoals.length() == 0) {
            allGoals.append goals
            execute { Node node ->
                appendOrReplaceNode node, 'goals', this.allGoals.toString()
            }
        } else {
            allGoals.append ' '
            allGoals.append goals
        }
    }

    /**
     * Specifies the JVM options needed when launching Maven as an external process.
     * @param mavenOpts JVM options needed when launching Maven
     */
    def mavenOpts(String mavenOpts) {
        checkState jobArguments['type'] == maven, "mavenOpts can only be applied for Maven jobs"
        if (allMavenOpts.length() == 0) {
            allMavenOpts.append mavenOpts
            execute { Node node ->
                appendOrReplaceNode node, 'mavenOpts', this.allMavenOpts.toString()
            }
        } else {
            allMavenOpts.append ' '
            allMavenOpts.append mavenOpts
        }
    }

    /**
     * If set, Jenkins will send an e-mail notifications for each module, defaults to <code>true</code>.
     * @param perModuleEmail set to <code>false</code> to disable per module e-mail notifications
     */
    def perModuleEmail(boolean perModuleEmail) {
        checkState jobArguments['type'] == maven, "perModuleEmail can only be applied for Maven jobs"
        checkState !perModuleEmailAdded, "perModuleEmail can only be applied once"
        perModuleEmailAdded = true
        execute { Node node ->
            appendOrReplaceNode node, 'perModuleEmail', perModuleEmail
        }
    }

    /**
     * If set, Jenkins  will not automatically archive all artifacts generated by this project, defaults to
     * <code>false</code>.
     * @param archivingDisabled set to <code>true</code> to disable automatic archiving
     */
    def archivingDisabled(boolean archivingDisabled) {
        checkState jobArguments['type'] == maven, "archivingDisabled can only be applied for Maven jobs"
        checkState !archivingDisabledAdded, "archivingDisabled can only be applied once"
        archivingDisabledAdded = true
        execute { Node node ->
            appendOrReplaceNode node, 'archivingDisabled', archivingDisabled
        }
    }

    /**
     * Set to allow Jenkins to configure the build process in headless mode, defaults to <code>false</code>.
     * @param runHeadless set to <code>true</code> to run the build process in headless mode
     */
    def runHeadless(boolean runHeadless) {
        checkState(jobArguments['type'] == maven, "runHeadless can only be applied for Maven jobs")
        checkState(!runHeadlessAdded, "runHeadless can only be applied once")
        runHeadlessAdded = true
        execute { Node node ->
            appendOrReplaceNode node, 'runHeadless', runHeadless
        }
    }

    /**
     * Name of the JDK installation to use for this job.
     * @param jdk name of the JDK installation to use for this job.
     */
    def jdk(String jdk) {
        checkState jobArguments['type'] == maven, "jdk can only be applied for Maven jobs"
        checkState !jdkAdded, "jdk can only be applied once"
        jdkAdded = true
        execute { Node node ->
            appendOrReplaceNode node, 'jdk', jdk
        }
    }

    private static void appendOrReplaceNode(Node node, String name, Object value) {
        node.children().removeAll { it instanceof Node && it.name() == name }
        node.appendNode name, value
    }
}
