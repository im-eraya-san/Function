FROM public.ecr.aws/lambda/python:3.9.2023.06.27.12-x86_64
COPY app.py .
CMD [app.handler]
