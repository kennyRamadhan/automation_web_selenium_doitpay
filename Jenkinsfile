pipeline {
    agent any

    parameters {
        choice(
            name: 'SUITE',
            choices: ['WebSuite.xml', 'APISuite.xml'],
            description: 'Pilih TestNG suite yang ingin dijalankan'
        )

           choice(
            name: 'BROWSER_MODE',
            choices: ['normal', 'headless'],
            description: 'Pilih mode browser untuk WebUI tests'
        )
    }

    tools {
        jdk 'jdk22'             // Sesuai nama konfigurasi JDK di Jenkins
        maven 'maven3'          // Sesuai nama konfigurasi Maven di Jenkins
        allure 'allure'  // Sesuai nama Allure tool yang di-install di Jenkins
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
                // Plugin Allure Jenkins akan otomatis gunakan Allure tool
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
            echo ' Build and tests completed successfully!'
        }
        failure {
            echo 'Build failed! Check logs and allure-results for details.'
        }
    }
}
