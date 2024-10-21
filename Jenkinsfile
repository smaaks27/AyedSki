pipeline {
    agent any

    environment {
        // SonarQube environment variables
        SCANNER_HOME = tool 'sonar' // Name of SonarQube installation
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from the repository
                git branch: 'Ayed', url: 'https://github.com/smaaks27/AyedSki.git'
            }
        }

        stage('Set Permissions') {
            steps {
                dir('gestion-station-ski') {
                    // Grant execute permission to the mvnw file
                    sh 'chmod +x mvnw'
                }
            }
        }

        stage('Build') {
            steps {
                // Clean and build the project with Maven
                dir('gestion-station-ski') {
                    sh './mvnw clean install'
                }
            }
        }

        stage('Test') {
            steps {
                // Run unit tests
                dir('gestion-station-ski') {
                    sh './mvnw test'
                }
            }
        }

         stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-cred') {
                    sh "$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=AyedSki -Dsonar.projectName='AyedSki' -Dsonar.java.binaries=gestion-station-ski/target/classes"
                }
            }
        }

        stage('Package') {
            steps {
                // Package the application (e.g., create a JAR file)
                dir('gestion-station-ski') {
                    sh './mvnw package'
                }
            }
        }

        stage('Deploy') {
            steps {
                // Deploy the application, adjust as per your deploy method (SSH, Docker, Kubernetes, etc.)
                echo 'Deploying application...'
                // Example for copying JAR file to remote server:
                // sh 'scp target/gestion-station-ski.jar user@remote.server:/path/to/deploy'
            }
        }
    }

    post {
        success {
            echo 'Build and Deployment completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
