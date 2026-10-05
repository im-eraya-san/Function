def getRegion(){
   
//    This function takes aws region from Jenkins cred store
//    it is for security purpose.
   
    withCredentials([
        string(credentialsId: 'REGION', variable: 'Region')
    ]){ return Region } 
}

def awsLogin(){
    def REGION = getRegion()

// Apply aws cred according to region
    withAWS(credentials: 'AWS', region: REGION){
            withCredentials([
                usernamePassword(
                    // Get userName and Password of ECR
                    credentialsId: 'ECR-CRED',
                    usernameVariable: 'uname',
                    passwordVariable: 'passwd'
                )
            ]){
                // Login to ECR
                sh 'aws ecr get-login-password --region "$AWS_REGION"| docker login --username "$uname" --password-stdin "$passwd"'
            }
    }
}

def makeBuild(String imageName){
    sh "docker build --provenance=false --sbom=false -t ${imageName} ."

    withCredentials([
        string(credentialsId: 'ECR-ENDPOINT', variable: 'ecrEndpoint')
    ]){ 
        sh "docker image tag ${imageName} ${ecrEndpoint}:${env.BUILD_ID}"
        sh "docker push ${ecrEndpoint}:${env.BUILD_ID}"
     } 
}
