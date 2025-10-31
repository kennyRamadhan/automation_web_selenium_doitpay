pipeline {
    agent any

    tools {
        jdk 'jdk-22'
        maven 'maven-latest'
    }

    environment {
        PATH = "/opt/homebrew/bin:${PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "📦 Cloning repository from GitHub..."
                git branch: 'main', url: 'https://github.com/kennyRamadhan/automation_web_selenium_doitpay.git'
            }
        }

        stage('Build & Run TestNG Tests') {
            steps {
                echo " Running Selenium TestNG tests..."
                sh 'mvn clean test -DsuiteXmlFile=testng.xml'
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo " Generating Allure HTML Report..."
                sh 'allure generate allure-results --clean -o allure-report'
            }
        }

        stage('Publish Allure Report') {
            steps {
                echo "🚀 Publishing Allure Report to Jenkins Dashboard..."
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'allure-results']]
                ])
            }
        }

        stage('Archive Report Artifact') {
            steps {
                echo "📁 Archiving Allure report files..."
                archiveArtifacts artifacts: 'allure-report/**/*.*', fingerprint: true
            }
        }
    }

    post {
        success {
            echo "Pipeline finished successfully!"
        }
        failure {
            echo " Build failed! Check logs and allure-results for details."
        }
    }
}
