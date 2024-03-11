# Makefile

build:
	./gradlew clean
	./gradlew build

run:
	./gradlew run

test:
	./gradlew test

lint:
	./gradlew checkstyleMain

install-dist:
	./gradlew installDist

run-dist:
	./build/install/app/bin/app

generate-test-report:
	./gradlew jacocoTestReport

.PHONY: build
.PHONY: test
.PHONY: jacocoTestReport