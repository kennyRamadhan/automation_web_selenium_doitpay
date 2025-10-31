pipeline {
    agent any

    parameters {
        choice(
            name: 'SUITE',
            choices: ['WebSuite.xml', 'APISuite.xml'],
            description: 'Pilih TestNG suite yang ingin dijalankan'
        )
    }
    
     tools {
        jdk 'jdk22'             // Sesuai nama konfigurasi JDK di Jenkins
        maven 'maven3'          // Sesuai nama konfigurasi Maven di Jenkins
    }

    environment {
        ALLURE_HOME = "/opt/homebrew/bin/allure"
        
    }

    stages {
        stage('Checkout') {
            steps {
                echo "📦 Cloning repository..."
                git branch: 'main', url: 'https://github.com/kennyRamadhan/automation_web_selenium_doitpay.git'
            }
        }

        stage('Run Test Suite') {
            steps {
                echo "🧪 Running selected suite: ${params.SUITE}"
                sh "mvn clean test -DsuiteFile=${params.SUITE}"
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo "📊 Generating Allure report..."
                sh 'allure generate --clean allure-results -o allure-report || true'
            }
        }

        stage('Publish Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'allure-results']]
                ])
            }
        }

        stage('Archive Report Artifact') {
            steps {
                archiveArtifacts artifacts: 'allure-report/**', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build and tests completed successfully!'
        }
        failure {
            echo '❌ Build failed! Check logs and allure-results for details.'
        }
    }
}
