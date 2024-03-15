# Makefile

install:
	./gradlew clean build

run:
	./gradlew run

test:
	./gradlew test

lint:
	./gradlew checkstyleMain

clean:
	./gradlew clean

run-dist:
	./build/install/app/bin/app

generate-test-report:
	./gradlew jacocoTestReport

.PHONY: build
.PHONY: test
.PHONY: jacocoTestReport