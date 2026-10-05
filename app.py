def handler(event, context):      
    name = event["queryStringParameters"]["name"]    
    return {                       
        "statusCode": 200,
        "body": f"Hello {name}"
    }
