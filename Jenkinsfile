@Library('ECR') _

pipeline{
    agent any
    stages{
        stage("Login to aws"){
            steps{
                script{
                    handler.awsLogin()
                }
            }
        }
        
    }
}
