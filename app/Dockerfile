FROM gradle:8.0-jdk20

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD gradle run