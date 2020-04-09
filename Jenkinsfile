#!/usr/bin/env groovy

DEV_TEAM = 'ajovanovic@corelogic.com,iazarny@corelogic.com'
slackURL = 'https://hooks.slack.com/services/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'
payload_started = "{\"attachments\":[{\"fallback\": \"Required plain-text summary of the attachment.\",\"color\":\"#f2f542\",\"pretext\":\"STARTED\",\"title\":\"${env.JOB_NAME}\",\"title_link\":\"${env.BUILD_URL}\",\"text\":\"${env.BRANCH_NAME} [${env.BUILD_NUMBER}]\"}]}"
payload_failed =  "{\"attachments\":[{\"fallback\": \"Required plain-text summary of the attachment.\",\"color\":\"#f54242\",\"pretext\":\"FAILED\",\"title\":\"${env.JOB_NAME}\",\"title_link\":\"${env.BUILD_URL}\",\"text\":\"${env.BRANCH_NAME} [${env.BUILD_NUMBER}]\"}]}"
payload_success = "{\"attachments\":[{\"fallback\": \"Required plain-text summary of the attachment.\",\"color\":\"#16e042\",\"pretext\":\"SUCCESS\",\"title\":\"${env.JOB_NAME}\",\"title_link\":\"${env.BUILD_URL}\",\"text\":\"${env.BRANCH_NAME} [${env.BUILD_NUMBER}]\"}]}"

