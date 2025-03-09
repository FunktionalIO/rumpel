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
    )
  )
)

ThisBuild / githubWorkflowGeneratedCI ++= List(releasePreparation)
ThisBuild / githubWorkflowPublishNeeds := List("prepare-release")
