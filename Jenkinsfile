#!/usr/bin/env groovy

@Library(['piper-lib', 'piper-lib-os']) _
@Library('txs-lib@v2.2.4')

import com.sap.jenkins.taxservice.PipelineParameters
import com.sap.jenkins.taxservice.Gitflow
import com.sap.jenkins.taxservice.BuildTrigger

Gitflow flow = new Gitflow(this)
PipelineParameters txsPipeline = new PipelineParameters(this)

milestonePipelineId = 'c2b7ec5e-46c8-41ce-a1a1-ff11625209b2'
releasePipelineId = '473047e4-45ac-41ae-88ee-bc3d65386780'

if (flow.isCommit()?.toMaster()) {
	properties([
			pipelineTriggers([cron('H 3 * * 1')])
	])
}else if (flow.isCommit()?.toDevelop()) {
	properties([
			pipelineTriggers([cron('H 3 * * 1-5')])
	])
}

try {
	flowSetupStage(script: this, projectName: 'com.sap.slh.tax-taxcalculationservice')

	flowLevel1Stage('Build') {
		node {
			measureDuration(script: this, measurementName: 'build_duration') {
				setVersion (
					script: this,
					buildTool: 'maven'
				)

				stashFiles(script: this) {
					executeBuild script: this, buildType: 'xMakeStage', xMakeBuildQuality: txsPipeline.getBuildQuality().toString()
				}
				testsPublishResults (
					script: this,
					junit: [updateResults: true, archive: true],
					jacoco: [archive: true],
					allowUnstableBuilds: false
				)
				stash includes: 'cumulus-configuration.json', name: 'cumulus-config'
				stash includes: '**/TEST*.xml', name: 'cumulus-junit'
				stash includes: '**/jacoco.xml', name: 'jacoco-coverage'
			}
		}
	}

	flowLevel2Stage('Static Code Checks - Sonar & Fortify') {
		parallel(
			  'Sonar' : {
			node {
			  executeSonarScan(
				script: this,
				useWebhook: true,
				options: "-Dsonar.projectKey=${txsPipeline.getSonarProjectKey()} -Dsonar.projectVersion=${this.globalPipelineEnvironment.getArtifactVersion()?.tokenize('-')?.get(0)}"
			  )
				stash includes: 'sonarscan-result.json', name: 'sonarqube'
			  timeout(time: 20, unit: 'MINUTES') {
				  def qg = waitForQualityGate()
				  if (qg.status != 'OK' && qg.status != 'WARN') {
					error "Pipeline aborted due to quality gate failure: ${qg.status}"
				  }
			  }
			}
			  },
			  'Fortify': {
				  node {
				  measureDuration(script: this, measurementName: 'fortify_duration') {
					  executeFortifyScan (
						script: this,
						environment: 'docker',
						scanType: 'maven',
						fortifyProjectName: txsPipeline.getProjectName(),
						fortifyProjectVersion: txsPipeline.getFortifyProjectVersion()
					  )
					  stash includes: '**/*.PDF', name: 'fortify'	
				  }
				}
			}, failFast: false
		)
	}

	flowLevel2Stage('Acceptance Tests - FIT') {
		node {
			unstash 'buildDescriptor'
			downloadArtifactsFromNexus (
				script: this,
				fromStaging: true,
				buildDescriptorFile: 'taxcalculationservice-service/pom.xml',
				disableLegacyNaming: true
			)
			cloudFoundryDeploy (
				script: this,
				deployType: 'standard',
				cloudFoundry: [
					apiEndpoint: 'https://api.cf.sap.hana.ondemand.com',
					credentialsId: 'CF_USER',
					org: 'CF_TaaS_TAASGS',
					space: 'DEV_IN',
					manifest: 'taxcalculationservice-service/manifest-acceptance.yml'
				]
			)
			sh("mvn test -Dtest=KarateTestRunner -DfailIfNoTests=false")
			cucumber ( fileIncludePattern: '**/target/surefire-reports/*.json')
			stash includes: '**/target/surefire-reports/*.json', name: 'cumulus-cucumber'
		}
	}

	flowLevel2Stage('E2E System Tests') {
		node {
			deleteDir()
			downloadArtifactsFromNexus (
				script: this,
				fromStaging: true,
				buildDescriptorFile: 'taxcalculationservice-service/pom.xml',
				disableLegacyNaming: true
			)
			githubPublishRelease (
				script: this,
				version: "build_${globalPipelineEnvironment.getArtifactVersion()}",
				assetPath: "$env.WORKSPACE/taxcalculationservice-service/target/taxcalculationservice-service.war"
			)
		}
		parallel(
			'Acceptance': {
				node {
					def result = build job: '../txs-integration-job/master',
					propagate: true,
					wait: true,
					parameters: [
						string(name: 'caller_job', value: "${env.BUILD_TAG}".toString()),
						string(name: 'service_name', value: "tax-calculation-service"),
						string(name: 'service_version', value: globalPipelineEnvironment.getArtifactVersion()),
						string(name: 'build_quality', value: txsPipeline.getBuildQuality().toString().toUpperCase()),
						string(name: 'test_type', value: txsPipeline.getAcceptanceTestType()),
						string(name: 'test_scope', value: "END_TO_END")
					]
					echo("Acceptance tests result: $result")
				}
			},
			'Performance': {
				if(((txsPipeline.getBuildTrigger() == BuildTrigger.TIMER) && flow.isCommit()?.toMaster()) || flow.isCommit()?.toRelease()) {
					node {
						def result = build job: '../txs-integration-job/master',
						propagate: false,
						wait: true,
						parameters: [
							string(name: 'caller_job', value: "${env.BUILD_TAG}".toString()),
							string(name: 'service_name', value: "tax-calculation-service"),
							string(name: 'service_version', value: globalPipelineEnvironment.getArtifactVersion()),
							string(name: 'build_quality', value: txsPipeline.getBuildQuality().toString().toUpperCase()),
							string(name: 'test_type', value: "PERFORMANCE"),
							string(name: 'test_scope', value: "APPLICATION")
						]
						echo("Performance tests result: $result")
					}
				} else {
					echo("[Gitflow] flowLevel2Stage: SKIP: Performance Stage")
				}
			}, failFast: false
		)
	}

	flowLevel2Stage('Dependencies Compliance', unstableOnError: flow.isLevel2Stage()) {
		node {
			deleteDir()
			unstash 'buildDescriptor'
			def wsProjectNames = txsPipeline.getWhiteSourceProjectNamesFromMaven()
			executeWhitesourceScan(
				script: this,
				scanType: 'maven',
				whitesourceProjectNames: wsProjectNames,
				securityVulnerabilities: 'true'
			)
			executePPMSComplianceCheck(
				script: this,
				scanType: 'whitesource',
				whitesourceProjectNames: wsProjectNames,
				ppmsBuildVersionCreation: true,
				ppmsBuildVersion: "\${version.major}.\${version.minor}.0",
				ppmsChangeRequestUpload: true
			)
		}
	}

	flowLevel2Stage('Cumulus Upload') {
		node {
			String version = this.globalPipelineEnvironment.getArtifactVersion()
			String pipelineId = getPipelineId()
			unstash 'cumulus-config'
			sapCumulusUpload (
				script: this,
				version: version,
				pipelineId: pipelineId,
				filePattern: "cumulus-configuration.json",
				stepResultType: "cumulus-upload"
			)
			unstash 'cumulus-cucumber'
			sapCumulusUpload (
				script: this,
				version: version,
				pipelineId: pipelineId,
				filePattern: "**/target/surefire-reports/*.json",
				stepResultType: "cucumber"
			)
			unstash 'cumulus-junit'
			sapCumulusUpload (
			script: this,
			version: version,
			pipelineId: pipelineId,
			filePattern: "**/TEST*.xml",
			stepResultType: "junit"
			)
			unstash 'jacoco-coverage'
			sapCumulusUpload (
			script: this, 
			version: version, 
			pipelineId: pipelineId,
			filePattern: "**/jacoco.xml",  
			stepResultType: "jacoco-coverage"
			)
			unstash 'sonarqube'
			sapCumulusUpload (
			script: this, 
			version: version, 
			pipelineId: pipelineId,
			filePattern: "sonarscan-result.json",  
			stepResultType: "sonarqube"
			)
			unstash 'fortify'
			sapCumulusUpload (
			script: this, 
			version: version, 
			pipelineId: pipelineId,
			filePattern: "**/*.PDF",  
			stepResultType: "fortify"
			)
			sapCumulusUpload (
			script: this, 
			version: version, 
			pipelineId: pipelineId,
			filePattern: "**/piper_whitesource_ppms_report.*",  
			stepResultType: "whitesource-ip"
			)
			sapCumulusUpload (
			script: this, 
			version: version, 
			pipelineId: pipelineId,
			filePattern: "whitesource-riskReport.pdf",  
			stepResultType: "whitesource-ip"
			)
		}

	}

	flowPromoteStage(script: this)

	flowDeployStage(
		script: this,
		manifestPath: 'taxcalculationservice-service',
		credentialsId: 'CF_USER'
	)
}
catch (Throwable err) {
	globalPipelineEnvironment.addError(this, err)
	throw err
}
finally {
	currentBuild.result = this.globalPipelineEnvironment.getBuildResult()
	if(flow.isCommit()?.toDevelop() || flow.isCommit()?.toMaster() || flow.isCommit()?.toRelease() ||
	   flow.isCommit()?.toBugfix() || flow.isCommit()?.toHotfix()) {
		slackSendNotification script: this
	}
}

def getPipelineId() {
	PipelineParameters txsPipeline = new PipelineParameters(this)

	if (txsPipeline.getBuildQuality().isRelease()) {
		return releasePipelineId
	}
	return milestonePipelineId
}

