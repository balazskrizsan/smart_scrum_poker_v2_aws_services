FROM ubuntu:jammy

COPY target/aws_services /aws_services

CMD ["/smart_scrum_poker_backend_native"]
