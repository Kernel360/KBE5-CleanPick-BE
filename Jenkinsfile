pipeline {
    agent any

    environment {
        PROJECT_DIR = '/home/ubuntu/KBE5-CleanPick-BE'
        DEPLOY_SCRIPT = '/home/ubuntu/scripts/deploy.sh'
    }

    stages {
        stage('Clone') {
            steps {
                echo '[+] GitHub에서 코드 가져오기'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '[+] Gradle 빌드 실행'
                sh './gradlew clean build -x test'
            }
        }

        stage('Deploy') {
            steps {
                echo '[+] 배포 스크립트 실행'
                sh 'bash ${DEPLOY_SCRIPT}'
            }
        }
    }
}
