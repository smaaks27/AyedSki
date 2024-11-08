pipeline {
    agent any

    environment {
        // SonarQube environment variables
        SCANNER_HOME = tool 'sonar' // Name of SonarQube installation
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "AyedSki"
        NEXUS_CREDENTIAL_ID = "nexus-cred"
        DOCKER_IMAGE = "smaxxxxx/spring-boot-app"
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
                    sh './mvnw test jacoco:report'
                    sh './mvnw jacoco:report'
                }
            }
        }

         stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-cred') {
                    sh "$SCANNER_HOME/bin/sonar-scanner -DPcoverage -Dsonar.projectKey=AyedSki -Dsonar.projectName='AyedSki' -Dsonar.java.binaries=gestion-station-ski/target/classes -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
                }
            }
        }

        stage('Package') {
            steps {
                // Package the application (e.g., create a JAR file)
                dir('gestion-station-ski') {
                    sh './mvnw package -DskipTests'
                }
            }
        }

        stage("Publish to Nexus Repository Manager") {
            steps {
                dir('gestion-station-ski') {
                    script {
                        pom = readMavenPom file: "pom.xml";
                        filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                        echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                        artifactPath = filesByGlob[0].path;
                        artifactExists = fileExists artifactPath;
                        if(artifactExists) {
                            echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                            nexusArtifactUploader(
                                nexusVersion: NEXUS_VERSION,
                                protocol: NEXUS_PROTOCOL,
                                nexusUrl: NEXUS_URL,
                                groupId: pom.groupId,
                                version: pom.version,
                                repository: NEXUS_REPOSITORY,
                                credentialsId: NEXUS_CREDENTIAL_ID,
                                artifacts: [
                                    [artifactId: pom.artifactId,
                                    classifier: '',
                                    file: artifactPath,
                                    type: pom.packaging],
                                    [artifactId: pom.artifactId,
                                    classifier: '',
                                    file: "pom.xml",
                                    type: "pom"]
                                ]
                            );
                        } else {
                            error "*** File: ${artifactPath}, could not be found";
                        }
                    }
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        def NEXUS_GROUP_ID = "tn.esprit.spring"
                        def NEXUS_ARTIFACT_ID = "gestion-station-ski"
                        def NEXUS_VERSION_NUMBER = "1.0"
                        // Build Docker image with Nexus JAR
                        sh """
                            docker build -t ${DOCKER_IMAGE}:latest \
                            --build-arg NEXUS_URL=${NEXUS_PROTOCOL}://${NEXUS_URL} \
                            --build-arg NEXUS_REPO=${NEXUS_REPOSITORY} \
                            --build-arg NEXUS_GROUP_ID=${NEXUS_GROUP_ID} \
                            --build-arg NEXUS_ARTIFACT_ID=${NEXUS_ARTIFACT_ID} \
                            --build-arg NEXUS_VERSION=${NEXUS_VERSION_NUMBER} \
                            --build-arg NEXUS_USERNAME=${NEXUS_USERNAME} \
                            --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} \
                            .
                        """
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-cred', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    }
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }
        stage('Docker Compose Up') {
            steps {
                script {
                    // Pull the latest Docker image
                    sh "docker pull ${DOCKER_IMAGE}:latest"

                    // Run docker-compose up to start the application
                    sh "docker stop mysql-container"
                    sh "docker compose up -d"
                }
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
