import org.typelevel.sbt.gha.Permissions

ThisBuild / githubWorkflowOSes         := Seq("ubuntu-latest")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.graalvm("21"))

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
      cond = Some(
        """github.ref != 'refs/tags/v0.0.1' && startsWith(github.ref, 'refs/tags/v')startsWith(github.ref, 'refs/tags/v')"""
      ),
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
      cond = Some("""github.ref != 'refs/tags/v0.0.1' && startsWith(github.ref, 'refs/tags/v')"""),
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
      ref = UseRef.Public("ncipollo", "release-action", "v1.15.0"),
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
