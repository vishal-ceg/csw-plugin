package io.jenkins.plugins.tools;

import io.jenkins.plugins.sample.Gravity;

import java.util.stream.Stream;

public enum Scanner {
	SAST("sast"),
	DAST("dast"),
	OSS("oss"),
	CONTAINER("container");

	  private final String scanner;

	  Scanner(String scanner) {
	    this.scanner = scanner;
	  }

	  public String getScanner() {
	    return scanner;
	  }

	  public static Scanner getIfPresent(String scanner) {
	    if (scanner == null) {
	      return null;
	    }

	    return Stream.of(Scanner.values())
	                 .filter(entry -> entry.getScanner().equalsIgnoreCase(scanner))
	                 .findFirst().orElse(null);
	  }



}
