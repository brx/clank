sbt info test-compile && \
    exec scala -cp lib_managed/scala_2.8.0/compile/scalacheck_2.8.0-1.7.jar:\
lib_managed/scala_2.8.0/compile/scalatest-1.2.jar:\
lib_managed/scala_2.8.0/compile/test-interface-0.3.jar:\
target/scala_2.8.0/classes/ \
    org.scalatest.tools.Runner -p target/scala_2.8.0/test-classes -g -m com.clank.tests
