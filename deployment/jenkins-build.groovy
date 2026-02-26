pipeline {
agent any

environment {
  GIT_CREDENTIALS_ID = 'Github-Jenkins-Service-Account'
  BUILD_TAG = 'latest'
}

parameters {
  string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
}

stages {

stage('Build Name') {
  steps {
    script {
        // Generate a timestamp for the tag
        BUILD_TAG = new Date().format("yyMMdd-HHm")
        currentBuild.displayName = BUILD_TAG
    }
  }
}

stage('Checkout') {
  steps {
    script {
        echo "Checking out branch: ${params.BRANCH_NAME}"
        git branch: "${params.BRANCH_NAME}",
        url: 'https://github.com/navalgandhi1989/angular-java-docker-jenkins.git'
    }
  }
}

stage('Build Docker Images') {
  steps {
    script {
        // Build Docker images
        dir("${WORKSPACE}") {
            echo "----------------- Comencing build process -----------------"
            sh 'docker compose -f deployment/docker-compose.build.yml build'
        }
    }
  }
}

stage('Tag Docker Images') {
  steps {
    script {
        // Tag Docker images
        dir("${WORKSPACE}") {
  
            sh "docker tag sample-app-frontend sample-app-frontend:${BUILD_TAG}"
            sh "docker tag sample-app-backend sample-app-backend:${BUILD_TAG}"
  
            sh "docker image tag sample-app-frontend:${BUILD_TAG} ghcr.io/navalgandhi1989/angular-java-docker-jenkins/sample-app-frontend:${BUILD_TAG}"
            sh "docker image tag sample-app-backend:${BUILD_TAG} ghcr.io/navalgandhi1989/angular-java-docker-jenkins/sample-app-backend:${BUILD_TAG}"
            
            echo "----------------- Tagged Docker images Build:${BUILD_TAG}  -----------------"
        }
  
    }
  }
}

stage('Push Docker Images to registry') {
  steps {
    script {
        // Push Docker images 
        dir("${WORKSPACE}") {
            echo "----------------- Login to registry -----------------"
            docker.withRegistry('https://ghcr.io', GIT_CREDENTIALS_ID) {
                
                def uiImage = docker.image("ghcr.io/navalgandhi1989/angular-java-docker-jenkins/sample-app-frontend:${BUILD_TAG}")
                /* Push the container to the Registry */
                echo "----------------- Pushing UI image to registry -----------------"
                uiImage.push()
            
            }
  
            docker.withRegistry('https://ghcr.io', GIT_CREDENTIALS_ID) {
  
                def backendImage = docker.image("ghcr.io/navalgandhi1989/angular-java-docker-jenkins/sample-app-backend:${BUILD_TAG}")
                /* Push the container to the Registry */
                echo "----------------- Pushing Backend image to registry -----------------"
                backendImage.push()
            
            }
        }
  
        echo "----------------- Finished -----------------"
        echo "----------------- Build Number : ${BUILD_TAG} -----------------"
    }
  }
}
}

post {
	always {
		echo "Pipeline completed"
	}
}
}
