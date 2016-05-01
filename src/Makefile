LIBDIR=../lib

# Run the generated tests for the GoLite compiler.
run_tests: compile_tests
	-java -cp $(LIBDIR)/hamcrest-core-1.3.jar:$(LIBDIR)/junit-4.12.jar:$(LIBDIR)/assertj-core-3.3.0.jar: org.junit.runner.JUnitCore test.GoLiteTestSuite > run_tests.log
	less run_tests.log
	rm -f run_tests.log

# Compile the generated tests for the GoLite compiler.
compile_tests: build_tests
	javac -cp $(LIBDIR)/junit-4.12.jar:$(LIBDIR)/assertj-core-3.3.0.jar: test/*.java

# Build the tests for the GoLite compiler (if the BUILD_TESTS_OPTS includes '-r', then the generated tests are for Vince's reference GoLite compiler).
build_tests: build
	bash -c "if [[ '$(BUILD_TESTS_OPTS)' == '' ]]; \
	then \
		python build_tests/build_tests.py -i build_tests/test_ignore.txt; \
	else \
		python build_tests/build_tests.py $(BUILD_TESTS_OPTS); \
	fi"
	
# Build the source.
build: compile_grammar
	javac -cp $(LIBDIR)/commons-cli-1.3.1.jar: golite/*.java golite/lexer/*.java golite/parser/*.java golite/node/*.java golite/analysis/*.java golite/*/*.java

# Compile the SableCC 3 grammar.
compile_grammar: clean
	java -jar ../sablecc-3.7/lib/sablecc.jar golite.sablecc -d .

# Clean the source by removing automatically generated source and test files, and .class files.
clean:
	rm -rf golite/lexer golite/parser golite/node golite/analysis test/
	find . -name "*.class" -type f -delete
