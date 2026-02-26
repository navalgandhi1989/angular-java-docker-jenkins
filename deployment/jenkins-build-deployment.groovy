pipeline {
    agent any

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build and deploy')
        choice(name: 'TARGET_ENVIRONMENT', choices: ["UAT", "PRODUCTION"], description: 'Target Environment')
    }

    environment {
        BUILD_TAG = 'latest'
        UI_BUILD_NUMBER = 'latest'
        BACKEND_BUILD_NUMBER = 'latest'
    }

    stages {
        stage('Build Docker Images') {
            steps {
                script {
                    def sampleAppBuild = build job: 'sample-app-build',
                          propagate: false,
                          wait: true, 
                          parameters: [
                              string(name: 'BRANCH_NAME', value: params.BRANCH_NAME)
                          ]
                    
                    BUILD_TAG = sampleAppBuild.displayName
                    echo "BUILD_TAG is: ${BUILD_TAG}"

                    if (BUILD_TAG == 'latest') {
                        error "Failed to capture BUILD_TAG from the sample-app-build."
                    }
                    
                    if (sampleAppBuild.result != 'SUCCESS') {
                        error "sample-app-build failed with status: ${sampleAppBuild.result}"
                    }
                }
            }
        }

        stage('Deploy Docker Images') {
            steps {
                script {
                    // Use the captured BUILD_TAG for deployment
                    def UI_BUILD_NUMBER = BUILD_TAG
                    def BACKEND_BUILD_NUMBER = BUILD_TAG
                    def TARGET_ENVIRONMENT = params.TARGET_ENVIRONMENT

                    def sampleAppDeployment = build job: 'sample-app-deployment',
                          propagate: false,
                          wait: true, 
                          parameters: [
                              string(name: 'UI_BUILD_NUMBER', value: UI_BUILD_NUMBER),
                              string(name: 'BACKEND_BUILD_NUMBER', value: BACKEND_BUILD_NUMBER),
                              string(name: 'TARGET_ENVIRONMENT', value: TARGET_ENVIRONMENT)
                          ]
                                        
                    if (sampleAppDeployment.result != 'SUCCESS') {
                        error "sample-app-deployment failed with status: ${sampleAppDeployment.result}"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
        success {
            echo 'Pipelines executed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
