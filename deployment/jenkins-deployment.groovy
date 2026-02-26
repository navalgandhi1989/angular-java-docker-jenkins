pipeline {
	agent any

parameters {
	string(name: 'BACKEND_BUILD_NUMBER', description: 'Build number for Backend')
	string(name: 'UI_BUILD_NUMBER', description: 'Build number for UI')
	choice(name: 'TARGET_ENVIRONMENT', choices: ["UAT", "PRODUCTION"], description: 'Target Environment')
}

environment {
	SSH_USER = ''
	SSH_HOST = ''
	SSH_KEY = ''
	TARGET_DIR = ''
}

stages {
	stage('Print Build Numbers and set Environment') {
		steps {
			script {
				if(params.TARGET_ENVIRONMENT == "UAT") {
					SSH_USER = "root"
					SSH_HOST = "10.1.21.51"
					SSH_KEY = "SSH_KEY_CT-12251"
					TARGET_DIR = "/root/sample-app"
				} else if (params.TARGET_ENVIRONMENT == "PRODUCTION") {
					SSH_USER = "root"
					SSH_HOST = "10.1.22.51"
					SSH_KEY = "SSH_KEY_CT-13251"
					TARGET_DIR = "/root/sample-app"
				}
			}
		echo "Backend Build Number: ${params.BACKEND_BUILD_NUMBER}"
		echo "UI Build Number: ${params.UI_BUILD_NUMBER}"
		echo "Target Directory: ${TARGET_DIR}"
		}
	}

	stage('SSH to Server and Run Commands') {
		steps {
			sshagent(credentials: [SSH_KEY]) {
				sh """
				  [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
				  ssh-keyscan -t rsa,dsa ${SSH_HOST} >> ~/.ssh/known_hosts
				  
				  ssh -t -t  ${SSH_USER}@${SSH_HOST} 'bash -s << 'ENDSSH'
						  cd ${TARGET_DIR}
						  UI_BUILD_NUMBER=${params.UI_BUILD_NUMBER} BACKEND_BUILD_NUMBER=${params.BACKEND_BUILD_NUMBER} docker compose up -d
						  docker compose logs
						  exit
					ENDSSH'
				  """
			}
		}
	}
}
}