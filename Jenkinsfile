@Library('lambda') _

pipeline{
    agent any
    stages{
        stage("Login to aws"){
            steps{
                script{
                    uatSecret.awsLogin()
                }
            }
        }
        
    }
}