pipeline {
    agent {
        
        label 'linux'
    }
    environment{
            PATH = "/usr/local/bin:${PATH}"
            JAVA_HOME = tool 'Java8'    
    }

    options {
        timestamps()
    }

    stages{
        stage('Cull Builds') {
            steps {
                milestone(ordinal: Integer.parseInt(env.BUILD_ID) -1)
                milestone(ordinal: Integer.parseInt(env.BUILD_ID))
            }
        }
        stage('Prepare'){
            when {
                anyOf{
                    branch "staging"
                    branch "uat"
                    branch "master"
                }
            }
            steps{
                sh "curl -k -X POST --data-urlencode \'payload=${payload_started}\' ${slackURL}"
            }
        }

        stage('Build'){
            steps{
                sh "chmod +x gradlew"
                sh "./gradlew clean build -x test -x check --console=plain --stacktrace --rerun-tasks --refresh-dependencies"
            }
        }

        stage('Codestyle') {
            steps{
                sh './gradlew check -x test'
            }
        }

        stage('Test') {
            steps{
                sh './gradlew test -i -PexcludeTests=**/Accu*'
                sh './gradlew test -i --tests AccuZip*'
                sh './gradlew test -i --tests AccuConnect*'
            }
        }

        stage('Store Artifact'){
            when {
                branch "master"
            }
            steps{
                nexusArtifactUploader artifacts: [[artifactId: 'accuservice', classifier: '', file: 'build/libs/accuservice-0.0.1-SNAPSHOT.jar', type: 'jar']], credentialsId: '8c09494a-0280-4fb4-95b8-27db0f24a585', groupId: 'toolbox.accuservice', nexusUrl: 'repo2.corelogic.net', nexusVersion: 'nexus3', protocol: 'https', repository: 'homevisit', version: ''
            }
        }

        stage('Sonar Analysis') {
            when {
                branch "master"
            }
            steps{
                echo "SonarQube Analysis and QG check"
                echo "Sonar analysis"
                withSonarQubeEnv('sonarqube') {
                    sh './gradlew sonarqube --info -x test -PignoreFailedTests=true -Dsonar.host.url=http://hub2.corelogic.net:9900'
                    echo "QualityGate check"
                }
                timeout(time: 25, unit: 'MINUTES') {
                    waitForQualityGate()
                }
            }
        }

        stage('Report') {
            when {
                anyOf{
                    branch "staging"
                    branch "uat"
                    branch "master"
                }
            }
            steps{
                script{
                    def subject = "Build job ${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - results"
                    def body = """Build job ${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - results here  ${env.BUILD_URL} \n
                        Junit report ${env.JOB_URL}JUnit_HTML_Report/ \n
                        Checkstyle report ${env.JOB_URL}Checkstyle_Report/ \n
                        Pmd report ${env.JOB_URL}Pmd_Report/ \n"""

                    //------------------------------------
                    def changes = "\nChanges:\n"
                    build = currentBuild
                    while (build != null && build.result != 'SUCCESS') {
                        changes += "In ${build.id}:\n"
                        for (changeLog in build.changeSets) {
                            for (entry in changeLog.items) {
                                changes += "* ${entry.msg} by ${entry.author} \n"
                            }

                        }
                        build = build.previousBuild
                    }
                    //------------------------------------
                    body += changes
                    emailext attachLog: true, body: body, subject: subject, from: 'jenkins@corelogic.com', to: DEV_TEAM
                }
            }
        }

        stage('Deploy'){
            when {
                anyOf{
                    branch "staging"
                    branch "uat"
                    branch "master"
                }
            }
            steps{
            script{
                if (env.BRANCH_NAME == 'staging'){
                    env.BUILD_ENVIRONMENT = 'stg'
                    cf_api = 'https://api.sys.pcfusw1prd.solutions.corelogic.com'
                    cf_org = 'real_estate_us-prd'
                    cf_credentials = 'd3611420-505d-4d48-b84a-7d01a82bfa2a'
                    env.CF_APP_NAME = 'homevisit-accuservice-stg'
                    env.CF_DOMAIN = 'apps.pcfusw1prd.solutions.corelogic.com'
                } else if (env.BRANCH_NAME == 'uat'){
                    env.BUILD_ENVIRONMENT = 'uat'
                    cf_api = 'https://api.sys.pcfusw1prd.solutions.corelogic.com'
                    cf_org = 'real_estate_us-prd'
                    cf_credentials = 'd3611420-505d-4d48-b84a-7d01a82bfa2a'
                    env.CF_APP_NAME = 'homevisit-accuservice-uat'
                    env.CF_DOMAIN = 'apps.pcfusw1prd.solutions.corelogic.com'
                } else if (env.BRANCH_NAME == 'master'){
                    env.BUILD_ENVIRONMENT = 'prd'
                    cf_api = 'https://api.sys.pcfusc1prd.solutions.corelogic.com'
                    cf_org = 'real_estate_us-prd'
                    cf_credentials = 'd3611420-505d-4d48-b84a-7d01a82bfa2a'
                    env.CF_APP_NAME = 'homevisit-accuservice'
                    env.CF_DOMAIN = 'apps.pcfusc1prd.solutions.corelogic.com'
                } else {
                error('Stopping early…')
                }
            }
            withCfCli(
                apiEndpoint: cf_api,
                cloudFoundryCliVersion: 'CloudFoundryCLI',
                credentialsId: cf_credentials,
                organization: cf_org,
                space: 'Homevisit') {
                    script{
                        env.CF_TEMP_APP_EXISTS = sh(script: "cf apps | awk '{print \$1}' | grep -xoh ${CF_APP_NAME}-temp", ,returnStatus: true) == 0
                        env.CF_APP_EXISTS = sh(script: "cf apps | awk '{print \$1}' | grep -xoh ${CF_APP_NAME}", ,returnStatus: true) == 0
                        if (env.CF_APP_EXISTS == 'false') {
                            sh 'cf push -f ./manifests/manifest.'+ env.BUILD_ENVIRONMENT +'.yml'
                            sh 'cf stop ${CF_APP_NAME}-temp' 
                        }
                        else if (env.CF_APP_EXISTS == 'true') {
                            if (env.CF_TEMP_APP_EXISTS == 'true') {
                                sh 'cf delete ${CF_APP_NAME}-temp -f'
                            }
                            sh 'cf rename ${CF_APP_NAME} ${CF_APP_NAME}-temp'
                            sh 'cf push -f ./manifests/manifest.'+ env.BUILD_ENVIRONMENT +'.yml'
                            sh 'cf stop ${CF_APP_NAME}-temp'
                        }
                    }               
                }
            }
        }
    }

    post {
      always{
        publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/tests/test', reportFiles: 'index.html', reportName: 'JUnit HTML Report'])
        publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/checkstyle', reportFiles: 'main.html', reportName: 'Checkstyle Report'])
        publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/pmd', reportFiles: 'main.html', reportName: 'Pmd Report'])
        deleteDir()
      }
      success{
        sh "curl -k -X POST --data-urlencode \'payload=${payload_success}\' ${slackURL}"
      }
      failure{
        sh "curl -k -X POST --data-urlencode \'payload=${payload_failed}\' ${slackURL}"
        }
    }
}
