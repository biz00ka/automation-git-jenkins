// Jenkinsfile
pipeline {
    agent any

    environment {
        // SonarQube authentication token (securely store in Jenkins credentials)
        SONAR_AUTH_TOKEN = credentials('jenkins token')
        // Replace with your SonarQube project key
        SONAR_PROJECT_KEY = 'automation-git-jenkins'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/biz00ka/automation-git-jenkins.git'
            }
        }

        stage('Maven Build & Test') {
            steps {
                sh 'mvn clean install -DskipTests' // Build without running tests initially to ensure compilation
            }
        }

        stage('Run Automated Tests') {
            steps {
                sh 'mvn test' // Run TestNG tests
            }
            post {
                always {
                    // Publish TestNG XML results
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Quality Analysis (SonarQube)') {
            steps {
                withSonarQubeEnv(installationName: 'sonar server', credentialsId: 'sonar-token') { // Ensure this matches your Jenkins credential ID
                                    sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.sources=src/test/java"
                                }
            }
        }

        stage('Publish Extent Reports') {
            steps {
                script {
                    // Check if Extent Reports exist before publishing
                    def extentReportDir = "test-output/ExtentReports"
                    def extentReportHtml = "${extentReportDir}/index.html"
                    if (fileExists(extentReportHtml)) {
                        publishHTML(target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: extentReportDir,
                            reportFiles: 'index.html',
                            reportName: 'Extent Test Report',
                            reportTitles: ''
                        ])
                    } else {
                        echo "Extent Reports not found at ${extentReportDir}. Skipping publishing."
                    }
                }
            }
        }

        stage('Cleanup') {
            steps {
                deleteDir() // Cleans up the workspace after the pipeline completes
            }
        }
    }

    post {
        always {
            // Send email notifications or Slack notifications based on build status
             mail to: 'siddharthdubey45@gmail.com', subject: "Pipeline ${currentBuild.fullDisplayName} Status: ${currentBuild.result}", body: "Check build log at ${env.BUILD_URL}"
            // For Slack: slackSend channel: '#your-slack-channel', message: "Pipeline ${currentBuild.fullDisplayName} Status: ${currentBuild.result} - ${env.BUILD_URL}"
        }
    }
}