SRC = $(shell find . -name "*.java" -not -path "./bin/*")

all:
	mkdir -p bin
	javac -d bin $(SRC)

run: all
	java -cp bin Main

clean:
	rm -rf bin

.PHONY: all run clean
