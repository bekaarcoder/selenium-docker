pipeline {
    agent any

    stages {
        stage ('Build Jar') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage ('Build Docker Image') {
            steps {
                sh "docker build -t blitzstriker/selenium-java:latest ."
            }
        }

        stage ('Push Image') {
            environment {
                DOCKER_HUB = credentials('dockerhub-creds')
            }
            steps {
                sh 'echo ${DOCKER_HUB_PSW} | docker login -u ${DOCKER_HUB_USR} --password-stdin'
                sh "docker push blitzstriker/selenium-java:latest"
                sh "docker tag blitzstriker/selenium-java:latest blitzstriker/selenium-java:${env.BUILD_NUMBER}"
                sh "docker push blitzstriker/selenium-java:${env.BUILD_NUMBER}"
            }
        }
    }

    post {
        always {
            sh "docker logout"
        }
    }
}