FROM gradle:8.0-jdk17

WORKDIR /app

COPY /app .

RUN gradle clean installShadowDist

CMD ./build/install/app/bin/app