# Function
Function in Python, packaged as a Docker container image and exposed through API Gateway.

## Project Structure
 
```
.
├── Dockerfile
└── app.py
```
 
## How It Works
 
`app.py` defines the Lambda handler:
 
```python
def handler(event, context):
    name = event["queryStringParameters"]["name"]
    return {
        "statusCode": 200,
        "body": f"Hello {name}"
    }
```
 
The function expects an API Gateway / Function URL style event containing `queryStringParameters.name`. It responds with `Hello <name>`.
 
## Requirements
 
- Docker
- AWS CLI (for deployment)
- An AWS account with permission to use ECR and Lambda
## Build the Image
 
```bash
docker build -t hello-lambda .
```
 
## Run Locally
 
The AWS Lambda base image includes the Runtime Interface Emulator, so you can test without deploying.
 
```bash
docker run --rm -p 9000:8080 hello-lambda
```
 
In another terminal, invoke the function:
 
```bash
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" \
  -d '{"queryStringParameters": {"name": "World"}}'
```
 
Expected response:
 
```json
{"statusCode": 200, "body": "Hello World"}
```
 
## Deploy to AWS
 
1. **Create an ECR repository**
```bash
   aws ecr create-repository --repository-name hello-lambda --region <region>
```
 
2. **Authenticate Docker to ECR**
```bash
   aws ecr get-login-password --region <region> | \
     docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com
```
 
3. **Tag and push the image**
```bash
   docker tag hello-lambda:latest <account-id>.dkr.ecr.<region>.amazonaws.com/hello-lambda:latest
   docker push <account-id>.dkr.ecr.<region>.amazonaws.com/hello-lambda:latest
```
 
4. **Create the Lambda function**
```bash
   aws lambda create-function \
     --function-name hello-lambda \
     --package-type Image \
     --code ImageUri=<account-id>.dkr.ecr.<region>.amazonaws.com/hello-lambda:latest \
     --role arn:aws:iam::<account-id>:role/<lambda-execution-role>
```
 
5. **(Optional) Expose it over HTTP** with an API Gateway or a Lambda Function URL, then call:
```
   https://<endpoint>?name=World
```
 
## Notes
 
- The base image is `public.ecr.aws/lambda/python:3.9`. Python 3.9 is end-of-life, so consider upgrading to a newer Python base image.
- The handler will raise an error if `name` is not provided. Add validation if the function will be public.
- The image targets `x86_64`. Use the matching `arm64` base image if you deploy to Graviton.
