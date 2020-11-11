package io.jenkins.plugins.sample;

import java.util.stream.Stream;

public enum Gravity {
	
	  LOW("low"),
	  MEDIUM("medium"),
	  HIGH("high");

	  private final String gravity;

	  Gravity(String gravity) {
	    this.gravity = gravity;
	  }

	  public String getGravity() {
	    return gravity;
	  }

	  public static Gravity getIfPresent(String gravity) {
	    if (gravity == null) {
	      return null;
	    }

	    return Stream.of(Gravity.values())
	                 .filter(entry -> entry.getGravity().equalsIgnoreCase(gravity))
	                 .findFirst().orElse(null);
	  }

}
