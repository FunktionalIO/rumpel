import org.typelevel.sbt.gha.Permissions
import xerial.sbt.Sonatype.GitHubHosting
import xerial.sbt.Sonatype.sonatypeCentralHost

ThisBuild / tlBaseVersion := "0.1" // your current series x.y

ThisBuild / organization     := "io.funktional"
ThisBuild / organizationName := "FunktionalIO"
ThisBuild / homepage         := Some(url("https://github.com/FunktionalIO/rumpel"))
ThisBuild / startYear        := Some(2024)
ThisBuild / licenses         := Seq("EPL-2.0" -> url("https://www.eclipse.org/legal/epl-2.0/"))
ThisBuild / developers ++= List(
  // your GitHub handle and name
  tlGitHubDev("rlemaitre", "Raphaël Lemaitre")
)

ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("FunktionalIO", "rumpel", "github.com.lushly070@passmail.net"))
ThisBuild / scmInfo                := Some(
  ScmInfo(url("https://github.com/FunktionalIO/rumpel"), "scm:git:git@github.com:FunktionalIO/rumpel.git")
)

val Scala3 = "3.3.4"
ThisBuild / scalaVersion := Scala3 // the default Scala

ThisBuild / githubWorkflowOSes         := Seq("ubuntu-latest")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11"))

ThisBuild / tlCiHeaderCheck          := true
ThisBuild / tlCiScalafmtCheck        := true
ThisBuild / tlCiMimaBinaryIssueCheck := true
ThisBuild / tlCiDependencyGraphJob   := true
ThisBuild / autoAPIMappings          := true

val sharedSettings = Seq(
  organization   := "io.funktional",
  name           := "rumpel",
  scalaVersion   := "3.3.4",
  libraryDependencies ++= Seq(
    "org.scalameta" %%% "munit" % "1.0.2" % Test
  ),
  // Headers
  headerMappings := headerMappings.value + (HeaderFileType.scala -> HeaderCommentStyle.cppStyleLineComment),
  headerLicense  := Some(HeaderLicense.Custom(
    """|Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
               |This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
               |For more information see LICENSE or https://opensource.org/license/epl-2-0
               |""".stripMargin
  ))
)

lazy val root =
    project
        .in(file("."))
        .aggregate(rumpel.js, rumpel.jvm, rumpel.native, unidocs)
        .settings(sharedSettings)
        .settings(
          publish / skip := true
        )

lazy val rumpel  =
    crossProject(JSPlatform, JVMPlatform, NativePlatform)
        .withoutSuffixFor(JVMPlatform)
        .crossType(CrossType.Pure)
        .in(file("core"))
        .settings(sharedSettings)
        .jvmSettings(
          // Add JVM-specific settings here
        ).jsSettings(
          // Add JS-specific settings here
          scalaJSUseMainModuleInitializer := true
        ).nativeSettings(
          // Add Native-specific settings here
        )
lazy val unidocs = project
    .in(file("unidocs"))
    .enablePlugins(TypelevelUnidocPlugin)
    .settings(
      name                                       := "rumpel-docs",
      ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(rumpel.jvm)
    )
    .settings(sharedSettings)

// Github actions

val releasePreparation = WorkflowJob(
  id = "prepare-release",
  name = "👷 Prepare release",
  oses = List("ubuntu-latest"),
  cond = Some(
    """github.event_name != 'pull_request' && (startsWith(github.ref, 'refs/tags/v') || startsWith(github.ref, 'refs/heads/main'))"""
  ),
  needs = List("build"),
  env = Map("DTC_HEADLESS" -> "true"),
  permissions = Some(Permissions.Specify.defaultPermissive),
  steps = List(
    WorkflowStep.CheckoutFull,
    WorkflowStep.Run(
      name = Some("Get previous tag"),
      id = Some("previousTag"),
      cond = Some("""startsWith(github.ref, 'refs/tags/v')"""),
      commands = List(
        """name=$(git --no-pager tag --sort=creatordate --merged ${{ github.ref_name }} | tail -2 | head -1)""",
        """ref_name="${{ github.ref_name }}"""",
        """prefix="prepare-"""",
        """next_version=${ref_name/#$prefix}""",
        """echo "previousTag=$name"""",
        """echo "previousTag=$name" >> $GITHUB_ENV""",
        """echo "nextTag=$next_version"""",
        """echo "nextTag=$next_version" >> $GITHUB_ENV"""
      )
    ),
    WorkflowStep.Use(
      name = Some("Update CHANGELOG"),
      id = Some("changelog"),
      ref = UseRef.Public("requarks", "changelog-action", "v1"),
      cond = Some("""startsWith(github.ref, 'refs/tags/v')"""),
      params = Map(
        "token"       -> "${{ github.token }}",
        "fromTag"     -> "${{ github.ref_name }}",
        "toTag"       -> "${{ env.previousTag }}",
        "writeToFile" -> "true"
      )
    ),
    WorkflowStep.Use(
      name = Some("Commit CHANGELOG.md"),
      ref = UseRef.Public("stefanzweifel", "git-auto-commit-action", "v5"),
      cond = Some("""startsWith(github.ref, 'refs/tags/v')"""),
      params = Map(
        "commit_message" -> "docs: update CHANGELOG.md for ${{ env.nextTag }} [skip ci]",
        "branch"         -> "main",
        "file_pattern"   -> "CHANGELOG.md docToolchainConfig.groovy"
      )
    ),
    WorkflowStep.Use(
      name = Some("Create version tag"),
      ref = UseRef.Public("rickstaa", "action-create-tag", "v1"),
      cond = Some("""startsWith(github.ref, 'refs/tags/v')"""),
      params = Map(
        "tag"            -> "${{ github.ref_name }}",
        "message"        -> "Release ${{ github.ref_name }}",
        "force_push_tag" -> "true" // force push the tag to move it to HEAD
      )
    ),
    WorkflowStep.Use(
      name = Some("Create release"),
      ref = UseRef.Public("ncipollo", "release-action", "v1.14.0"),
      cond = Some("""startsWith(github.ref, 'refs/tags/v')"""),
      params = Map(
        "allowUpdates" -> "true",
        "draft"        -> "false",
        "makeLatest"   -> "true",
        "name"         -> "${{ github.ref_name }}",
        "tag"          -> "${{ github.ref_name }}",
        "body"         -> "${{ steps.changelog.outputs.changes }}",
        "token"        -> "${{ github.token }}"
      )
    )
  )
)
ThisBuild / githubWorkflowGeneratedCI ++= List(releasePreparation)
ThisBuild / githubWorkflowPublishNeeds := List("prepare-release")
