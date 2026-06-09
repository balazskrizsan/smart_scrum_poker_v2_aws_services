FROM ubuntu:jammy

COPY target/aws_services /aws_services

CMD ["/aws_services"]
